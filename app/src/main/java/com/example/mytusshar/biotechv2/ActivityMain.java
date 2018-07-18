package com.example.mytusshar.biotechv2;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.profile_like_playlist.ActivityLiked;
import com.example.mytusshar.biotechv2.profile_like_playlist.ActivityPlaylist;
import com.example.mytusshar.biotechv2.profile_like_playlist.ActivitySharedVideos;
import com.example.mytusshar.biotechv2.search_utils.SearchActivity;
import com.example.mytusshar.biotechv2.signin_pkg.ActivityLogin;
import com.example.mytusshar.biotechv2.signin_pkg.SQLiteHandler;
import com.example.mytusshar.biotechv2.signin_pkg.SessionManager;
import com.example.mytusshar.biotechv2.tab_fragments.AreasFragment;
import com.example.mytusshar.biotechv2.tab_fragments.CategoriesFragment;



public class ActivityMain extends AppCompatActivity {

    static int SPLASH_COUNT = 0;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SessionManager session;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SPLASH_COUNT == 0){
            //calling ActivitySplash
            Intent intent = new Intent(this, ActivitySplash.class);
            startActivity(intent);
            SPLASH_COUNT ++;
        }

        //check login status, if logged in then start main activity otherwise ActivityLogin
        // SqLite database handler
        // session manager
        session = new SessionManager(getApplicationContext());
        checkLoginStatus();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /////////optional/////////////////////////
        mViewPager.setOffscreenPageLimit(0);
        /////////optional/////////////////////////

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        initNavigationDrawer();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
//                    case R.id.nav_recent:
//                        Toast.makeText(getApplicationContext(),"recent",Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id.nav_settings:
                        Intent intent4 = new Intent(ActivityMain.this, ActivitySetting.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_playlist:
                        Intent intent = new Intent(ActivityMain.this, ActivityPlaylist.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        SessionManager session = new SessionManager(ActivityMain.this);
                        session.setLogin(false);
                        SQLiteHandler db = new SQLiteHandler(ActivityMain.this);
                        db.deleteUsers();
                        Intent intent5 = new Intent(ActivityMain.this, ActivityLogin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent5);
                        ActivityMain.this.finishAffinity();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_likedlist:
                        Intent intent1 = new Intent(ActivityMain.this,ActivityLiked.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sharedlist:
                        Intent intent2 = new Intent(ActivityMain.this, ActivitySharedVideos.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_details:
                        Intent intent3 = new Intent(ActivityMain.this, ActivityDetails.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        break;



                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.user_profile_email);
        TextView tv_name = (TextView)header.findViewById(R.id.user_profile_name);

        UserDetailsModel userDetailsModel = new UserDetailsModel(this);
        tv_name.setText(userDetailsModel.getUSER_NAME());
        tv_email.setText(userDetailsModel.getEMAIL());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void checkLoginStatus(){
        //if not logged in start ActivityLogin
        if (!session.isLoggedIn()) {
            // Launching the login activity
            Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    CategoriesFragment tb3 = new CategoriesFragment();
                    return tb3;
                case 1:
                    AreasFragment tb1 = new AreasFragment();
                    return tb1;
//                case 2:
//                    FilterFragment tb2 = new FilterFragment();
//                    return tb2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CATEGORIES";
                case 1:
                    return "AREAS";
//                case 2:
//                    return "FILTER";
            }
            return null;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(ActivityMain.this, SearchActivity.class));
            return true;
        }
        if (id == R.id.action_details) {
            startActivity(new Intent(ActivityMain.this, ActivityDetails.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

}
