package org.anhtran.mymail.utils;


import android.util.Log;

import org.anhtran.mymail.mail.DateCompare;
import org.anhtran.mymail.mail.MailItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;



public class CheckMail{
    private static final String LOG_TAG = CheckMail.class.getSimpleName();

    public static ArrayList<MailItem> check(String host, String storeType, String user,
                                           String password) {

        ArrayList<MailItem> mailItems = new ArrayList<>();
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "143");
            properties.put("mail.imap.auth", "true");

            Session emailSession = Session.getInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore(storeType);

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(FetchProfile.Item.ENVELOPE);
            fetchProfile.add(FetchProfile.Item.FLAGS);
            fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
            fetchProfile.add("Message-ID");


            // retrieve the messages from the folder in an array and print it

            Message[] messages = emailFolder.getMessages();

            emailFolder.fetch(messages, fetchProfile);

            List<Message> list = Arrays.asList(messages);
            //Sort messages list for more recent dates show first
            DateCompare.DescendingOrderCompare dateCompare =
                    new DateCompare.DescendingOrderCompare();
            Collections.sort(list,dateCompare);

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
}
