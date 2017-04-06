package org.anhtran.mymail.mail;

import android.text.Html;
import android.text.SpannableString;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;


public class MailContent {
    private static String bodyContent = "";

    private MailContent() {
    }

    /*
     * This method checks for content-type
     * based on which, it processes and
     * fetches the content of the message
     */
    public static String get(Part p) throws Exception {


        if (p instanceof Message) {
            // Clear old content before adding new content
            clearContent();
            //Call method getHeader
            bodyContent += getHeader((Message) p);
        }
        //check if the content is html text
        if (p.isMimeType("text/html")) {
            bodyContent += p.getContent();
        }

        //check if the content is plain text
        else if (p.isMimeType("text/plain")) {
            bodyContent += p.getContent();
        }
        // If Mime type is multipart/alternative, only add html content, if html content
        // does not exist, uses plain text instead
        else if (p.isMimeType("multipart/alternative")) {
            boolean hasHtml = false;
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();

            // Check if multipart email contains text/html content or not
            for (int i = 0; i < count; i++) {
                Part altPart = mp.getBodyPart(i);
                if (altPart.isMimeType("text/html")) {
                    hasHtml = true;
                    break;
                }
            }

            // Start get email content
            for (int i = 0; i < count; i++) {
                // Get alternative body parts
                Part altPart = mp.getBodyPart(i);
                // If body part is text/html type, add to content
                if (altPart.isMimeType("text/html")) {
                    bodyContent += altPart.getContent();
                }
                // If body part is not html and is plain text then add to content
                else if (!hasHtml && altPart.isMimeType("text/plain")) {
                    String htmlContent = Html.toHtml(
                            new SpannableString((String) altPart.getContent()));
                    bodyContent += htmlContent;
                }
            }
        }
        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                get(mp.getBodyPart(i));
            }
        }
        //check if the content is a nested message
        else if (p.isMimeType("message/rfc822")) {
            get((Part) p.getContent());
        }

        return bodyContent;
    }

    /*
    * This method would print FROM,TO and SUBJECT of the message
    */
    public static String getHeader(Message m) throws Exception {
        Address[] from = m.getFrom();
        Date sentDate = m.getSentDate();
        String header = "";
        // FROM
        if (from != null) {
            header += "<b>From:</b> " + InternetAddress.toString(from);
            header += "<br>";
        }

        // Sent date
        if (sentDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm");
            header += "<b>Sent:</b> " + formatter.format(sentDate) + "<br>";
        }

        // SUBJECT
        if (m.getSubject() != null) {
            header += "<b>Subject:</b> " + m.getSubject();
            header += "<br>";
        }
        return header;
    }

    private static void clearContent() {
        bodyContent = "";
    }
}
