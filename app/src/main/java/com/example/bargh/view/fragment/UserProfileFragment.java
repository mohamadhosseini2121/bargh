package com.example.bargh.view.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.RealPathUtil;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.User;
import com.example.bargh.view.activity.MainActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Objects;

public class UserProfileFragment extends Fragment {

    private boolean readStoragePermissionIsGranted = false;

    @BindView(R.id.toolbar_user_profile)
    Toolbar toolbar;
    @BindView(R.id.img_set_avatar_user_profile_frag)
    ShapeableImageView changeBtn;
    @BindView(R.id.img_avatar_user_profile_frag)
    ShapeableImageView avatarSiv;
    @BindView(R.id.tv_mobile_number_user_profile_frag)
    TextView mobileNumberTv;
    @BindView(R.id.tv_name_user_profile_frag)
    TextView nameTv;
    @BindView(R.id.tiet_first_name_user_profile)
    TextInputEditText firstNameEt;
    @BindView(R.id.tiet_last_name_user_profile)
    TextInputEditText lastNameEt;
    @BindView(R.id.tiet_email_user_profile)
    TextInputEditText emailEt;
    @BindView(R.id.btn_update_user_profile)
    Button updateBtn;
    //@BindView(R.id.tiet_mobile_number_user_profile)
    //TextInputEditText mobileNumberEt;

    private View mLayout;
    private Uri avatarImageUri = null;
    private Bitmap tempLoadedUserPic = null;
    private User user = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, mLayout);

        user = AppDatabase.getInstance(requireContext()).userDao().getFirst();
        setupToolbar();
        initViews();
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectImageDialog();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
                updateServer();
                user = createNewUserFromViews();
                initViews();
            }
        });
        return mLayout;
    }

    private void updateDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        database.userDao().delete(user);
        database.userDao().insert(createNewUserFromViews());
    }

    private void updateServer() {
        ApiService apiService = ApiService.getInstance(requireContext());

        /*apiService.updateUserData(createNewUserFromViews(), new ApiService.OnUserDataUpdate() {
            @Override
            public void onResult(HashMap<String, String> result) {
                if(Objects.requireNonNull(result.get("code")).equals("0")){
                    Snackbar.make(mLayout, "مشکلی در هنگام ارسال اطلاعات به سرور پیش آمده است", Snackbar.LENGTH_SHORT).show();
                }else
                    Snackbar.make(mLayout, "بروزرسانی اطلاعات با موفقیت انجام شد", Snackbar.LENGTH_SHORT).show();
            }
        });*/

        if (tempLoadedUserPic != null){
            apiService.uploadUserPic(tempLoadedUserPic, user.getMobileNumber());
        }
    }

    private User createNewUserFromViews () {
        if (user != null) {

            return new User(String.valueOf(firstNameEt.getText()),
                    String.valueOf(lastNameEt.getText()),
                    String.valueOf(emailEt.getText()),
                    user.getMobileNumber(),
                    user.getUserType());
        }else
            return null;
    }

    private void initViews () {
        if (user != null) {
            nameTv.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            mobileNumberTv.setText(user.getMobileNumber());
            firstNameEt.setText(user.getFirstName());
            lastNameEt.setText(user.getLastName());
            emailEt.setText(user.getEmail());
        }
    }

    private void setupToolbar () {

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

    }

    private void showSelectImageDialog() {
        final CharSequence[] options = {"دوربین", "انتخاب از بین عکس های موجود"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        builder.setTitle("انتخاب عکس حساب کاربری");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    mTakePicture.launch(avatarImageUri);

                } else if (item == 1) {
                    if (readStoragePermissionIsGranted) {
                        mGetContent.launch("image/*");
                    } else {
                        requestReadStoragePermission();
                    }
                }

            }
        });
        builder.show();
    }

    private void requestReadStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Snackbar.make(mLayout, "برای انجام درخواست شما به دسترسی حافظه نیاز است",
                    Snackbar.LENGTH_INDEFINITE).setAction("باشه", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }).show();

        } else {
            requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (readStoragePermissionIsGranted && uri != null){
                        String picturePath = RealPathUtil.getRealPath(requireActivity(), uri);
                        tempLoadedUserPic = BitmapFactory.decodeFile(picturePath);
                        avatarSiv.setImageBitmap(tempLoadedUserPic);}
                }
            });

    ActivityResultLauncher<Uri> mTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                if (avatarImageUri != null){
                    String picturePath = RealPathUtil.getRealPath(requireActivity(), avatarImageUri);
                    avatarSiv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
            }
        }
    });

    ActivityResultLauncher<String> requestStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    readStoragePermissionIsGranted = true;
                    mGetContent.launch("image/*");
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    readStoragePermissionIsGranted = false;
                    Snackbar.make(mLayout, "برای دریافت عکس داخل گوشی شما به دسترسی مورد نظر نیاز است", Snackbar.LENGTH_SHORT).show();
                }
            });



}