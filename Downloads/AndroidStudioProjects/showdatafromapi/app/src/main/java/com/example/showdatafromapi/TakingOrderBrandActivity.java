package com.abhishekpanwar.receivedatajson;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class TakingOrderBrandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_order_brand);

        TabLayout tabLayout = findViewById(R.id.tab_layoutbrand);

        tabLayout.addTab(tabLayout.newTab().setText("Nama Toko"));


//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CardView wardah = (CardView) findViewById(R.id.wardah);
        CardView makeover = (CardView) findViewById(R.id.makeover);
        CardView emina = (CardView) findViewById(R.id.emina);
        CardView putri = (CardView) findViewById(R.id.putri);

        wardah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakingOrderBrandActivity.this, HistoricalSalesActivity.class);
                startActivity(intent);
            }
        });
        makeover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakingOrderBrandActivity.this, HistoricalSalesMOActivity.class);
                startActivity(intent);
            }
        });
        emina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakingOrderBrandActivity.this, HistoricalSalesEminaActivity.class);
                startActivity(intent);
            }
        });
        putri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakingOrderBrandActivity.this, HistoricalSalesPutriActivity.class);
                startActivity(intent);
            }
        });
    }
}
