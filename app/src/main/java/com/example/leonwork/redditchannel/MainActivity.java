package com.example.leonwork.redditchannel;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WebViewFragment.OnHeadlineSelectedListener, ChannelAdapter.OnTopicSelection{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ChannelFragment channelFragment = null;
    private FavoritesFragment favoritesFragment = null;
    private WebViewFragment webViewFragment;
    private ChannelAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Channel> topicsList = new ArrayList<>();
    public static TabLayout tabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnTopicSelected(Channel topic) {
        webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("topic", topic);
        webViewFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content,webViewFragment, "WebViewFragment");
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void changeInFavorite() {
        favoritesFragment.changeFavorite();
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.channel_fragment, container, false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    channelFragment = new ChannelFragment();
                    return channelFragment;
                case 1:
                    favoritesFragment = new FavoritesFragment();
                    return favoritesFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Android";
                case 1:
                    return "Favorites";
            }
            return null;
        }
    }
}
