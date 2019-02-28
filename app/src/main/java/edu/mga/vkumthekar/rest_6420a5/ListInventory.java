package edu.mga.vkumthekar.rest_6420a5;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListInventory extends AppCompatActivity {
    RequestQueue requestQueue;

    public void addBookView(View v) {
        Intent bookView = new Intent(ListInventory.this, AddBook.class);
        startActivity(bookView);
    }

    public void getBranches(View v) {
        Intent branchView = new Intent(ListInventory.this, MainActivity.class);
        startActivity(branchView);
    }

    public void getInventory(View v) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        String url = MainActivity.BASE_URL + "inventory/";
        setContentView(R.layout.activity_main);
        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        final JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int counter = 100;
                        TableLayout ll = findViewById(R.id.inventory_layout);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        lp.setMargins(2, 5, 2, 5);
                        lp.width = 400;
                        Log.d("Inventory RESPONSE", response.toString());

                        // Check the length of our response (to see if the user has any repos)
                        if (response.length() > 0) {
                            // The user does have books, so let's loop through them all.
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    // For each repo, add a new line to our repo list.
                                    JSONObject inventory = response.getJSONObject(i);
                                    Log.d("RESPONSE - inventory", inventory.toString());
                                    //String title = book.get("title").toString();
                                    //String description = book.get("description").toString();
                                    //String author = book.get("author").toString();
                                    //String thumbnailUrl = book.get("thumbnailUrl").toString();
                                    //Double price = Double.valueOf(book.get("price").toString());

                                    TableRow row = new TableRow(ll.getContext());
                                    row.setId(++counter + i);
                                    row.setLayoutParams(lp);
                                    row.setClickable(true);
                                    row.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            v.setBackgroundColor(Color.GRAY);
                                            System.out.println("Row clicked: " + v.getId());

                                            //get the data you need
                                            TableRow tablerow = (TableRow) v;
                                            TextView inventory = (TextView) tablerow.getChildAt(0);
                                            String inventoryId = inventory.getText().toString();
                                            Log.d("Show inventory with id", inventoryId);
                                            Intent inventoryView = new Intent(ListInventory.this, InventoryActivity.class);
                                            inventoryView.putExtra("inventoryId", inventoryId);
                                            startActivity(inventoryView);
                                        }
                                    });
                                    if (i % 2 == 0)
                                        row.setBackgroundColor(Color.rgb(240, 240, 240));
                                    else
                                        row.setBackgroundResource(R.drawable.border);

                                    TextView id = new TextView(row.getContext());
                                    id.setId(++counter + i);
                                    id.setLayoutParams(lp);
                                    id.setText(inventory.get("id").toString());
                                    row.addView(id);

                                    TextView branch = new TextView(row.getContext());
                                    branch.setId(++counter + i);
                                    branch.setLayoutParams(lp);
                                    branch.setText(inventory.get("branchName").toString());
                                    row.addView(branch);

                                    TextView book = new TextView(row.getContext());
                                    book.setId(++counter + i);
                                    book.setLayoutParams(lp);
                                    book.setText(inventory.get("bookTitle").toString());
                                    row.addView(book);

                                    TextView quantity = new TextView(row.getContext());
                                    quantity.setId(++counter + i);
                                    quantity.setLayoutParams(lp);
                                    quantity.setText(inventory.get("quantity").toString());
                                    row.addView(quantity);

                                    ll.addView(row, i, lp);

                                } catch (JSONException e) {
                                    // If there is an error then output this to the logs.
                                    Log.e("Volley", "Invalid JSON Object.");
                                }

                            }

                            TableRow header = new TableRow(ll.getContext());
                            header.setId(counter - 1);
                            header.setBackgroundColor(Color.rgb(188, 244, 188));
                            header.setLayoutParams(lp);

                            TextView l_id = new TextView(header.getContext());
                            l_id.setId(counter - 2);
                            l_id.setLayoutParams(lp);
                            l_id.setText("InventoryId");
                            header.addView(l_id);

                            TextView l_branch = new TextView(header.getContext());
                            l_branch.setId(counter - 2);
                            l_branch.setLayoutParams(lp);
                            l_branch.setText("Branch");
                            header.addView(l_branch);

                            TextView l_book = new TextView(header.getContext());
                            l_book.setId(counter - 3);
                            l_book.setLayoutParams(lp);
                            l_book.setText("Book");
                            header.addView(l_book);

                            TextView l_quantity = new TextView(header.getContext());
                            l_quantity.setId(counter - 4);
                            l_quantity.setLayoutParams(lp);
                            l_quantity.setText("Quantity");
                            header.addView(l_quantity);

                            ll.addView(header, 0, lp);
                        } else {
                            Log.e("NoBranch", "No branch found");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_inventory);
        requestQueue = Volley.newRequestQueue(this);
        getInventory(null);

    }
}
