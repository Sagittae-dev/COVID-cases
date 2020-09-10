package com.example.wszystkoowirusie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void checkCases(View view){
        Log.i("cases","next page and show Cases");
        Intent intent = new Intent(getApplicationContext(),CasesActivity.class);
        startActivity(intent);
    }

    public void startWeb(View view){
        Intent intent = new Intent(getApplicationContext(),WebActivity.class);
        if(view.getTag().equals("gov"))
        intent.putExtra("link","https://www.gov.pl/web/koronawirus/podejrzewasz-u-siebie-koronawirusa");
        else if(view.getTag().equals("onet"))
        intent.putExtra("link","https://wiadomosci.onet.pl/kraj/koronawirus-w-polsce-kolejne-ofiary-smiertelne-relacja/bbv496k");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}