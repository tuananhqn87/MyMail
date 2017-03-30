package org.anhtran.mymail.utils;

import java.util.Properties;

public class MailProperties {

    private MailProperties() {
    }

    public static Properties getSimpleImap(String host) {
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "143");
        properties.put("mail.imap.auth", "true");

        return properties;
    }

    public static Properties getSimpleSmtp(String host) {
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");

        return properties;
    }
}
