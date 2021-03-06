package org.anhtran.mymail;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.anhtran.mymail.loader.CheckMailLoader;
import org.anhtran.mymail.mail.MailItem;
import org.anhtran.mymail.mail.SendMail;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;


public class MailEditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<MailItem> {
    public static final String KEY_REPLY = "reply";
    private static final String LOG_TAG = MailEditorActivity.class.getSimpleName();
    private static final int MAIL_LOADER_ID = 1;

    private EditText editorTo;
    private EditText editorCc;
    private EditText editorSubject;
    private EditText editorContent;

    private String toEmail;
    private String ccEmail;
    private String subject;
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_editor);

        // Initialize views
        initializeWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.editor_menu_send:
                sendMail();
                return true;
            case R.id.editor_menu_save:
                return true;
            case android.R.id.home:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initializeWidgets() {
        editorTo = (EditText) findViewById(R.id.editor_to);
        editorCc = (EditText) findViewById(R.id.editor_cc);
        editorSubject = (EditText) findViewById(R.id.editor_subject);
        editorContent = (EditText) findViewById(R.id.editor_content);

        // If this activity was called with an intent extra KEY_REPLY
        // then change the title and chang hints in edit text components
        // and init loader
        if (getIntent().hasExtra(KEY_REPLY)) {
            setTitle(getString(R.string.editor_reply));
            editorTo.setHint(R.string.editor_loading);
            editorSubject.setHint(R.string.editor_loading);
            editorContent.setHint(R.string.editor_reply_message);
            getLoaderManager().initLoader(MAIL_LOADER_ID, null, this);
        }
    }

    private void sendMail() {
        getValues();
        SendMail sendMail = new SendMail(this, toEmail, ccEmail, subject, content);
        sendMail.execute();
    }

    private void getValues() {
        toEmail = editorTo.getText().toString().trim();
        ccEmail = editorCc.getText().toString().trim();
        subject = editorSubject.getText().toString().trim();
        if (content != null) {
            content = Html.toHtml(editorContent.getText()) + content;
        } else {
            content = Html.toHtml(editorContent.getText());
        }
    }

    /**
     * This method load the reply to information such as recipients, subject, previous content
     *
     * @param mailItem The mail item what will be parse to contents
     */
    private void loadReplyInfo(MailItem mailItem) {

        String replyTo = null;
        try {
            replyTo = InternetAddress.toString(mailItem.getMailMessage().getReplyTo());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        editorTo.setText(replyTo);
        String subject = mailItem.getSubject();
        if ("Re:".equals(subject.substring(0, 3))) {
            editorSubject.setText(subject);
        } else {
            editorSubject.setText("Re: " + subject);
        }

        content = mailItem.getContent();

    }

    @Override
    public Loader<MailItem> onCreateLoader(int id, Bundle args) {
        // Get extra information of intent from previous activity
        int msgNumber = getIntent().getExtras().getInt(KEY_REPLY);

        // Return new FetchMailLoader
        return new CheckMailLoader.MessageNumberLoader(
                this, msgNumber);
    }

    @Override
    public void onLoadFinished(Loader<MailItem> loader, MailItem data) {
        if (data != null) {
            loadReplyInfo(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<MailItem> loader) {

    }
}
