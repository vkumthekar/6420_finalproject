package edu.mga.vkumthekar.rest_6420a5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AddBranch extends AppCompatActivity {
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void addBranch(View v) {

        final String branchName = getTextEditView(R.id.b_name).toString();
        final String address = getTextEditView(R.id.b_address).toString();
        final String city = getTextEditView(R.id.b_city).toString();
        final String state = getTextEditView(R.id.b_state).toString();
        final String zip = getTextEditView(R.id.b_zip).toString();
        final String phone = getTextEditView(R.id.b_phone).toString();

        String url = MainActivity.BASE_URL + "branches/";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BranchAdd", "Branch added.");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("branchName", branchName);
                    jsonBody.put("address", address);
                    jsonBody.put("city", city);
                    jsonBody.put("state", state);
                    jsonBody.put("zip", zip);
                    jsonBody.put("phone", phone);
                    final String mRequestBody = jsonBody.toString();
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (Exception uee) {
                    return null;
                }
            }
        };

        requestQueue.add(MyStringRequest);
        Intent branchesView = new Intent(AddBranch.this, MainActivity.class);
        startActivity(branchesView);
    }

    private CharSequence getTextEditView(int key) {
        TextView textView = findViewById(key);
        return textView.getText();
    }

    public void getInventory(View v) {
        Intent inventoryView = new Intent(AddBranch.this, ListInventory.class);
        startActivity(inventoryView);
    }

    public void getBranches(View v) {
        Intent branchView = new Intent(AddBranch.this, MainActivity.class);
        startActivity(branchView);
    }
}
