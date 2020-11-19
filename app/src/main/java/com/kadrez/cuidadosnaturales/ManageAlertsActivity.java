package com.kadrez.cuidadosnaturales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ManageAlertsActivity extends AppCompatActivity {
    private Button createBtn;
    private Button listBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_alerts);

        createBtn = findViewById(R.id.createBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageAlertsActivity.this, AlertsActivity.class);
                startActivity(intent);
            }
        });

        listBtn = findViewById(R.id.listBtn);

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ManageAlertsActivity.this, AlertListActivity.class);
                startActivity(intent2);
            }
        });
    }
}