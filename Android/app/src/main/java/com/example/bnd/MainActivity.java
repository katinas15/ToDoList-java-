package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;
import com.example.bnd.lab.Company;
import com.example.bnd.lab.User;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    public static String address = "http://192.168.56.1:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void prisijungi(View v) {
        EditText log = findViewById(R.id.l_login);
        EditText pas = findViewById(R.id.l_pass);
        String login = log.getText().toString();
        String pass = pas.getText().toString();
        String siuntimui = "{\"login\":\"" + login + "\", \"pass\":\"" + pass + "\"}";
        CheckBox check = findViewById(R.id.checkBox);

        if(check.isChecked()){
            CompanyLogin prisijungti = new CompanyLogin();
            prisijungti.execute(siuntimui);
        } else {
            UserLogin prisijungti = new UserLogin();
            prisijungti.execute(siuntimui);
        }
    }

    private final class UserLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Tikrinami prisijungimo duomenys", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "5labor_war/login";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return TinkloKontroleris.sendPost(url, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Unable to get data from web!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("GAUTA: " + result);
            if (result != null && !result.equals("Neteisingai ivesti duomenys")) {
                Gson parseris = new Gson();
                try {
                    Intent intent = new Intent(MainActivity.this, com.example.bnd.ListActivity.class);
                    intent.putExtra("token", result);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Incorrect Data", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Incorrect Data", Toast.LENGTH_LONG).show();
            }
        }
    }

    private final class CompanyLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Tikrinami prisijungimo duomenys", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "5labor_war/login/company";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return TinkloKontroleris.sendPost(url, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Unable to get data from web!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("GAUTA: " + result);
            if (result != null && !result.equals("Neteisingai ivesti duomenys")) {
                Gson parseris = new Gson();
                try {
                    Intent intent = new Intent(MainActivity.this, com.example.bnd.CompanyActivity.class);
                    intent.putExtra("token", result);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Incorrect Data", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Incorrect Data", Toast.LENGTH_LONG).show();
            }
        }
    }
}




