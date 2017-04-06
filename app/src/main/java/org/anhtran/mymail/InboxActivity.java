package org.anhtran.mymail;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.anhtran.mymail.session.SharePreferencesConstants;
import org.anhtran.mymail.loader.CheckMailLoader;
import org.anhtran.mymail.mail.MailItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class InboxActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MailItem>> {

    private static final String LOG_TAG = InboxActivity.class.getSimpleName();

    private static final int MAIL_LOADER_ID = 1;
    SharedPreferences preferences;
    private MailAdapter mMailAdapter;
    private Map<Integer, Integer> msgNumbers;
    private TextView emptyView;
    private ProgressBar spinner;
    private ListView mMailListView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        // initialize UI components
        initializeWidgets();
        // Add behavior on UI components
        addBehavior();

        if (!isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }

    // Method to initialize UI components
    private void initializeWidgets() {
        emptyView = (TextView) findViewById(R.id.empty_view);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.reply_fab);
        mMailListView = (ListView) findViewById(R.id.mail_list_view);
    }

    private void addBehavior() {
        // Get shared preference
        preferences = getApplicationContext().getSharedPreferences(
                SharePreferencesConstants.PREFERENCE_NAME,
                SharePreferencesConstants.PRIVATE_MODE);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, MailEditorActivity.class);
                startActivity(intent);
            }
        });

        settingUpListView();
    }

    private void settingUpListView() {
        mMailAdapter = new MailAdapter(this, new ArrayList<MailItem>());

        mMailListView.setAdapter(mMailAdapter);

        // Get system connectivity manager
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get the active network information
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Check if the phone is connected with network
        boolean isConnected = (activeNetwork != null) &&
                (activeNetwork.isConnectedOrConnecting());

        // If the phone is connected with network then init the loader
        // else set no internet connection text to empty view
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MAIL_LOADER_ID, null, this);
        } else {
            emptyView.setText(R.string.no_internet);
            spinner.setVisibility(View.GONE);
        }

        // Set on mail item click listener to open the mail
        mMailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open email viewer to show email content
                Intent viewerIntent = new Intent(InboxActivity.this, EmailViewerActivity.class);

                // Put message number to intent extra then the opened activity will used it
                viewerIntent.putExtra(EmailViewerActivity.KEY_EXTRA, msgNumbers.get(position));

                // Start the activity
                startActivity(viewerIntent);
            }
        });
    }

    /**
     * Check the app is logged in or not
     *
     * @return Return true if the app is logged in with a mail account
     */
    private boolean isLogin() {
        return preferences.getBoolean(SharePreferencesConstants.IS_LOGGED_IN, false);
    }

    // Exit app on back pressed
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SharePreferencesConstants.IS_LOGGED_IN, false);
            editor.clear();
            editor.commit();

            // This intent is to open login activity
            Intent i = new Intent(this, LoginActivity.class);
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Start login activity with this intent
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<MailItem>> onCreateLoader(int id, Bundle args) {
        return new CheckMailLoader.AllMessageLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<MailItem>> loader, List<MailItem> mailItems) {
        spinner.setVisibility(View.GONE);

        // Clear old adapter content before loading new content to adapter
        mMailAdapter.clear();

        // If mail items list is not null and is not empty, add items list to mail adapter
        // otherwise set empty view to "No Email Found"
        if (mailItems != null && !mailItems.isEmpty()) {
            mMailAdapter.addAll(mailItems);
        } else {
            emptyView.setText(R.string.no_email);
        }

        // Get the message numbers list from mail adapter,
        // the numbers in this list are used to make intent to open mail
        msgNumbers = mMailAdapter.getMsgNumbers();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMailAdapter.clear();
    }
}
