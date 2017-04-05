package org.anhtran.mymail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.anhtran.mymail.session.MailProperties;

import static org.anhtran.mymail.session.SharePreferencesConstants.*;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String SECURITY_NOT_SECURED = "Not Secured";
    public static final String SECURITY_SECURED_SSL = "Secured with SSL";

    private final boolean VISIBLE = true;
    private final boolean GONE = false;
    LinearLayout incomingServerLayout;
    LinearLayout outgoingServerLayout;
    LinearLayout securityLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner providerSpinner;
    private EditText incomingHostEditText;
    private EditText incomingPortEditText;
    private EditText outgoingHostEditText;
    private EditText outgoingPortEditText;
    private Spinner securitySpinner;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeWidgets();
        setConnectOnClickListener();
    }

    private void initializeWidgets() {
        emailEditText = (EditText) findViewById(R.id.login_edittext_email);
        passwordEditText = (EditText) findViewById(R.id.login_edittext_password);

        providerSpinner = (Spinner) findViewById(R.id.login_spinner_provider);

        incomingHostEditText = (EditText) findViewById(R.id.login_edittext_incoming_server);
        incomingPortEditText = (EditText) findViewById(R.id.login_edittext_incoming_port);

        outgoingHostEditText = (EditText) findViewById(R.id.login_edittext_outgoing_server);
        outgoingPortEditText = (EditText) findViewById(R.id.login_edittext_outgoing_port);

        securitySpinner = (Spinner) findViewById(R.id.login_spinner_security);

        incomingServerLayout = (LinearLayout) findViewById(R.id.login_incoming_server);
        outgoingServerLayout = (LinearLayout) findViewById(R.id.login_outgoing_server);
        securityLayout = (LinearLayout) findViewById(R.id.login_security);

        connectButton = (Button) findViewById(R.id.login_connect);

        createProviderSpinner();
    }


    private void createProviderSpinner() {
        List<String> list = new ArrayList<>();
        list.add(PROVIDER_GMAIL);
        list.add(PROVIDER_OTHER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, list);

        providerSpinner.setAdapter(adapter);

        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (providerSpinner.getSelectedItem().equals(PROVIDER_OTHER)) {
                    setViewsVisibility(VISIBLE);
                    createSecuritySpinner();
                } else {
                    setViewsVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createSecuritySpinner() {
        List<String> list = new ArrayList<>();
        list.add(SECURITY_NOT_SECURED);
        list.add(SECURITY_SECURED_SSL);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, list);

        securitySpinner.setAdapter(adapter);

        securitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (securitySpinner.getSelectedItem().equals(SECURITY_SECURED_SSL)) {
                    incomingPortEditText.setText(MailProperties.SECURED_IMAP_PORT);
                    outgoingPortEditText.setText(MailProperties.SECURED_SMTP_PORT);
                } else {
                    incomingPortEditText.setText(MailProperties.DEFAULT_IMAP_PORT);
                    outgoingPortEditText.setText(MailProperties.DEFAULT_SMTP_PORT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setViewsVisibility(boolean show) {

        if (show == VISIBLE) {
            incomingServerLayout.setVisibility(View.VISIBLE);
            outgoingServerLayout.setVisibility(View.VISIBLE);
            securityLayout.setVisibility(View.VISIBLE);
        } else {
            incomingServerLayout.setVisibility(View.GONE);
            outgoingServerLayout.setVisibility(View.GONE);
            securityLayout.setVisibility(View.GONE);
        }
    }

    private void setConnectOnClickListener() {
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllViewValid()) {
                    saveToSharedPreferences();
                    Intent i = new Intent(LoginActivity.this, InboxActivity.class);

                    // Closing all the Activities from stack
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Please fill all empty fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isAllViewValid() {
        boolean isValid = true;
        if (providerSpinner.getSelectedItem().toString()
                .equals(PROVIDER_GMAIL)) {
            if (isViewEmpty(emailEditText)
                    || isViewEmpty(passwordEditText)) {
                isValid = false;
            }
        } else {
            if (isViewEmpty(emailEditText)
                    || isViewEmpty(passwordEditText)
                    || isViewEmpty(incomingHostEditText)
                    || isViewEmpty(incomingPortEditText)
                    || isViewEmpty(outgoingHostEditText)
                    || isViewEmpty(outgoingPortEditText)) {
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean isViewEmpty(EditText view) {
        return view.getText() == null
                || view.getText().toString().trim().equals("");
    }

    private void saveToSharedPreferences() {

        preferences = getApplicationContext().getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);

        editor = preferences.edit();
        editor.putString(KEY_EMAIL_ID, emailEditText.getText().toString().trim());

        editor.putString(KEY_PASSWORD, passwordEditText.getText().toString());

        editor.putString(KEY_PROVIDER, providerSpinner.getSelectedItem().toString());

        if (providerSpinner.getSelectedItem().toString().equals(PROVIDER_OTHER)) {

            editor.putString(KEY_IMAP_HOST, incomingHostEditText.getText().toString().trim());

            editor.putString(KEY_SMTP_HOST, outgoingHostEditText.getText().toString().trim());

            editor.putString(KEY_IMAP_PORT, incomingPortEditText.getText().toString());

            editor.putString(KEY_SMTP_PORT, outgoingPortEditText.getText().toString());

        } else {
            editor.putString(KEY_IMAP_HOST, MailProperties.GMAIL_IMAP_HOST);

            editor.putString(KEY_SMTP_HOST, MailProperties.GMAIL_SMTP_HOST);

            editor.putString(KEY_IMAP_PORT, MailProperties.GMAIL_IMAP_SSL_PORT);

            editor.putString(KEY_SMTP_PORT, MailProperties.SECURED_SMTP_PORT);
        }

        editor.putBoolean(IS_LOGGED_IN, true);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
