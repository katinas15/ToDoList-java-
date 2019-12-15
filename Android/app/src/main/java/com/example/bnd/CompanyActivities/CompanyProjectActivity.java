package com.example.bnd.CompanyActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bnd.R;
import com.example.bnd.REST.RESTController;
import com.example.bnd.Objektai.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bnd.UserActivities.MainActivity.address;

public class CompanyProjectActivity extends AppCompatActivity {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_project);
        Intent from = this.getIntent();
        token = (String)from.getSerializableExtra("token");
        GetCompanyProjects projects = new GetCompanyProjects();
        projects.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCompanyProjects projects = new GetCompanyProjects();
        projects.execute();
    }

    private final class GetCompanyProjects extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            System.out.println("ISSIUSTA: " + token);
            String url = address + "5labor_war/company/project?reqToken=" + token;
            try {
                return RESTController.sendGet(url);
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
                    Type listType = new TypeToken<ArrayList<Project>>(){}.getType();
                    final List<Project> projects = new Gson().fromJson(result, listType);
                    ListView listView = findViewById(R.id.list);
                    ArrayAdapter<Project> arrayAdapter = new ArrayAdapter<>
                            (CompanyProjectActivity.this, android.R.layout.simple_list_item_1, projects);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Toast info = Toast.makeText(CompanyProjectActivity.this, "Selected " + projects.get(position), Toast.LENGTH_LONG);
                            info.show();
                            Intent newWindow = new Intent(CompanyProjectActivity.this, CompanyProjectTaskActivity.class);
                            newWindow.putExtra("token", token);
                            newWindow.putExtra("project", projects.get(position));
                            startActivity(newWindow);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CompanyProjectActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CompanyProjectActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
            }
        }
    }
}
