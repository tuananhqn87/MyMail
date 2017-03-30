package org.anhtran.mymail.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.anhtran.mymail.mail.MailItem;

import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.MessageNumberTerm;
import javax.mail.search.SearchTerm;



public class FetchMail {
    private static final String LOG_TAG = FetchMail.class.getSimpleName();
    // Declares email content
    private static String content = "";

    // Declare session information
    private String host;
    private String storeType;
    private String user;
    private String password;
    private Message mMessage;

    public FetchMail(String host, String storeType, String user,
                     String password) {
        this.host = host;
        this.storeType = storeType;
        this.user = user;
        this.password = password;
    }

    private Properties getProperties() {
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "143");
        properties.put("mail.imap.auth", "true");

        return properties;
    }


    private void clearContent() {
        content = "";
    }

    public MailItem fetchByMessageNumber(int messageNumber) {
        MailItem mailItem;

        try {

            Session emailSession = Session.getInstance(getProperties());

            //create the store object and connect with the server
            Store store = emailSession.getStore(storeType);

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Create fetch profile what used to fetch email
            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(FetchProfile.Item.ENVELOPE);
            fetchProfile.add(FetchProfile.Item.FLAGS);
            fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
            fetchProfile.add("Message-ID");

            // Declares search term to search message by message number
            SearchTerm searchTerm = new MessageNumberTerm(messageNumber);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.search(searchTerm);

            // Fetch email
            emailFolder.fetch(messages,fetchProfile);

            // Because message is gotten by message number
            // so messages array only has one message
            mMessage = messages[0];

            // Clear old content before adding new content
            clearContent();

            // Add new content
            content = MailContent.get(mMessage);

            // close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             mailItem = new MailItem(mMessage, content);
        }
        return mailItem;
    }
}

