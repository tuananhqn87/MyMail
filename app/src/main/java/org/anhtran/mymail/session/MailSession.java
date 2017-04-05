package org.anhtran.mymail.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import static org.anhtran.mymail.session.SharePreferencesConstants.*;


public class MailSession {
    private static final String LOG_TAG = MailSession.class.getSimpleName();

    private MailSession() {
    }

    public static Session getSmtpSession(Context context) {

        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, PRIVATE_MODE);

        Properties properties;

        // If shared preference provider is Gmail then properties is Gmail SMTP properties
        // otherwise properties will be assigned simple SMTP properties with provided host
        if (sharedPreferences.getString(KEY_PROVIDER, "").equals(PROVIDER_GMAIL)) {
            properties = MailProperties.getGmailSmtpSsl();
        } else {
            properties = MailProperties.getSimpleSmtp(
                    sharedPreferences.getString(KEY_SMTP_HOST, ""));
        }

        // Create the connection session with SMTP server
        Session session = Session.getInstance(properties,

                // Create an authenticator that is used to authenticate against server
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                sharedPreferences.getString(KEY_EMAIL_ID, ""),
                                sharedPreferences.getString(KEY_PASSWORD, ""));
                    }
                });

        return session;
    }

    public static Store connectImapStore(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, PRIVATE_MODE);

        Properties properties;
        String storeType;

        // If shared preference provider is Gmail then properties is Gmail IMAP properties
        // and store type is IMAPS
        // otherwise properties will be assigned simple IMAP properties with provided host
        if (sharedPreferences.getString(KEY_PROVIDER, "").equals(PROVIDER_GMAIL)) {
            properties = MailProperties.getGmailImapSsl();
            storeType = MailProperties.PROTOCOL_IMAPS;
        } else {
            properties = MailProperties.getSimpleImap(
                    sharedPreferences.getString(KEY_IMAP_HOST, ""));
            storeType = MailProperties.PROTOCOL_IMAP;
        }

        // Create the connection session with IMAP server
        Session session = Session.getInstance(properties);

        //create the store object and connect with the IMAP server
        Store store = null;
        try {
            store = session.getStore(storeType);

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }
}
