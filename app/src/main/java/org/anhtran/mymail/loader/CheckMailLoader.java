package org.anhtran.mymail.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.anhtran.mymail.mail.MailItem;
import org.anhtran.mymail.mail.CheckMail;

import java.util.List;

import static android.R.attr.host;
import static android.R.attr.password;

public class CheckMailLoader {

    public static class AllMessageLoader extends AsyncTaskLoader<List<MailItem>> {

        private Context context;

        public AllMessageLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<MailItem> loadInBackground() {
            return CheckMail.check(context);
        }
    }

    public static class MessageNumberLoader extends AsyncTaskLoader<MailItem> {
        private Context context;
        private int msgNumber;

        public MessageNumberLoader
                (Context context, int msgNumber) {
            super(context);
            this.context = context;
            this.msgNumber = msgNumber;

        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public MailItem loadInBackground() {

            MailItem mailItem = CheckMail.openMail(context, msgNumber);
            return mailItem;
        }
    }

}
