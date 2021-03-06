package com.example.bargh.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.bargh.ApiService;
import com.example.bargh.JsonParser;
import com.example.bargh.R;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.User;
import com.example.bargh.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A login screen that offers login via mobile number/password.
 */
public class LoginFragment extends Fragment {

    private final String TAG = "LoginActivity";

    @BindView(R.id.et_mobile_number_login)
    EditText mobileNumberView;
    @BindView(R.id.et_password_login)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.btn_sign_in_login)
    Button mSignInButton;
    @BindView(R.id.btn_sign_up_login)
    Button mSignUpButton;

    private AppDatabase database;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        database = AppDatabase.getInstance(requireContext());
        List<User> users = database.userDao().getAll();
        if(!users.isEmpty()){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        }

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mSignInButton.setOnClickListener((View v) -> {
            hideKeyboard();
            attemptLogin();

        });

        mSignUpButton.setOnClickListener((View v) -> {
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);

        });

    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mobileNumberView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String mobileNumber = mobileNumberView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        }else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mobileNumber)) {
            mobileNumberView.setError(getString(R.string.error_field_required));
            focusView = mobileNumberView;
            cancel = true;

        } else if (!isMobileNumberValid(mobileNumber)) {
            mobileNumberView.setError(getString(R.string.error_invalid_mobile_number));
            focusView = mobileNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            StartLoginTask (mobileNumber, password);

        }
    }

    private boolean isMobileNumberValid(String mobileNumber) {
        return mobileNumber.startsWith("09") && mobileNumber.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    private void StartLoginTask(String mobileNumber, String password) {

        ApiService apiService = ApiService.getInstance(requireContext());
        apiService.UserLoginTask(mobileNumber, password, new ApiService.OnLoginResponseReceived() {
            @Override
            public void onReceived(String rsp) {
                showProgress(false);
                if (rsp.equals("200"))
                {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));

                }else if (rsp.equals("300")){

                    mobileNumberView.setError(getString(R.string.error_not_a_user));
                }else {
                    User user = null;

                    try {
                        JSONObject jsonObj = new JSONObject(rsp);
                        user = JsonParser.parsLoginJsonObject(jsonObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    database.userDao().insert(user);

                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);

                }
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = requireActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(requireActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

