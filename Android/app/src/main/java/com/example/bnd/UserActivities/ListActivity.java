package com.example.bnd.UserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class ListActivity extends AppCompatActivity {

    String token;

    public void createProject(View view){
        EditText title = findViewById(R.id.projectEditText);
        String send = "{\"reqToken\":" + token + ",\"title\":\"" + title.getText().toString() + "\"}";
        CreateNewProject create = new CreateNewProject();
        create.execute(send);
    }

    private final class CreateNewProject extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String dataParams = params[0];
            System.out.println("ISSIUSTA: " + dataParams);
            String url = address + "5labor_war/user/project";
            try {
                return RESTController.sendPost(url, dataParams);
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
                GetUserProjects allProjects = new GetUserProjects();
                allProjects.execute(token);
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);
        Intent dabar = this.getIntent();
        token = (String)dabar.getSerializableExtra("token");
        GetUserProjects allProjects = new GetUserProjects();
        allProjects.execute(token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetUserProjects allProjects = new GetUserProjects();
        allProjects.execute(token);
    }

    private final class GetUserProjects extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String dataParams = params[0];
            System.out.println("ISSIUSTA: " + dataParams);
            String url = address + "5labor_war/user/project?reqToken="+dataParams;
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
                    final List<Project> projektai = new Gson().fromJson(result, listType);
                    ListView listView = findViewById(R.id.list);
                    ArrayAdapter<Project> arrayAdapter = new ArrayAdapter<Project>
                            (ListActivity.this, android.R.layout.simple_list_item_1, projektai);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Toast info = Toast.makeText(ListActivity.this, "Selected " + projektai.get(position), Toast.LENGTH_LONG);
                            info.show();
                            Intent newWindow = new Intent(ListActivity.this, TaskActivity.class);
                            newWindow.putExtra("project", projektai.get(position));
                            newWindow.putExtra("token", token);
                            startActivity(newWindow);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ListActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
            }
        }
    }

}
