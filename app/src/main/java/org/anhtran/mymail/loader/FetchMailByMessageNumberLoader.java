package org.anhtran.mymail.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.anhtran.mymail.mail.MailItem;
import org.anhtran.mymail.mail.FetchMail;

/**
 * Created by anhtran on 3/27/17.
 */

public class FetchMailByMessageNumberLoader extends AsyncTaskLoader<MailItem> {
    private String host;
    private String storeType;
    private String user;
    private String password;
    private int msgNumber;

    public FetchMailByMessageNumberLoader
            (Context context, String host,
             String storeType, String user,
             String password, int msgNumber) {
        super(context);
        this.host = host;
        this.storeType = storeType;
        this.user = user;
        this.password = password;
        this.msgNumber = msgNumber;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public MailItem loadInBackground() {
        if (host == null || storeType == null || user == null || password == null) {
            return null;
        }
        FetchMail fetchMail = new FetchMail(host, storeType, user, password);
        MailItem mailItem = fetchMail.fetchByMessageNumber(msgNumber);
        return mailItem;
    }
}
