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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayBranchActivity extends AppCompatActivity {
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_display_branch);
        Intent intent = getIntent();
        String branchId = intent.getStringExtra("branchId");
        TextView textView = findViewById(R.id.b_id);
        textView.setText(branchId);
        setBranchForm(branchId);

    }

    public void updateBranch(View v) {
        final String branchId = getTextEditView(R.id.b_id).toString();
        final String branchName = getTextEditView(R.id.b_name).toString();
        final String address = getTextEditView(R.id.b_address).toString();
        final String city = getTextEditView(R.id.b_city).toString();
        final String state = getTextEditView(R.id.b_state).toString();
        final String zip = getTextEditView(R.id.b_zip).toString();
        final String phone = getTextEditView(R.id.b_phone).toString();

        String url = MainActivity.BASE_URL + "branches/" + branchId;

        StringRequest MyStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BranchUpdate", "Branch updated.");
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
                    jsonBody.put("branchId", branchId);
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
        Intent branchesView = new Intent(DisplayBranchActivity.this, MainActivity.class);
        startActivity(branchesView);
    }

    public void deleteBranch(View v) {
        final String branchId = getTextEditView(R.id.b_id).toString();
        String url = MainActivity.BASE_URL + "branches/" + branchId;
        Log.d("BranchDelete", "Branch being deleted" + branchId);
        StringRequest MyStringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BranchDelete", "Branch deleted.");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        });
        requestQueue.add(MyStringRequest);
        Intent branchesView = new Intent(DisplayBranchActivity.this, MainActivity.class);
        startActivity(branchesView);
    }

    private void setBranchForm(String branchId) {
        String url = MainActivity.BASE_URL + "branches/" + branchId;
        Log.d("Get Branch Response: ", url);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject branch) {
                        Log.d("Get Branch Response: ", branch.toString());
                        // Process the JSON
                        try {

                            // Get the current student (json object) data
                            String branchName = branch.getString("branchName");
                            String address = branch.getString("address");
                            String city = branch.getString("city");
                            String state = branch.getString("state");
                            String zip = branch.getString("zip");
                            String phone = branch.getString("phone");

                            setTextEditView(R.id.b_name, branchName);
                            setTextEditView(R.id.b_address, address);
                            setTextEditView(R.id.b_city, city);
                            setTextEditView(R.id.b_state, state);
                            setTextEditView(R.id.b_zip, zip);
                            setTextEditView(R.id.b_phone, phone);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void setTextEditView(int key, String value) {
        TextView textView = findViewById(key);
        textView.setText(value);

    }

    private CharSequence getTextEditView(int key) {
        TextView textView = findViewById(key);
        return textView.getText();
    }

    public void getBranches(View v) {
        Intent branchesView = new Intent(DisplayBranchActivity.this, MainActivity.class);
        startActivity(branchesView);
    }

    public void getInventory(View view) {
        Intent inventoryView = new Intent(DisplayBranchActivity.this, ListInventory.class);
        startActivity(inventoryView);
    }
}
