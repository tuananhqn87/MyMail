package org.anhtran.mymail.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = SendMail.class.getSimpleName();

    private static final String EMAIL_ID = "tuananh.tran@serdao.com";
    private static final String PASSWORD = "Toimailatoi_87";

    private Context context;

    /* Information to send toEmail */
    private String toEmail;
    private String subject;
    private String message;
    private String ccEmail;

    // Declare progress dialog to show when sending toEmail
    private ProgressDialog progressDialog;

    /**
     * Constructor of SendMail class
     * @param context The context of action send mail
     * @param toEmail Email addresses that email will send to
     * @param ccEmail Email addresses that email will cc to
     * @param subject The email subject
     * @param message The email body
     */
    public SendMail (Context context, String toEmail,
                     String ccEmail, String subject,
                     String message) {
        this.context = context;
        this.toEmail = toEmail;
        this.ccEmail = ccEmail;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Before sending email, show the progress dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Show the progress dialog before sending email
        progressDialog = ProgressDialog.show(context,
                "Sending Message", "Please wait...", false, false);
    }

    /**
     * Defines actions that will be do in background task
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {
        // Declares the properties settings to connect to SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.serdao.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");

        // Create the connection session with SMTP server
        Session session = Session.getInstance(props,

                // Create an authenticator that is used to authenticate against server
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_ID, PASSWORD);
                    }
                });


        // Try to send the message and return error if any error happened
        try {
            // Declares MIME message which will be sent
            MimeMessage mm = new MimeMessage(session);

            // Set email address which sends the mail from
            mm.setFrom(new InternetAddress(EMAIL_ID));

            // Add recipients
            mm.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            mm.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));

            // Set the email subject
            mm.setSubject(subject,"utf-8");

            // Set the body content of email
            mm.setText(message, "utf-8","html");
            // Set sent date
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            mm.setSentDate(currentDate);

            // Send the email
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Dismiss progress dialog after sending email
        progressDialog.dismiss();
        Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();

        // Finish the activity that
        ((Activity)context).finish();
    }

}
