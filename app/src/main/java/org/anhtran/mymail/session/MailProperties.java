package org.anhtran.mymail.session;

import android.util.Log;

import java.util.Properties;

public class MailProperties {
    public static final String MAIL = "mail.";
    public static final String PROTOCOL_IMAP = "imap";
    public static final String PROTOCOL_IMAPS = "imaps";
    public static final String PROTOCOL_POP3 = "pop3";
    public static final String PROTOCOL_POP3S = "pop3s";
    public static final String PROTOCOL_SMTP = "smtp";
    public static final String PROTOCOL_HOST = ".host";
    public static final String PROTOCOL_PORT = ".port";
    public static final String PROTOCOL_AUTH = ".auth";
    public static final String PROTOCOL_SOCKET_CLASS = ".socketFactory.class";
    public static final String PROTOCOL_SOCKET_PORT = ".socketFactory.port";
    public static final String PROTOCOL_TLS_ENABLE = ".starttls.enable";
    public static final String STORE_PROTOCOL = "mail.store.protocol";
    public static final String DEFAULT_IMAP_PORT = "143";
    public static final String SECURED_IMAP_PORT = "993";
    public static final String DEFAULT_SMTP_PORT = "25";
    public static final String SECURED_SMTP_PORT = "465";
    public static final String DEFAULT_POP3_PORT = "110";
    public static final String SECURE_POP3_PORT = "995";
    public static final String GMAIL_IMAP_SSL_PORT = "587";
    public static final String GMAIL_IMAP_HOST = "imap.gmail.com";
    public static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    public static final String JAVA_SSL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String ENABLE = "true";
    public static final String DISABLE = "false";
    private static final String LOG_TAG = MailProperties.class.getSimpleName();

    private MailProperties() {
    }

    public static Properties getSimpleImap(String host) {
        //create properties field
        Properties properties = new Properties();

        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_HOST, host);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_PORT, DEFAULT_IMAP_PORT);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_AUTH, ENABLE);

        return properties;
    }

    public static Properties getSimpleSmtp(String host) {
        //create properties field
        Properties properties = new Properties();

        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_HOST, host);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_PORT, DEFAULT_SMTP_PORT);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_AUTH, ENABLE);

        return properties;
    }

    public static Properties getSecuredSmtpSsl(String host) {
        Properties properties = new Properties();
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_HOST, host);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_SOCKET_PORT, SECURED_SMTP_PORT);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_SOCKET_CLASS,
                JAVA_SSL_SOCKET_FACTORY);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_AUTH, ENABLE);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_PORT, SECURED_SMTP_PORT);

        return properties;
    }

    public static Properties getSecuredImapSsl(String host) {

        Properties properties = new Properties();
        properties.put(STORE_PROTOCOL, PROTOCOL_IMAPS);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_HOST, host);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_SOCKET_PORT, SECURED_IMAP_PORT);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_SOCKET_CLASS,
                JAVA_SSL_SOCKET_FACTORY);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_AUTH, ENABLE);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_PORT, SECURED_IMAP_PORT);
        return properties;
    }

    public static Properties getGmailSmtpSsl() {
        Properties properties = new Properties();
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_HOST, GMAIL_SMTP_HOST);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_SOCKET_PORT, SECURED_SMTP_PORT);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_SOCKET_CLASS,
                JAVA_SSL_SOCKET_FACTORY);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_AUTH, ENABLE);
        properties.put(MAIL + PROTOCOL_SMTP + PROTOCOL_PORT, SECURED_SMTP_PORT);

        return properties;
    }

    public static Properties getGmailImapSsl() {
        Log.e(LOG_TAG, "getGmailImapSsl() called");
        Properties properties = new Properties();
        properties.put(STORE_PROTOCOL, PROTOCOL_IMAPS);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_HOST, GMAIL_IMAP_HOST);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_SOCKET_PORT, GMAIL_IMAP_SSL_PORT);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_SOCKET_CLASS,
                JAVA_SSL_SOCKET_FACTORY);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_AUTH, ENABLE);
        properties.put(MAIL + PROTOCOL_IMAP + PROTOCOL_PORT, GMAIL_IMAP_SSL_PORT);
        return properties;
    }

}
