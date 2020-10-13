package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.ReviewRequestsAdapter;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.UserRepairRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewRequestsFragment extends Fragment {

    private List<UserRepairRequest> userRepairRequests = new ArrayList<>();
    private ApiService apiService;
    private ReviewRequestsAdapter rrAdapter;

    @BindView(R.id.recycler_view_review_requests)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty_data_review_requests)
    TextView emptyTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_requests, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    public void init () {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true));
        apiService = ApiService.getInstance(requireContext());
        rrAdapter = new ReviewRequestsAdapter(requireContext(), userRepairRequests);
        recyclerView.setAdapter(rrAdapter);
        getRepairRequestsFromServer();
        isRecyclerViewEmpty();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRepairRequestsFromServer();
        isRecyclerViewEmpty();
    }

    public void getRepairRequestsFromServer () {

        if (apiService == null)
            apiService = ApiService.getInstance(requireContext());

        apiService.getAllUsersRequests(new ApiService.OnGettingAllUsersRequests() {
            @Override
            public void onReceived(List<UserRepairRequest> requests) {
                if (requests != null && !requests.isEmpty()) {
                    userRepairRequests.clear();
                    userRepairRequests.addAll(requests);
                    rrAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(rrAdapter.getItemCount() - 1);
                }
            }
        });
    }

    /**
     * if recycler view is empty, this method hides recycler view and
     * shows empty text view (text view with a message that shows recycler view is empty)
     */
    private void isRecyclerViewEmpty() {

        if (userRepairRequests != null) {
            if (userRepairRequests.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyTv.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyTv.setVisibility(View.GONE);
            }
        }
    }
}