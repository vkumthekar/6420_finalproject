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

import org.json.JSONException;
import org.json.JSONObject;

public class AddBook extends AppCompatActivity {
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void addBook(View v) {

        final String b_quantity = getTextEditView(R.id.b_quantity).toString();
        final String description = getTextEditView(R.id.book_description).toString();
        final String title = getTextEditView(R.id.book_title).toString();
        final String price = getTextEditView(R.id.book_price).toString();
        final String branch_id = getTextEditView(R.id.b_id).toString();
        final String author = getTextEditView(R.id.author).toString();
        final String publisher = getTextEditView(R.id.publisher).toString();
        final String thumbnailUrl = getTextEditView(R.id.thumbURL).toString();


        String url = MainActivity.BASE_URL + "books/";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BookAdd", "Book added. Now creating inventory");
                String book_id = null;
                try {
                    Log.d("BoodResponse", response);
                    JSONObject book = new JSONObject(response);
                    book_id = book.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateInventory(branch_id, book_id, b_quantity);
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
                    jsonBody.put("author", author);
                    jsonBody.put("description", description);
                    jsonBody.put("title", title);
                    jsonBody.put("price", price);
                    jsonBody.put("publisher", publisher);
                    jsonBody.put("thumbnailUrl", thumbnailUrl);
                    final String mRequestBody = jsonBody.toString();
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (Exception uee) {
                    return null;
                }
            }
        };

        requestQueue.add(MyStringRequest);
        Intent bookView = new Intent(AddBook.this, MainActivity.class);
        startActivity(bookView);
    }

    private CharSequence getTextEditView(int key) {
        TextView textView = findViewById(key);
        return textView.getText();
    }

    public void getInventory(View v) {
        Intent inventoryView = new Intent(AddBook.this, ListInventory.class);
        startActivity(inventoryView);
    }

    public void getBranches(View v) {
        Intent branchView = new Intent(AddBook.this, MainActivity.class);
        startActivity(branchView);
    }

    private void updateInventory(final String branchId, final String bookId, final String quantity) {

        String url = MainActivity.BASE_URL + "inventory/";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
        Intent inventoryView = new Intent(AddBook.this, ListInventory.class);
        startActivity(inventoryView);
    }
}
