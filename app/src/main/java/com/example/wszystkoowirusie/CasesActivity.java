package com.example.wszystkoowirusie;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class CasesActivity extends AppCompatActivity {
    TextView countryName;
    TextView totalCases;
    TextView deatchCases;
    TextView recoveryCases;
    TextView currentlyCases;
    TextView criticalCases;
    EditText editText;
    boolean forAllWorld = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);
        countryName = findViewById(R.id.countryNameTextView);
        totalCases = findViewById(R.id.totalCasesTextView);
        deatchCases = findViewById(R.id.deatchCasesTextView);
        recoveryCases = findViewById(R.id.recoveryCasesTextView);
        currentlyCases = findViewById(R.id.currentlyCasesTextView);
        criticalCases = findViewById(R.id.criticalCasesTextView);
        try{
            DownloadTask task = new DownloadTask();
            task.execute("https://coronavirus-19-api.herokuapp.com/all");
        }catch (Exception e){
            Log.i("błąd", "błąd");
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void setTextInTextViews(String country, String totalCases, String deatchCases, String recoveryCases, String currentlyCases, String criticalCases){
        this.countryName.setText(country);
        this.totalCases.setText("Wszystkich zachorowań: "+totalCases);
        this.deatchCases.setText("Przypadków śmiertelnych: "+deatchCases);
        this.recoveryCases.setText("Osoby które wyzdrowiały: "+recoveryCases);
        this.currentlyCases.setText("Osoby obecnie chore: "+currentlyCases);
        this.criticalCases.setText("Pacjenci w stanie krytycznym: "+criticalCases);
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1)
                {
                    char current = (char)data;
                    result +=current;
                    data = reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return  null;
            }
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String cases = jsonObject.getString("cases").toString();
                System.out.println(cases);
                if(forAllWorld) {
                    totalCases.setText("Wszystkich zachorowań: "+jsonObject.getString("cases"));
                    deatchCases.setText("Przypadków śmiertelnych: "+jsonObject.getString("deaths"));
                    recoveryCases.setText("Osoby które wyzdrowiały: "+jsonObject.getString("recovered"));
                }else{
                    setTextInTextViews(jsonObject.getString("country"),jsonObject.getString("cases"), jsonObject.getString("deaths"), jsonObject.getString("recovered"), jsonObject.getString("active"), jsonObject.getString("critical"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getInfoForCountry(View view){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        forAllWorld = false;
        DownloadTask task = new DownloadTask();
        editText = findViewById(R.id.editText);
        if(!editText.getText().toString().equals("")) {
            try {
                task.execute("https://coronavirus-19-api.herokuapp.com/countries/"+editText.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Nie można znaleźć państwa. Upewnij się, że podano poprawną nazwę",Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Wprowadź nazwę państwa",Toast.LENGTH_SHORT).show();
        }
    }
}

