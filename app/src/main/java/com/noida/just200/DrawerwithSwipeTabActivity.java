package com.noida.just200;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DrawerwithSwipeTabActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    LinearLayout mDrawerPane ;
    TextView textViewtitle;
    ImageView iv_logout;
    SessionManager sessionManager;


    TabLayout.Tab tab1, tab2, tab3;
    // private Toolbar toolbar;



    public TabLayout tabLayout;
    private ViewPager viewPager;
    private static int[] tabIcons = {
            R.drawable.sent,
            R.drawable.connect, R.drawable.settings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawerwithswipetab);
         sessionManager = new SessionManager(this);
        /**
         *Setup the DrawerLayout and NavigationView
         */

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

       tabLayout = (TabLayout) findViewById(R.id.tablayout);

       tabLayout.setupWithViewPager(viewPager);
        // setUpTabIcons();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.iv_tab);


            tabTextView.setText(tab.getText());
           // imageView.setImageResource(tabIcons[i]);
            tab.setCustomView(relativeLayout);

        }

         /*    mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
             mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            TextView  tv_home_profile=(TextView)findViewById(R.id.tv_home_profile);

        tv_home_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        });*/
            // mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
             textViewtitle =(TextView)findViewById(R.id.toolbar_title) ;

            iv_logout =(ImageView)findViewById(R.id.iv_logout);

            iv_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutdiaolog();

                }
            });

                android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

               // mDrawerLayout.setDrawerListener(mDrawerToggle);

              //  mDrawerToggle.syncState();

    }


    public void logoutdiaolog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        // set title
        alertDialogBuilder.setTitle("Would you like to logout?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                               sessionManager.logoutUser();
                                //startActivity(new Intent(HomeActivity.this, DefaultScreenActivity.class));



/*
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);*/

                                finish();

                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           int a = requestCode;
            Intent c = data;
        if (requestCode == 1) {

            Uri filePath = data.getData();
            Log.v("image path....", filePath + "");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DashbordFragment(), "Dashboard");
        adapter.addFrag(new RegistrationFragment(), "Registration");
        adapter.addFrag(new UpgradeFragment(), "Upgrade");
        // adapter.
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        //    private BaseFragment mFragmentAtPos2; // Fragment at index 2
        private final FragmentManager mFragmentManager = getSupportFragmentManager();
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            //   mFragmentManager = manager;

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DashbordFragment();
            }
            if (position == 1) {
                return new RegistrationFragment();
            }
            if (position == 2)
                return new UpgradeFragment();
            else
                return new DashbordFragment();

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}