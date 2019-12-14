package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;
import com.example.bnd.lab.Company;
import com.example.bnd.lab.Project;
import com.example.bnd.lab.Task;
import com.example.bnd.lab.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bnd.MainActivity.address;

public class CompanyProjectTaskActivity extends AppCompatActivity {

    String token;
    Project currentProject;

    public void editProjectTitle(View view){
        EditText title = findViewById(R.id.projectEditText);
        currentProject.setTitle(title.getText().toString());
        String send = "{\"reqToken\":" + token + ",\"projectId\":" + currentProject.getId() + ",\"title\":\"" + title.getText().toString() + "\"}";
        EditProject create = new EditProject();
        create.execute(send);
    }

    private final class EditProject extends AsyncTask<String, String, String> {

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
                return TinkloKontroleris.sendPut(url, dataParams);
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
                resetText();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_project_task);
        Intent from = this.getIntent();
        currentProject = (Project) from.getSerializableExtra("project");
        token = (String)from.getSerializableExtra("token");
        resetText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProjectTasks allProjects = new GetProjectTasks();
        allProjects.execute(Integer.toString(currentProject.getId()));
    }

    private void resetText(){
        TextView info = (TextView)findViewById(R.id.projectInfo);
        info.setText("Project - " + currentProject);
        GetProjectTasks allProjects = new GetProjectTasks();
        allProjects.execute(Integer.toString(currentProject.getId()));
    }

    private final class GetProjectTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String dataParams = params[0];
            System.out.println("ISSIUSTA: " + dataParams);
            String url = address + "5labor_war/project/task?reqToken=" + token + "&id="+dataParams;
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
                    Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
                    final List<Task> taskai = new Gson().fromJson(result, listType);
                    ListView listView = findViewById(R.id.list);
                    ArrayAdapter<Task> arrayAdapter = new ArrayAdapter<Task>
                            (CompanyProjectTaskActivity.this, android.R.layout.simple_list_item_1, taskai);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Toast info = Toast.makeText(CompanyProjectTaskActivity.this, "Selected " + taskai.get(position), Toast.LENGTH_LONG);
                            info.show();
                            Intent newWindow = new Intent(CompanyProjectTaskActivity.this, com.example.bnd.CompanyProjectSubtask.class);
                            newWindow.putExtra("task", taskai.get(position));
                            newWindow.putExtra("token", token);
                            newWindow.putExtra("project", currentProject);
                            startActivity(newWindow);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CompanyProjectTaskActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CompanyProjectTaskActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
            }
        }
    }
}
