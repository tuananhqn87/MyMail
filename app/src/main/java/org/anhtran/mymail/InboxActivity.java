package org.anhtran.mymail;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.anhtran.mymail.mail.CheckMailLoader;
import org.anhtran.mymail.mail.MailAdapter;
import org.anhtran.mymail.mail.MailItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class InboxActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<MailItem>>{

    private static final String LOG_TAG = InboxActivity.class.getSimpleName();

    private static final int MAIL_LOADER_ID = 1;

    private MailAdapter mMailAdapter;
    private Map<Integer, Integer> msgNumbers;

    private TextView emptyView;
    private ProgressBar spinner;
    private ListView mMailListView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        // initialize UI components
        initializeWidgets();
        // Add behavior on UI components
        addBehavior();

    }
    // Method to initialize UI components
    private void initializeWidgets() {
        emptyView = (TextView) findViewById(R.id.empty_view);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.reply_fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mMailListView = (ListView) findViewById(R.id.mail_list_view);
    }

    private void addBehavior() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, MailEditorActivity.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        settingUpListView();


    }

    private void settingUpListView() {
        mMailAdapter = new MailAdapter(this, new ArrayList<MailItem>());

        mMailListView.setAdapter(mMailAdapter);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = (activeNetwork != null) &&
                (activeNetwork.isConnectedOrConnecting());
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


        mMailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open email viewer to show email content
                Intent viewerIntent = new Intent(InboxActivity.this, EmailViewerActivity.class);
                viewerIntent.putExtra(EmailViewerActivity.KEY_EXTRA, msgNumbers.get(position));
                startActivity(viewerIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<List<MailItem>> onCreateLoader(int id, Bundle args) {
        String imapHost = "imap.serdao.com";
        String storeType = "imap";
        String user = "tuananh.tran@serdao.com";
        String password = "Toimailatoi_87";
        return new CheckMailLoader(this, imapHost, storeType, user, password);
    }

    @Override
    public void onLoadFinished(Loader<List<MailItem>> loader, List<MailItem> mailItems) {
        spinner.setVisibility(View.GONE);

        mMailAdapter.clear();

        if( mailItems !=null && !mailItems.isEmpty()) {
            mMailAdapter.addAll(mailItems);
        } else {
            emptyView.setText(R.string.no_email);
        }
        msgNumbers = mMailAdapter.getMsgNumbers();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMailAdapter.clear();
    }
}
