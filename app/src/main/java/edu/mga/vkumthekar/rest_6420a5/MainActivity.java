package edu.mga.vkumthekar.rest_6420a5;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText etGitHubUser; // This will be a reference to our GitHub username input.
    Button btnGetRepos;  // This is a reference to the "Get Repos" button.
    TextView tvRepoList;  // This will reference our repo list text box.

    //String baseUrl = "http://34.73.117.117:8080/";  // This is Vaibhav's REST VM
    public static String BASE_URL = "http://35.188.245.24:8080/";  // This is Final Project VM
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // This is some magic for Android to load a previously saved state for when you are switching between actvities.
        setContentView(R.layout.activity_main);  // This links our code to our layout which we defined earlier.
        requestQueue = Volley.newRequestQueue(this);
        getBranches(null);
    }

    public void addBranchView(View v) {
        Intent branchView = new Intent(MainActivity.this, AddBranch.class);
        startActivity(branchView);
    }

    public void getBranches(View v) {
        String url = BASE_URL + "branches/";
        setContentView(R.layout.activity_main);
        final JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int counter = 100;
                        TableLayout ll = (TableLayout) findViewById(R.id.books_layout);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        lp.setMargins(2,5,2,5);
                        lp.width = 400;
                        Log.d("RESPONSE", response.toString());

                        // Check the length of our response (to see if the user has any repos)
                        if (response.length() > 0) {
                            // The user does have books, so let's loop through them all.
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    // For each repo, add a new line to our repo list.
                                    JSONObject book = response.getJSONObject(i);
                                    Log.d("RESPONSE - BOOK", book.toString());
                                    //String title = book.get("title").toString();
                                    //String description = book.get("description").toString();
                                    //String author = book.get("author").toString();
                                    //String thumbnailUrl = book.get("thumbnailUrl").toString();
                                    //Double price = Double.valueOf(book.get("price").toString());

                                    TableRow row = new TableRow(ll.getContext());
                                    row.setId(++counter+i);
                                    row.setLayoutParams(lp);
                                    row.setClickable(true);
                                    row.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            v.setBackgroundColor(Color.GRAY);
                                            System.out.println("Row clicked: " + v.getId());

                                            //get the data you need
                                            TableRow tablerow = (TableRow)v;
                                            TextView branch = (TextView) tablerow.getChildAt(0);
                                            String branchId = branch.getText().toString();
                                            Log.d("Show book with id", branchId);
                                            Intent branchesView = new Intent(MainActivity.this, DisplayBranchActivity.class);
                                            branchesView.putExtra("branchId", branchId);
                                            startActivity(branchesView);
                                        }
                                    });
                                    if(i % 2 == 0)
                                        row.setBackgroundColor(Color.rgb(240, 240, 240));
                                    else
                                        row.setBackgroundResource(R.drawable.border);

                                    TextView id = new TextView(row.getContext());
                                    id.setId(++counter + i);
                                    id.setLayoutParams(lp);
                                    id.setText(book.get("id").toString());
                                    row.addView(id);

                                    TextView title = new TextView(row.getContext());
                                    title.setId(++counter + i);
                                    title.setLayoutParams(lp);
                                    title.setText(book.get("branchName").toString());
                                    row.addView(title);

                                    TextView description = new TextView(row.getContext());
                                    description.setId(++counter + i);
                                    description.setLayoutParams(lp);
                                    description.setText(book.get("city").toString());
                                    row.addView(description);

                                    TextView price = new TextView(row.getContext());
                                    price.setId(++counter + i);
                                    price.setLayoutParams(lp);
                                    price.setText(book.get("zip").toString());
                                    row.addView(price);

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
                            l_id.setText("Id");
                            header.addView(l_id);

                            TextView l_title = new TextView(header.getContext());
                            l_title.setId(counter - 2);
                            l_title.setLayoutParams(lp);
                            l_title.setText("Name");
                            header.addView(l_title);

                            TextView l_description = new TextView(header.getContext());
                            l_description.setId(counter - 3);
                            l_description.setLayoutParams(lp);
                            l_description.setText("City");
                            header.addView(l_description);

                            TextView l_price = new TextView(header.getContext());
                            l_price.setId(counter - 4);
                            l_price.setLayoutParams(lp);
                            l_price.setText("Zip");
                            header.addView(l_price);

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
                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);
    }


    public void getInventory(View v) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        String url = BASE_URL+ "books";

        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Check the length of our response (to see if the user has any repos)
                        if (response.length() > 0) {
                            // The user does have repos, so let's loop through them all.
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    // For each repo, add a new line to our repo list.
                                    JSONObject jsonObj = response.getJSONObject(i);
                                    String repoName = jsonObj.get("name").toString();
                                    String lastUpdated = jsonObj.get("updated_at").toString();
                                    addToRepoList(repoName, lastUpdated);
                                } catch (JSONException e) {
                                    // If there is an error then output this to the logs.
                                    Log.e("Volley", "Invalid JSON Object.");
                                }

                            }
                        } else {
                            // The user didn't have any repos.
                            setRepoListText("No repos found.");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );

    }

    private void clearRepoList() {
        // This will clear the repo list (set it as a blank string).
        this.tvRepoList.setText("");
    }

    private void addToRepoList(String repoName, String lastUpdated) {
        // This will add a new repo to our list.
        // It combines the repoName and lastUpdated strings together.
        // And then adds them followed by a new line (\n\n make two new lines).
        String strRow = repoName + " / " + lastUpdated;
        String currentText = tvRepoList.getText().toString();
        this.tvRepoList.setText(currentText + "\n\n" + strRow);
    }

    private void setRepoListText(String str) {
        // This is used for setting the text of our repo list box to a specific string.
        // We will use this to write a "No repos found" message if the user doens't have any.
        this.tvRepoList.setText(str);
    }


}
