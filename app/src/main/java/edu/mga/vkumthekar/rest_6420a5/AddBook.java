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

public class AddBook extends AppCompatActivity {
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void addBook(View v) {

        final String bookid = getTextEditView(R.id.book_id).toString();
        final String description = getTextEditView(R.id.book_description).toString();
        final String title = getTextEditView(R.id.book_title).toString();
        final String price = getTextEditView(R.id.book_price).toString();


        String url = MainActivity.BASE_URL + "books/";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BookAdd", "Book added.");
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
                    jsonBody.put("bookid", bookid);
                    jsonBody.put("description", description);
                    jsonBody.put("title", title);
                    jsonBody.put("price", price);
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
        Intent BookView = new Intent(AddBook.this, MainActivity.class);
        startActivity(BookView);
    }

    public void getBranches(View v) {
        Intent branchView = new Intent(AddBook.this, MainActivity.class);
        startActivity(branchView);
    }
}
