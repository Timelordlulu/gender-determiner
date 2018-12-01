package finalproject.gender_determiner;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab11:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;
    /** the outcome. */
    private TextView outcomereview;
    /** enter the name.  */
    private EditText enterNameText;
    /** enter the country code. */
    private EditText enterCountrycodeText;
    /** the name stored. */
    private String inputName = "";
    /** the country code stored. */
    private String inputCountryCode = "";
    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        outcomereview = findViewById(R.id.outcome);
        Button submitButton = findViewById(R.id.submit);
        Button updateButton = findViewById(R.id.update);
        enterNameText = findViewById(R.id.enter_name);
        enterCountrycodeText = findViewById(R.id.enter_countrycode);
        requestQueue = Volley.newRequestQueue(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputName = enterNameText.getText().toString();
                inputCountryCode = enterCountrycodeText.getText().toString();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse(inputName, inputCountryCode);
            }
        });
    }

    /**
     * patse function.
     * @param inputName2
     * @param inputCountryCode2
     */
    private void jsonParse(final String inputName2, final String inputCountryCode2) {
        String url = "https://api.genderize.io/?name=" + inputName2 + "&country_id=" + inputCountryCode2;
        if (inputCountryCode2.equals("")) {
            url = "https://api.genderize.io/?name=" + inputName2;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            if (response.getString("gender").equals("null")) {
                                outcomereview.setText("not found");
                            } else {
                                String name = "name: " + response.getString("name");
                                String gender = "gender: " + response.getString("gender");
                                String probability = "accuracy: " + response.getString("probability");
                                String count = "count: " + response.getInt("count");
                                //String country_idAppend = "country id: " + response.getString("country_id");
                                outcomereview.setText("");
                                outcomereview.append(name + "\n");
                                outcomereview.append(gender + "\n");
                                outcomereview.append(probability + "\n");
                                outcomereview.append(count + "\n");
                                if (inputCountryCode2.equals("")) {
                                    outcomereview.append("Country id : World Wide\n");
                                } else {
                                    outcomereview.append("Country id : " + inputCountryCode2 + "\n");
                                }
                                //myTextViewResult.append(country_idAppend+ "\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the IP geolocation API.
     *
     * @param ipAddress IP address to look up
     */
    void startAPICall(final String ipAddress) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://ipinfo.io/" + ipAddress + "/json",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            Log.i(TAG, response.get("hostname").toString());
        } catch (JSONException ignored) { }
    }
}

