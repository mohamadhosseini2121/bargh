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
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.bargh.R;
import com.example.bargh.adapter.MainPagerAdapter;
import com.example.bargh.adapter.RequestedServicesAdapter;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class HomeFragment extends Fragment implements RequestedServicesAdapter.OnRequestedServicesLongClick {

    private final String TAG = "MainActivity";

    @BindView(R.id.item_view_profile_bottom_sheet)
    LinearLayout profileItemView;
    @BindView(R.id.img_delete_main_ac)
    ImageView deleteImg;
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


    private BottomSheetBehavior bottomSheetBehavior;
    private UserRepairRequest choseToDeleteService = null;
    private View view;

    private MainPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        profileItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_userProfileFragment);
            }
        });
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ServicesFragment)pagerAdapter.getFragment(0)).deleteRequestedService(choseToDeleteService);

            }
        });

    }

    public void init () {

        setUpBottomAppBar();

        //click event over FAB
        fab.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_serviceRequestFragment));

        pagerAdapter = new MainPagerAdapter(getChildFragmentManager(), getLifecycle(),this);
        viewPager2.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    fab.show();
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

                }else if(tab.getPosition() == 1){
                    fab.hide();
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
                    tab.setText("خدمات");
                    tab.setIcon(R.drawable.ic_services);
                    Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    break;
                case 1:
                    tab.setText("محصولات");
                    tab.setIcon(R.drawable.ic_products);
                    Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    break;
                case 2:
                    tab.setText("خریدها");
                    tab.setIcon(R.drawable.ic_purchases);
                    break;
            }

        });

        tabLayoutMediator.attach();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * set up Bottom Bar
     */
    private void setUpBottomAppBar() {

        //set bottom bar to Action bar as it is similar like Toolbar
        ((AppCompatActivity)requireActivity()).setSupportActionBar(bottomAppBar);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fab.show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { fab.hide(); }
        });

    }

    public void alignFabCenter () {
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        }
    }

    public void alignFabEnd () {
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        }
    }


    @Override
    public void onRequestedServiceLongClick(UserRepairRequest userRepairRequest) {
        choseToDeleteService = userRepairRequest;
        alignFabEnd();
        deleteImg.setVisibility(View.VISIBLE);
    }

}
