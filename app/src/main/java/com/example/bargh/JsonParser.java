package com.example.bargh;

import android.util.Log;

import com.example.bargh.datamodel.Service;
import com.example.bargh.datamodel.Product;
import com.example.bargh.datamodel.RequestedService;
import com.example.bargh.datamodel.User;

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

    public static List<RequestedService> parsUserServicesJsonArray (JSONArray jsonArray){

        List<RequestedService> requestedServices = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RequestedService requestedService = new RequestedService();
                requestedService.setType(jsonObject.getString("type"));
                requestedService.setInfo(jsonObject.getString("info"));
                requestedService.setDate(jsonObject.getString("date"));
                requestedService.setUser(jsonObject.getString("user"));
                requestedService.setState(jsonObject.getInt("state"));
                requestedService.setLat(jsonObject.getDouble("lat"));
                requestedService.setLng(jsonObject.getDouble("lng"));

                requestedServices.add(requestedService);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "parsUserServicesJsonArray: services: date of first item: " +
                requestedServices.get(0).getDate() + " date of second item: " + requestedServices.get(1).getDate());

        return requestedServices;

    }

    public static User parsLoginJsonObject(JSONObject jsonObject) {

        User user = new User();

        try {
            user.setFirstName(jsonObject.getString("firstname"));
            user.setLastName(jsonObject.getString("lastname"));
            user.setEmail(jsonObject.getString("email"));
            user.setMobileNumber(jsonObject.getString("mobilenumber"));
            user.setUserType(Integer.parseInt(jsonObject.getString("usertype")));

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
