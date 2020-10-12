package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bargh.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminFragment extends Fragment {

    @BindView(R.id.viewPager_admin_frag)
    ViewPager2 viewPager2;
    @BindView(R.id.tabLayout_admin_frag)
    TabLayout tabLayout;
    @BindView(R.id.bottom_app_bar_admin_frag)
    BottomAppBar bottomAppBar;
    @BindView(R.id.fab_admin_frag)
    FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    public void init () {

        initBottomAppBar();
        initViewPager();

    }
}