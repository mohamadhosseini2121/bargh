package com.example.bargh.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterFragment extends Fragment {


    private final String TAG = "RegisterActivity";
    @BindView(R.id.et_name_register)
    TextInputLayout mNameEt;
    @BindView(R.id.et_lastname_register)
    TextInputLayout mLastNameEt;
    @BindView(R.id.et_mobilenumber_register)
    TextInputLayout mMobileNumberEt;
    @BindView(R.id.et_email_register)
    TextInputLayout mEmailEt;
    @BindView(R.id.et_password_register)
    TextInputLayout mPasswordEt;
    @BindView(R.id.et_repassword_register)
    TextInputLayout mRePasswordEt;
    @BindView(R.id.btn_sign_up)
    Button mSignUpBtn;
    @BindView(R.id.register_progress)
    View mProgressView;
    @BindView(R.id.register_form)
    View mRegisterFormView;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mSignUpBtn.setOnClickListener((View v) -> {
            attemptRegister();

        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {

        // Reset errors.
        mNameEt.setError(null);
        mLastNameEt.setError(null);
        mMobileNumberEt.setError(null);
        mEmailEt.setError(null);
        mPasswordEt.setError(null);
        mRePasswordEt.setError(null);

        // Store values at the time of the login attempt.
        String name = Objects.requireNonNull(mNameEt.getEditText()).getText().toString();
        String lastName = Objects.requireNonNull(mLastNameEt.getEditText()).getText().toString();
        String mobileNumber = Objects.requireNonNull(mMobileNumberEt.getEditText()).getText().toString();
        String email = Objects.requireNonNull(mEmailEt.getEditText()).getText().toString();
        String password = Objects.requireNonNull(mPasswordEt.getEditText()).getText().toString();
        String rePassword = Objects.requireNonNull(mRePasswordEt.getEditText()).getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for valid RePassword, if user entered one.
        if (TextUtils.isEmpty(rePassword)) {
            mRePasswordEt.setError(getString(R.string.error_field_required));
            focusView = mRePasswordEt;
            cancel = true;

        } else if (!isRePasswordValid(password, rePassword)) {
            mRePasswordEt.setError(getString(R.string.error_repassword));
            focusView = mRePasswordEt;
            cancel = true;
        }

        // Check for valid password, if user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordEt.setError(getString(R.string.error_field_required));
            focusView = mPasswordEt;
            cancel = true;

        } else if (!isPasswordValid(password)) {
            mPasswordEt.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEt;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailEt.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEt;
            cancel = true;
        }

        // Check if user entered MobileNumber.
        if (TextUtils.isEmpty(mobileNumber)) {
            mMobileNumberEt.setError(getString(R.string.error_field_required));
            focusView = mMobileNumberEt;
            cancel = true;
        } else if (!isMobileNumberValid(mobileNumber)) {
            mMobileNumberEt.setError(getString(R.string.error_invalid_mobile_number));
            focusView = mMobileNumberEt;
            cancel = true;
        }

        // Check if user entered LastName.
        if (TextUtils.isEmpty(lastName)) {
            mLastNameEt.setError(getString(R.string.error_field_required));
            focusView = mLastNameEt;
            cancel = true;

        }

        // Check if user entered Name.
        if (TextUtils.isEmpty(name)) {
            mNameEt.setError(getString(R.string.error_field_required));
            focusView = mNameEt;
            cancel = true;

        }

        if (cancel) {
            // There was an error; don't attempt Register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Register attempt.
            showProgress(true);
            //mRegTask = new UserRegisterTask (name, lastName, mobileNumber, email, password, this);
            //mRegTask.execute((Void) null);

            ApiService apiService = ApiService.getInstance(requireContext());
            apiService.UserRegisterTask(name, lastName, mobileNumber, email, password, 0,
                    new ApiService.OnRegisterResponseReceived() {
                        @Override
                        public void onReceived(String rsp) {
                            if (rsp.equals("100")) {
                                showProgress(false);
                                mMobileNumberEt.setError("already someone signed up using this mobile number");
                            } else {
                                showProgress(false);
                                Snackbar.make(view , "ثبت نام با موفقیت انجام شد", Snackbar.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                            }
                        }
                    });
        }
    }

    private boolean isMobileNumberValid(String mobileNumber) {

        return mobileNumber.contains("9") && mobileNumber.length() == 11;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private boolean isRePasswordValid(String password, String rePassword) {
        return password.equals(rePassword);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
