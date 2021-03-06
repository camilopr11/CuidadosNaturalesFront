package com.kadrez.cuidadosnaturales;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewAlertsAdapter;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewInfoAdapter;
import com.kadrez.cuidadosnaturales.Models.Info;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaListActivity extends AppCompatActivity {
    private Button getBtn;
    ProgressBar progressBar;

    private List<Info> infos;
    private RecyclerView recyclerView;
    UtilService utilService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_list);
        infos = new ArrayList<>();
        recyclerView = findViewById(R.id.listRecyclerView);
        getBtn = findViewById(R.id.getBtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listInfo(view);
            }
        });



        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();



    }

    private void listInfo(View view) {
        progressBar.setVisibility(View.VISIBLE);


        String apiKey = "https://cuidadosnaturales.herokuapp.com/enciclopedia/listInfo";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                apiKey, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                // save response
                try {

                    for (int i = 0; i < response.length(); i++) {
                            Info info = new Info();
                            info.setTitle(response.getJSONObject(i).getString("title"));
                            info.setDescription(response.getJSONObject(i).getString("description"));
                            info.setContent(response.getJSONObject(i).getString("content"));
                            info.setCategory(response.getJSONObject(i).getString("category"));
                            infos.add(info);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response", response.toString());
                setuprecyclerview(infos);
                progressBar.setVisibility(View.GONE);
            }
        },
                new Response.ErrorListener() {
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

    private void setuprecyclerview(List<Info> infos) {


        RecyclerViewInfoAdapter myadapter = new RecyclerViewInfoAdapter(this, infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }


}