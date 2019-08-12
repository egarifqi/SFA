package com.example.sfa;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ReasonNotECActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason_notec);
        TabLayout tabLayout = findViewById(R.id.tab_layoutreasonnotec);

        tabLayout.addTab(tabLayout.newTab().setText("Alasan Not EC"));
        Button sendalasan = (Button) findViewById(R.id.save_gambarnotec);

        sendalasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReasonNotECActivity.this, KunjunganActivity.class);
                StatusToko.statuskunjungan.add("2");
                startActivity(intent);
                finish();
            }
        });
    }

    public void pilihAlasan(View view) {
        TextView alasan = findViewById(R.id.tampil_alasannotec);
        Spinner spinner=(Spinner)findViewById(R.id.spinner_notec);
        String alasanNotEC = String.valueOf(spinner.getSelectedItem());
        alasan.setText(alasanNotEC);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backintent = new Intent(ReasonNotECActivity.this, KunjunganActivity.class);
        StatusToko.statuskunjungan.add("0");
        startActivity(backintent);
    }
}
