package com.example.bargh;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bargh.datamodel.Service;
import com.example.bargh.datamodel.Product;
import com.example.bargh.db.entity.User;
import com.example.bargh.db.entity.UserRepairRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;

public class ApiService {

    private final String TAG = "ApiService: ";
    private static ApiService instance;
    private Context context;
    private static final String server = "http://192.168.1.9";
    private final String showProduct_url = server + "/bargh/ShowProducts.php";
    private final String login_url = server + "/bargh/Login.php";
    private final String register_url = server + "/bargh/register.php";
    private final String getUserRequests_url = server + "/bargh/getUserRequests.php";
    private final String getAllServices_url = server + "/bargh/getAllServices.php";
    private final String removeUserRequestedService_url = server + "/bargh/removeUserService.php";
    private final String sendUserRepairRequest_url = server + "/bargh/addUserRepairRequest.php";
    private final String getAllUsersRequests_url = server + "/bargh/getAllUsersRequests.php";
    private final String addService_url = server + "/bargh/addService.php";
    private final String changeUserRepairRequestState_url = server + "/bargh/updateUserRepairRequest.php";
    private final String updateUserData_url = server + "/bargh/UpdateUserData.php";
    private final String uploadUserPic_url = server + "/bargh/UploadUserPic.php";



    private ApiService(Context context) {
        this.context = context;
    }

    public static ApiService getInstance(Context context) {
        if (instance == null)
            instance = new ApiService(context);
        return instance;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void uploadUserPic(Bitmap bitmap, String userMobileNumber) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUserPic_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(context, obj.getString("content"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", userMobileNumber);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }


    public void updateUserData(User newUser, OnUserDataUpdate onUserDataUpdate) {

        StringRequest request = new StringRequest(Request.Method.POST, updateUserData_url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    onUserDataUpdate.onResult(JsonParser.parsResult(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: addService: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mFirstName", newUser.getFirstName());
                params.put("mLastName", newUser.getLastName());
                params.put("mEmail", newUser.getEmail());
                params.put("mMobileNumber", newUser.getMobileNumber());
                params.put("mUserType", String.valueOf(newUser.getUserType()));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);
    }

    public void changeUserRepairRequestState(UserRepairRequest userRequest, OnChangingUserRepairRequest onChangingUserRepairRequest) {

        StringRequest request = new StringRequest(Request.Method.POST,
                changeUserRepairRequestState_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            onChangingUserRepairRequest.onStateChangeResult(JsonParser.parsResult(jsonObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mState", String.valueOf(userRequest.getState()));
                params.put("mUser", userRequest.getUser());
                params.put("mTimestamp", userRequest.getTimestamp());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);

    }

    public void addService(Service service, OnAddingService onAddingService) {

        StringRequest request = new StringRequest(Request.Method.POST, addService_url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    onAddingService.onResult(JsonParser.parsResult(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: addService: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mName", service.getName());
                params.put("mInfo", service.getInfo());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);

    }

    public void getAllUsersRequests(OnGettingAllUsersRequests onGettingAllUsersRequests) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getAllUsersRequests_url, null,
                response -> {
                    onGettingAllUsersRequests.onReceived(JsonParser.parsUsersRequestsJsonArray(response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString()));

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);
    }

    public void addUserRepairRequest(UserRepairRequest userRepairRequest) {

        StringRequest request = new StringRequest(Request.Method.POST,
                sendUserRepairRequest_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: sendRepairRequestToServer: " + response);
                    }
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("type", userRepairRequest.getType());
                params.put("info", userRepairRequest.getInfo());
                params.put("user", userRepairRequest.getUser());
                params.put("state", String.valueOf(userRepairRequest.getState()));
                params.put("lat", String.valueOf(userRepairRequest.getLat()));
                params.put("lng", String.valueOf(userRepairRequest.getLng()));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);


    }

    public void getAllServices(OnGettingAllServices onGettingAllServices) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getAllServices_url, null,
                response -> {
                    Log.d(TAG, "getAllServices: response:" + response);
                    onGettingAllServices.onReceived(JsonParser.parsServicesJsonArray(response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString()));

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);

    }

    public void deleteUserService(UserRepairRequest userRepairRequest,
                                  OnDeletingRequestedService onDeletingRequestedService) {

        StringRequest request = new StringRequest(Request.Method.POST,
                removeUserRequestedService_url,
                response -> {
                    onDeletingRequestedService.onDelete(Integer.parseInt(response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mNumber", userRepairRequest.getUser());
                params.put("timestamp", userRepairRequest.getTimestamp());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);


    }

    public void getUserServices(String UserMobileNumber, OnGettingUserRequests onGettingUserRequests) {

        StringRequest request = new StringRequest(Request.Method.POST, getUserRequests_url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response: " + response);
                JSONArray jsonArray = null;

                if (response.equals("error 200")) {
                    onGettingUserRequests.onReceived(null);
                } else {
                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onGettingUserRequests.onReceived(JsonParser.parsUsersRequestsJsonArray(jsonArray));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mNumber", UserMobileNumber);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);
    }

    public void getProducts(OnProductsReceived onProductsReceived) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                showProduct_url, null,
                response -> {
                    onProductsReceived.onReceived(JsonParser.parsProductsJsonArray((JSONArray) response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString()));

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);

    }

    public void UserLoginTask(String mNumber, String password,
                              OnLoginResponseReceived onLoginResponseReceived) {


        StringRequest request = new StringRequest(Request.Method.POST,
                login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onLoginResponseReceived.onReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mNumber", mNumber);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

    }

    public void UserRegisterTask(String firstName, String lastName, String mNumber,
                                 @Nullable String email, String password, int userType,
                                 OnRegisterResponseReceived onRegisterResponseReceived) {


        StringRequest request = new StringRequest(Request.Method.POST,
                register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onRegisterResponseReceived.onReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("mNumber", mNumber);
                params.put("email", email);
                params.put("password", password);
                params.put("userType", String.valueOf(userType));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

    }

    public interface OnUserDataUpdate {
        void onResult(HashMap<String, String> result);
    }

    public interface OnAddingService {
        void onResult(HashMap<String, String> result);
    }

    public interface OnChangingUserRepairRequest {
        void onStateChangeResult(HashMap<String, String> result);
    }

    public interface OnGettingAllUsersRequests {
        void onReceived(List<UserRepairRequest> requests);
    }

    public interface OnDeletingRequestedService {
        void onDelete(int rsp);
    }

    public interface OnGettingAllServices {
        void onReceived(List<Service> allServices);
    }

    public interface OnGettingUserRequests {
        void onReceived(List<UserRepairRequest> rsp);
    }

    public interface OnRegisterResponseReceived {
        void onReceived(String rsp);
    }

    public interface OnLoginResponseReceived {
        void onReceived(String rsp);
    }

    public interface OnProductsReceived {
        void onReceived(List<Product> products);
    }
}
