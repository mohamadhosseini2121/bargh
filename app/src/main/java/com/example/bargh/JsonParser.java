package com.example.bargh;

import android.util.Log;

import com.example.bargh.datamodel.Service;
import com.example.bargh.datamodel.Product;
import com.example.bargh.db.entity.UserRepairRequest;
import com.example.bargh.db.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {


    private static final String TAG = "JsonParser: ";

    public static List<Service> parsServicesJsonArray (JSONArray jsonArray) {

        List<Service> services = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Service service = new Service();
                service.setName(jsonObject.getString("name"));
                service.setInfo(jsonObject.getString("info"));

                services.add(service);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "parsServicesJsonArray: service 0 name:" + services.get(0).getName());
        return services;
    }

    public static List<UserRepairRequest> parsUserServicesJsonArray (JSONArray jsonArray){

        List<UserRepairRequest> userRepairRequests = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UserRepairRequest userRepairRequest = new UserRepairRequest();
                userRepairRequest.setType(jsonObject.getString("type"));
                userRepairRequest.setInfo(jsonObject.getString("info"));
                userRepairRequest.setDate(jsonObject.getString("date"));
                userRepairRequest.setUser(jsonObject.getString("user"));
                userRepairRequest.setState(jsonObject.getInt("state"));
                userRepairRequest.setTimestamp(String.valueOf(jsonObject.getInt("timestamp")));
                userRepairRequest.setLat(jsonObject.getDouble("lat"));
                userRepairRequest.setLng(jsonObject.getDouble("lng"));

                userRepairRequests.add(userRepairRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return userRepairRequests;

    }

    public static User parsLoginJsonObject(JSONObject jsonObject) {

        User user = null;

        try {
            user = new User(jsonObject.getString("firstname"),
                            jsonObject.getString("lastname"),
                            jsonObject.getString("email"),
                            jsonObject.getString("mobilenumber"),
                            Integer.parseInt(jsonObject.getString("usertype")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }


    public static List<Product> parsProductsJsonArray(JSONArray jsonArray) {

        List<Product> products = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product();
                product.setName(jsonObject.getString("name"));
                product.setPrice(jsonObject.getInt("price"));
                product.setInfo(jsonObject.getString("info"));
                product.setProductImageUrl(jsonObject.getString("pic"));

                products.add(product);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return products;
    }


}
