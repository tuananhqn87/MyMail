package org.anhtran.mymail;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.anhtran.mymail.loader.CheckMailLoader;
import org.anhtran.mymail.mail.MailItem;

public class EmailViewerActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<MailItem> {
    // Key of intent extra
    public static final String KEY_EXTRA = "MailMessage";

    // Log tag to find the log when debug
    private static final String LOG_TAG = InboxActivity.class.getSimpleName();

    // Loader ID for initializing loader
    private static final int MAIL_LOADER_ID = 1;

    // Declares web view where email's content will appear
    private WebView emailViewer;

    // Spinner is used when waiting for loader finished
    private ProgressBar viewerSpinner;

    // The floating button to reply email
    private FloatingActionButton replyFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_viewer);

        // Init views
        initializeWidgets();

        // Add views' behavior
        addBehavior();
    }

    private void initializeWidgets() {
        replyFab = (FloatingActionButton) findViewById(R.id.reply_fab);
        emailViewer = (WebView) findViewById(R.id.email_viewer);
        viewerSpinner = (ProgressBar) findViewById(R.id.viewer_spinner);
    }

    public void addBehavior() {

        // Set on click listener of floating button
        replyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The intent to start reply activity
                Intent replyMailIntent = new Intent(
                        EmailViewerActivity.this, MailEditorActivity.class);
                // Put message ID that this activity receive from InboxActivity
                replyMailIntent.putExtra(MailEditorActivity.KEY_REPLY,
                        getIntent().getExtras().getInt(KEY_EXTRA));
                // Start the reply activity when this button is clicked
                startActivity(replyMailIntent);
            }
        });

        // Check network connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get network info
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = (activeNetwork != null) &&
                (activeNetwork.isConnectedOrConnecting());

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader
            loaderManager.initLoader(MAIL_LOADER_ID, null, this);
        } else {
            // Show no internet warning if there is no internet connection
            emailViewer.loadDataWithBaseURL(null, getText(R.string.no_internet).toString(),
                    "text/html", "utf-8", null);
            viewerSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        getMenuInflater().inflate(R.menu.viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.viewer_delete:
                return true;
            case R.id.viewer_forward:
                return true;
            case android.R.id.home:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<MailItem> onCreateLoader(int id, Bundle args) {
        // Get extra information of intent from previous activity
        int msgNumber = getIntent().getExtras().getInt(KEY_EXTRA);

        // Return new FetchMailLoader
        return new CheckMailLoader.MessageNumberLoader(this, msgNumber);
    }

    @Override
    public void onLoadFinished(Loader<MailItem> loader, MailItem email) {
        // Load data to WebView
        emailViewer.loadDataWithBaseURL(null, email.getContent(),
                "text/html", "utf-8", null);
        // Hide progress bar spinner from view
        viewerSpinner.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<MailItem> loader) {
        emailViewer.clearCache(true);
        emailViewer.clearHistory();
    }
}
