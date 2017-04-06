package org.anhtran.mymail.session;

import android.content.Context;

public class SharePreferencesConstants {
    public static final String PREFERENCE_NAME = "Login_Preference";
    public static final int PRIVATE_MODE = Context.MODE_PRIVATE;

    public static final String KEY_EMAIL_ID = "email_id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IMAP_HOST = "imap_host";
    public static final String KEY_SMTP_HOST = "smtp_host";

    public static final String KEY_PROVIDER = "provider";
    public static final String PROVIDER_GMAIL = "Gmail";
    public static final String PROVIDER_OTHER = "Other";

    public static final String KEY_IMAP_PORT = "imap_port";
    public static final String KEY_SMTP_PORT = "smtp_port";


    public static final String IS_LOGGED_IN = "is_loged_in";
}
