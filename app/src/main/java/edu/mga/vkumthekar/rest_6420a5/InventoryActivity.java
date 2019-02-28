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

public class InventoryActivity extends AppCompatActivity

{
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_inventory);
        Intent intent = getIntent();
        String inventoryId = intent.getStringExtra("inventoryId");
        TextView textView = findViewById(R.id.i_id);
        textView.setText(inventoryId);
        setInventoryForm(inventoryId);

    }

    public void updateInventory(View v) {
        final String inventoryId = getTextEditView(R.id.i_id).toString();
        final String branchId = getTextEditView(R.id.b_id).toString();
        final String bookId = getTextEditView(R.id.book_id).toString();
        final String quantity = getTextEditView(R.id.b_quantity).toString();

        String url = MainActivity.BASE_URL + "inventory/" + inventoryId;

        StringRequest MyStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("InventoryUpdate", "Inventory updated.");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                error.printStackTrace();
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
                    jsonBody.put("id", inventoryId);
                    jsonBody.put("branchId", branchId);
                    jsonBody.put("bookId", bookId);
                    jsonBody.put("quantity", quantity);
                    final String mRequestBody = jsonBody.toString();
                    Log.d("UpdateInvRequestLog", mRequestBody);
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (Exception uee) {
                    Log.d("Error", uee.getMessage());
                    return null;
                }
            }
        };

        requestQueue.add(MyStringRequest);
        Intent inventoryView = new Intent(InventoryActivity.this, ListInventory.class);
        startActivity(inventoryView);
    }

    public void deleteInventory(View v) {
        final String inventoryId = getTextEditView(R.id.i_id).toString();
        String url = MainActivity.BASE_URL + "inventory/" + inventoryId;
        Log.d("InventoryDelete", "Inventory being deleted" + inventoryId);
        StringRequest MyStringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("InventoryDelete", "Inventory deleted.");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        });
        requestQueue.add(MyStringRequest);
        Intent inventoryView = new Intent(InventoryActivity.this, MainActivity.class);
        startActivity(inventoryView);
    }


    private void setInventoryForm(String inventoryId) {
        String url = MainActivity.BASE_URL + "inventory/" + inventoryId;
        Log.d("Get Inventory Response:", url);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject inventory) {
                        Log.d("Get Inventory Response:", inventory.toString());
                        // Process the JSON
                        try {

                            // Get the current student (json object) data
                            String branchId = inventory.getString("branchId");
                            String bookId = inventory.getString("bookId");
                            String quantity = inventory.getString("quantity");

                            setTextEditView(R.id.b_id, branchId);
                            setTextEditView(R.id.book_id, bookId);
                            setTextEditView(R.id.b_quantity, quantity);


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

    public void getInventory(View v) {
        Intent BookView = new Intent(InventoryActivity.this, MainActivity.class);
        startActivity(BookView);
    }

    public void getBranches(View v) {
        Intent branchView = new Intent(InventoryActivity.this, MainActivity.class);
        startActivity(branchView);
    }
}
