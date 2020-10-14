package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.RequestedServicesAdapter;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicesFragment extends Fragment implements RequestedServicesAdapter.OnRequestedServicesListener {

    private final static String TAG = "ServiceFragment: ";
    @BindView(R.id.recycler_view_services)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty_data)
    TextView emptyTv;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private BottomSheetBehavior bottomSheetBehavior;


    private List<UserRepairRequest> userRepairRequests = new ArrayList<>();
    private RequestedServicesAdapter rsAdapter;
    private ApiService apiService;
    private View view;


    private boolean isActionMode = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }


    @Override
    public void onResume() {
        getRepairRequestsFromServer();
        isRecyclerViewEmpty();
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getRepairRequestsFromServer();
        isRecyclerViewEmpty();
        super.onViewCreated(view, savedInstanceState);
    }

    public void getRepairRequestsFromServer () {

        apiService.getUserServices(HomeFragment.user.getMobileNumber(), new ApiService.OnGettingUserRequests() {
            @Override
            public void onReceived(List<UserRepairRequest> rsp) {
                if (rsp != null && !rsp.isEmpty()) {
                    AppDatabase database = AppDatabase.getInstance(requireContext());
                    database.userRepairRequestDao().wipeTable();
                    database.userRepairRequestDao().insertAll(rsp);
                    userRepairRequests.clear();
                    userRepairRequests.addAll(rsp);
                    Collections.reverse(userRepairRequests);
                    rsAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(rsAdapter.getItemCount() - 1);
                }
            }
        });
    }

    public void initViews () {

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        apiService = ApiService.getInstance(requireContext());
        HomeFragment homeFragment = ((HomeFragment) ServicesFragment.this.getParentFragment());
        if (homeFragment != null) {
            rsAdapter = new RequestedServicesAdapter(requireContext(), userRepairRequests, this);
            fab = homeFragment.fab;
            bottomAppBar = homeFragment.bottomAppBar;
            bottomSheetBehavior = BottomSheetBehavior.from(homeFragment.bottomSheetLayout);
        }
        recyclerView.setAdapter(rsAdapter);

        fab.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_serviceRequestFragment));

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_bottom_appbar_delete:
                        rsAdapter.removeItems(rsAdapter.getSelectedItems());
                        isRecyclerViewEmpty();

                        setActionMode(false);
                        break;

                    case R.id.menu_bottom_appbar_select_all:
                        if (rsAdapter.getSelectedItemCount() == userRepairRequests.size()) {
                            rsAdapter.clearSelection();
                        } else {
                            rsAdapter.toggleSelectAll();
                        }
                        break;
                }
                return true;
            }
        });

    }


    public void deleteRequestedServices() {

        // TODO: 9/29/2020  show progress bar then delete items (deleting items may take time)
        AppDatabase database = AppDatabase.getInstance(requireContext());

        for (int i = 0; i < userRepairRequests.size(); i++) {

            UserRepairRequest userRepairRequest = userRepairRequests.get(i);
            if (userRepairRequest.getState() == UserRepairRequest.STATE_PENDING) {
                apiService.deleteUserService(userRepairRequest, new ApiService.OnDeletingRequestedService() {
                    @Override
                    public void onDelete(int rsp) {
                        Log.d(TAG, "deleteRequestedServices: onDelete: rsp: " + rsp);
                    }
                });
            }

            database.userRepairRequestDao().delete(userRepairRequest);
            userRepairRequests.remove(userRepairRequest);
            rsAdapter.notifyItemRemoved(i);


        }
    }


    public void setActionMode(boolean isActionMode) {

        if (isActionMode && !this.isActionMode) {

            changeMenuForActionMode(true);

        } else if (!isActionMode) {

            changeMenuForActionMode(false);
            rsAdapter.setActionMode(false);
            rsAdapter.clearSelection();
        }
        this.isActionMode = isActionMode;
    }

    public boolean isActionMode() {
        return isActionMode;
    }

    private void changeMenuForActionMode (boolean actionMode) {

        if (actionMode) {
            alignFabEnd();
            fab.hide();
            bottomAppBar.setHideOnScroll(false);
            bottomAppBar.replaceMenu(R.menu.bottom_appbar_delete_menu);
            bottomAppBar.setNavigationIcon(R.drawable.ic_arrow_back);
            bottomAppBar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent));
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActionMode(false);
                }
            });

        }else {
            fab.show();
            alignFabCenter();
            bottomAppBar.replaceMenu(R.menu.bottom_appbar_menu);
            bottomAppBar.setHideOnScroll(true);
            bottomAppBar.setNavigationIcon(R.drawable.ic_nav_menu);
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }
    }

    public void alignFabCenter() {
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        }
    }

    public void alignFabEnd() {
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        }
    }

    /**
     * if recycler view is empty, this method hides recycler view and
     * shows empty text view (text view with a message that shows recycler view is empty)
     */
    private void isRecyclerViewEmpty() {

        if (userRepairRequests.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActionModeChange(boolean isActionMode) {
        setActionMode(isActionMode);
    }
}
