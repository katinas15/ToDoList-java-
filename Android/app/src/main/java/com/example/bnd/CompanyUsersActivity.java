package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;
import com.example.bnd.lab.Company;
import com.example.bnd.lab.Task;
import com.example.bnd.lab.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bnd.MainActivity.address;

public class CompanyUsersActivity extends AppCompatActivity {


    Company currentCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_users);
        Intent from = this.getIntent();
        currentCompany = (Company) from.getSerializableExtra("company");
        GetCompanyUsers users = new GetCompanyUsers();
        users.execute(Integer.toString(currentCompany.getId()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCompanyUsers users = new GetCompanyUsers();
        users.execute(Integer.toString(currentCompany.getId()));
    }

    private final class GetCompanyUsers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String dataParams = params[0];
            System.out.println("ISSIUSTA: " + dataParams);
            String url = address + "5labor_war/company/user?id="+dataParams;
            try {
                return TinkloKontroleris.sendGet(url);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Unable to get data from web!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("GAUTA: " + result);
            if (result != null) {
                Gson parseris = new Gson();
                try {
                    Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                    final List<User> users = new Gson().fromJson(result, listType);
                    ListView listView = findViewById(R.id.list);
                    ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>
                            (CompanyUsersActivity.this, android.R.layout.simple_list_item_1, users);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Toast info = Toast.makeText(CompanyUsersActivity.this, "Selected " + users.get(position), Toast.LENGTH_LONG);
                            info.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CompanyUsersActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CompanyUsersActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
            }
        }
    }
}
