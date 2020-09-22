package com.example.bargh;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bargh.datamodel.Service;
import com.example.bargh.datamodel.Product;
import com.example.bargh.datamodel.RequestedService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;

public class ApiService {

    private final String TAG = "ApiService: ";
    private static ApiService instance;
    private Context context;
    private final String showProduct_url = "http://192.168.1.10/bargh/ShowProducts.php";
    private final String login_url = "http://192.168.1.10/bargh/Login.php";
    private final String register_url = "http://192.168.1.10/bargh/register.php";
    private final String getUserServices_url = "http://192.168.1.10/bargh/getUserServices.php";
    private final String getAllServices_url = "http://192.168.1.10/bargh/getAllServices.php";


    private ApiService (Context context){

        this.context = context;
    }


    public static ApiService getInstance(Context context)
    {
        if (instance == null)
            instance = new ApiService(context);
        return instance;
    }


    public void getAllServices (onGettingAllServices onGettingAllServices) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getAllServices_url, null,
                response -> {
                    Log.d(TAG, "getAllServices: response:" + response);
                    onGettingAllServices.onReceived(JsonParser.parsServicesJsonArray((JSONArray)response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString() ));

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);


    }


    /**
     * this method gets all of services requested by a user specified by UserMobileNumber
     * @param UserMobileNumber
     * @param onGettingUserServices
     */
    public void getUserServices (String UserMobileNumber, OnGettingUserServices onGettingUserServices){

        StringRequest request = new StringRequest(Request.Method.POST, getUserServices_url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response: " + response);
                JSONArray jsonArray = null;

                if (response.equals("error 200")) {
                    onGettingUserServices.onReceived(null);
                }else {
                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onGettingUserServices.onReceived(JsonParser.parsUserServicesJsonArray(jsonArray));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mNumber", UserMobileNumber);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);
    }


    public void getProducts (OnProductsReceived onProductsReceived) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                showProduct_url, null,
                response -> {
                    onProductsReceived.onReceived(JsonParser.parsProductsJsonArray((JSONArray)response));
                },

                error -> Log.e(TAG, "onErrorResponse: " + error.toString() ));

        request.setRetryPolicy(new DefaultRetryPolicy(18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Objects.requireNonNull(context)).add(request);

    }


    public void UserLoginTask (String mNumber, String password,
                                 OnLoginResponseReceived onLoginResponseReceived){


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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mNumber", mNumber);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

    }


    public void UserRegisterTask (String firstName, String lastName, String mNumber,
                                  @Nullable String email, String password, int userType,
                                  OnRegisterResponseReceived onRegisterResponseReceived){


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
        })
        {
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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

    }

    public interface onGettingAllServices {
        void onReceived (List<Service> allServices);
    }

    public interface OnGettingUserServices {
        void onReceived (List<RequestedService> rsp);
    }

    public interface OnRegisterResponseReceived {
        void onReceived (String rsp);
    }

    public interface OnLoginResponseReceived {
        void onReceived (String rsp);
    }

    public interface OnProductsReceived {
        void onReceived (List<Product> products);
    }
}
