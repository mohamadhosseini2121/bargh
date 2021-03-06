package com.example.bargh.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.example.bargh.R;
import com.example.bargh.adapter.MainPagerAdapter;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.User;
import com.example.bargh.view.activity.MainActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private final String TAG = HomeFragment.class.getSimpleName();

    // bottom sheet items
    @BindView(R.id.bottom_sheet_item_view_profile)
    LinearLayout profileBottomSheetView;
    @BindView(R.id.bottom_sheet_item_view_contact_us)
    LinearLayout contactUsBottomSheetView;
    @BindView(R.id.bottom_sheet_item_view_about_us)
    LinearLayout aboutUsBottomSheetView;
    @BindView(R.id.bottom_sheet_item_view_logout)
    LinearLayout logoutBottomSheetView;

    @BindView(R.id.viewPager_main_ac)
    ViewPager2 viewPager2;
    @BindView(R.id.tabLayout_main_ac)
    TabLayout tabLayout;
    @BindView(R.id.bottom_app_bar_main_ac)
    BottomAppBar bottomAppBar;
    @BindView(R.id.bottom_sheet_main_ac)
    LinearLayout bottomSheetLayout;
    @BindView(R.id.fab_main_ac)
    FloatingActionButton fab;


    private BottomSheetBehavior<View> bottomSheetBehavior;
    public static User user = null;
    public static boolean userIsAdmin = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        hideKeyboard(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        AppDatabase database = AppDatabase.getInstance(requireContext());
        user = database.userDao().getFirst();
        if (user.getUserType() == User.USER_TYPE_ADMIN)
            userIsAdmin = true;

        init();

    }

    public void init() {

        initBottomSheet();
        initBottomAppBar();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager(), getLifecycle());
        if (userIsAdmin) {
            pagerAdapter.addFragment(new ReviewRequestsFragment());
            pagerAdapter.addFragment(new ProductsFragment());
            pagerAdapter.addFragment(new AdminServicesFragment());

        } else {
            pagerAdapter.addFragment(new ServicesFragment());
            pagerAdapter.addFragment(new ProductsFragment());
        }

        viewPager2.setAdapter(pagerAdapter);
        initTabLayout(viewPager2, pagerAdapter);

    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    private void initBottomAppBar() {
        //set bottom bar to Action bar as it is similar like Toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(bottomAppBar);

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

    }

    private void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        profileBottomSheetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_userProfileFragment);
            }
        });

        contactUsBottomSheetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_contactUsFragment);
            }
        });

        aboutUsBottomSheetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_aboutUsFragment);
            }
        });

        logoutBottomSheetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase.getInstance(requireContext()).userDao().deleteAll();
                AppDatabase.getInstance(requireContext()).userRepairRequestDao().wipeTable();
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_userProfileFragment);
            }
        });
    }

    public void initTabLayout(ViewPager2 viewPager2, MainPagerAdapter pagerAdapter) {

        if (userIsAdmin) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if (tab.getPosition() == 0) {
                        fab.hide();

                    } else {
                        fab.show();
                        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                int tabIconColor = ContextCompat.getColor(requireContext(), R.color.colorWhite);
                switch (position) {
                    case 0:
                        tab.setText("درخواست ها");
                        tab.setIcon(R.drawable.ic_pending_actions);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        tab.setText("محصولات");
                        tab.setIcon(R.drawable.ic_products);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        tab.setText("خدمات");
                        tab.setIcon(R.drawable.ic_services);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        break;

                }
            });
            tabLayoutMediator.attach();

        } else {

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if (tab.getPosition() == 0) {
                        //in service tab user need floating action button to give request
                        fab.show();
                        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

                    } else if (tab.getPosition() == 1) {
                        //in product tab for clients we don't need floating action button so hide it
                        // only show fab to admin
                        fab.hide();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    //service tab
                    if (tab.getPosition() == 0) {
                        ServicesFragment servicesFragment = (ServicesFragment) (pagerAdapter.getFragment(0));
                        if (servicesFragment.isActionMode()) {
                            servicesFragment.setActionMode(false);
                        }
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                int tabIconColor = ContextCompat.getColor(requireContext(), R.color.colorWhite);
                switch (position) {

                    case 0:
                        tab.setText("خدمات");
                        tab.setIcon(R.drawable.ic_services);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        tab.setText("محصولات");
                        tab.setIcon(R.drawable.ic_products);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        break;

                }
            });
            tabLayoutMediator.attach();
        }
    }

}
