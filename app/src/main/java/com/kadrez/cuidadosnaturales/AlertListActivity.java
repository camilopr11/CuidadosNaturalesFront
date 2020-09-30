package com.kadrez.cuidadosnaturales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codewithajit.todoapp.UtilsService.SharedPreferenceClass;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewAdapter;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kadrez.cuidadosnaturales.Models.Alert;
import java.util.List;
import java.util.ArrayList;

public class AlertListActivity extends AppCompatActivity {
    private Button backBtn, getBtn;
    ProgressBar progressBar;

    private List<Alert> alerts ;
    private RecyclerView recyclerView ;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_list);
        alerts = new ArrayList<>() ;
        recyclerView = findViewById(R.id.listRecyclerView);
        getBtn = findViewById(R.id.getBtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlerts(view);
            }
        });




        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlertListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();

        sharedPreferenceClass = new SharedPreferenceClass(this);


    }

    private void getAlerts(View view) {
        progressBar.setVisibility(View.VISIBLE);


        String apiKey = "https://cuidadosnaturales.herokuapp.com/listAlerts";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                apiKey, null, new Response.Listener<JSONArray>() {


                @Override
                public void onResponse(JSONArray response) {
                // display response
                    try {

                        for(int i = 0; i < response.length(); i++){

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            try {
                                Alert alert = new Alert();
                                alert.setName(response.getJSONObject(i).getString("plant"));
                                alert.setAlertType(response.getJSONObject(i).getString("type"));
                                String dtStart = response.getJSONObject(i).getString("date");
                                System.out.println("dtStart: "+dtStart);
                                Date date = format.parse(dtStart);
                                System.out.println("date: "+date);
                                alert.setDate(date);
                                alert.setImage_url(response.getJSONObject(i).getString("img_url"));
                                alerts.add(alert);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Response", response.toString());
                    setuprecyclerview(alerts);
                    progressBar.setVisibility(View.GONE);
            }
            },
                    new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
            }) {

        };

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

        private void setuprecyclerview(List<Alert> alerts) {


            RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,alerts) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(myadapter);

        }


}