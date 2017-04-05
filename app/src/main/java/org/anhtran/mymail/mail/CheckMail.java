package org.anhtran.mymail.mail;


import android.content.Context;
import android.content.SharedPreferences;

import org.anhtran.mymail.session.MailSession;
import org.anhtran.mymail.utils.DateCompare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.search.MessageNumberTerm;
import javax.mail.search.SearchTerm;

import static org.anhtran.mymail.session.SharePreferencesConstants.*;

public class CheckMail {
    private static final String LOG_TAG = CheckMail.class.getSimpleName();

    private CheckMail() {
    }

    public static ArrayList<MailItem> check(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);

        String user = preferences.getString(KEY_EMAIL_ID, "");
        String password = preferences.getString(KEY_PASSWORD, "");
        String host = preferences.getString(KEY_IMAP_HOST, "");

        if (user.equals("") || password.equals("") || host.equals("")) {
            return null;
        }

        ArrayList<MailItem> mailItems = new ArrayList<>();
        try {

            Store store = MailSession.connectImapStore(context);

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it

            Message[] messages = emailFolder.getMessages();

            emailFolder.fetch(messages, getFetchProfile());

            List<Message> list = Arrays.asList(messages);
            //Sort messages list for more recent dates show first
            DateCompare.DescendingOrderCompare dateCompare =
                    new DateCompare.DescendingOrderCompare();
            Collections.sort(list, dateCompare);

            for (int i = 0, n = list.size(); i < n; i++) {
                Message message = list.get(i);

                int messageNumber = message.getMessageNumber();

                mailItems.add(new MailItem(message, messageNumber));

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mailItems;
    }

    public static MailItem openMail(Context context, int messageNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);

        String user = preferences.getString(KEY_EMAIL_ID, "");
        String password = preferences.getString(KEY_PASSWORD, "");
        String host = preferences.getString(KEY_IMAP_HOST, "");

        if (user.equals("") || password.equals("") || host.equals("")) {
            return null;
        }

        MailItem mailItem;
        Message mMessage = null;
        // Declares email content
        String content = "";

        try {

            //create the store object and connect with the server
            Store store = MailSession.connectImapStore(context);

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Declares search term to search message by message number
            SearchTerm searchTerm = new MessageNumberTerm(messageNumber);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.search(searchTerm);

            // Fetch email
            emailFolder.fetch(messages, getFetchProfile());

            // Because message is gotten by message number
            // so messages array only has one message
            mMessage = messages[0];

            // Clear old content before adding new content
            content = "";

            // Add new content
            content = MailContent.get(mMessage);

            // close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mMessage == null) {
                return null;
            }
            mailItem = new MailItem(mMessage, content);
        }
        return mailItem;
    }

    private static FetchProfile getFetchProfile() {
        // Create fetch profile what used to fetch email
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        fetchProfile.add(FetchProfile.Item.FLAGS);
        fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
        fetchProfile.add("X-mailer");

        return fetchProfile;
    }
}
