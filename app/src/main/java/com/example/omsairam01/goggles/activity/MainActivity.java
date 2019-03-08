package com.example.omsairam01.goggles.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.fragment.Debrecy;
import com.example.omsairam01.goggles.fragment.Debtor;
import com.example.omsairam01.goggles.fragment.Entery;
import com.example.omsairam01.goggles.fragment.Requirment;
import com.example.omsairam01.goggles.fragment.Requirmentrecy;
import com.example.omsairam01.goggles.fragment.Search;
import com.example.omsairam01.goggles.model.ViewData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    PageAdapter pageAdapter;
    ImageView search_img;
    LinearLayout ll_search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.simpleTabLayout);
        viewPager=findViewById(R.id.viewPager);
        ll_search= findViewById(R.id.ll_search_toobar);
        toolbar=findViewById(R.id.toolbar);
        search_img=findViewById(R.id.search_img);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
         pageAdapter=new PageAdapter(getSupportFragmentManager());
         pageAdapter.addFragment(new Entery(),"CREATOR");
         pageAdapter.addFragment(new Search(),"CR DATA");
         pageAdapter.addFragment(new Debtor(),"DEBTOR");
         pageAdapter.addFragment(new Debrecy(),"DR DATA");
         pageAdapter.addFragment(new Requirment(),"Requirment");
         pageAdapter.addFragment(new Requirmentrecy(),"RQ DATA");

         viewPager.setAdapter(pageAdapter);
         ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            //declare key
            Boolean first = true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0){
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //do what need
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(myOnPageChangeListener);
    }

    public class PageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public PageAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

           return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }


}
