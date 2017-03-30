package org.anhtran.mymail.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.anhtran.mymail.mail.MailItem;
import org.anhtran.mymail.mail.CheckMail;

import java.util.List;

/**
 * Created by anhtran on 3/24/17.
 */

public class CheckMailLoader extends AsyncTaskLoader<List<MailItem>> {

    private String host;
    private String storeType;
    private String user;
    private String password;

    public CheckMailLoader(Context context, String host, String storeType, String user,
                           String password) {
        super(context);
        this.host = host;
        this.storeType = storeType;
        this.user = user;
        this.password = password;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MailItem> loadInBackground() {
        if (host == null || storeType == null || user == null || password == null) {
            return null;
        }
        List<MailItem> mailItems = CheckMail.check(host, storeType, user, password);
        return mailItems;
    }

}
