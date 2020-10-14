package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.datamodel.Service;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddServiceFragment extends Fragment {

    private final static String TAG = AddServiceFragment.class.getSimpleName();

    @BindView(R.id.toolbar_add_service)
    Toolbar toolbar;
    @BindView(R.id.tiet_name_add_service)
    TextInputEditText nameEt;
    @BindView(R.id.tiet_info_add_service)
    TextInputEditText infoEt;
    @BindView(R.id.btn_add_service)
    Button addServiceBtn;
    @BindView(R.id.progress_bar_add_service)
    ProgressBar progressBar;

    private ApiService apiService;
    private View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_service, container, false);
        ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    public void init () {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        apiService = ApiService.getInstance(requireContext());
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                sendServiceToServer(new ApiService.OnAddingService() {
                    @Override
                    public void onResult(HashMap<String,String> result) {
                        showProgress(false);
                        String code = result.get("code");
                        String content = result.get("content");
                        if (code != null && content != null) {
                            if (code.equals("0"))
                                Snackbar.make(mView, content, Snackbar.LENGTH_SHORT).show();
                            else{
                                Snackbar.make(mView, content, Snackbar.LENGTH_SHORT).show();
                                clearEditTexts();
                            }
                        }
                    }
                });
            }
        });
    }

    public void sendServiceToServer (ApiService.OnAddingService onAddingService){
        if (apiService == null)
            apiService = ApiService.getInstance(requireContext());

        String name = Objects.requireNonNull(nameEt.getText()).toString().trim();
        String info = Objects.requireNonNull(infoEt.getText()).toString().trim();
        Log.d(TAG, "sendServiceToServer: name: " + name + " info: " + info);
        Service service = new Service(name , info);
        apiService.addService(service, onAddingService);
    }

    public void showProgress(boolean show) {

        if (show){
            addServiceBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }else{
            addServiceBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void clearEditTexts () {
        nameEt.setText("");
        infoEt.setText("");
    }
}