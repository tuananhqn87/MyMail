package org.anhtran.mymail.mail;


import android.util.Log;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;


public class MailItem implements Serializable {
    private static final String LOG_TAG = MailItem.class.getSimpleName();

    private String content;
    private int messageNumber;
    private Message message;


    public MailItem(Message message, int messageNumber){
        this.message = message;
        this.messageNumber = messageNumber;
    }

    public MailItem(Message message, String content){
        this.message = message;
        this.content = content;
    }

    public String getFrom() {
        String from = null;
        try {
            from = String.valueOf(message.getFrom()[0]);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return from;
    }

    public String getSubject() {
        String subject = null;
        try {
            subject = message.getSubject();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return subject;
    }
    public String getContent() {
        if (content != null) {
            return content;
        }
        return null;
    }


    public String getReceivedDate() {
        Date receivedDate = null;
        try {
            receivedDate = message.getReceivedDate();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm");
        if (receivedDate == null){
            return null;
        }
        return format.format(receivedDate);
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public Message getMailMessage() {
        return message;
    }
}
