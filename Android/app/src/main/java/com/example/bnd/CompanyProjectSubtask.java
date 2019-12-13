package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;
import com.example.bnd.lab.Project;
import com.example.bnd.lab.Task;
import com.example.bnd.lab.User;

import static com.example.bnd.MainActivity.address;

public class CompanyProjectSubtask extends AppCompatActivity {

    Task currentTask;
    User currentUser;
    Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_project_subtask);
        Intent from = this.getIntent();
        currentProject = (Project) from.getSerializableExtra("project");
        currentTask = (Task)from.getSerializableExtra("task");
        TextView info = (TextView)findViewById(R.id.taskInfo);
        info.setText("Task - " + currentTask);
    }

    public void deleteTaskClick(View view){
        String send = "{\"taskId\":" + currentTask.getId() + "}";
        DeleteTask create = new DeleteTask();
        create.execute(send);
    }

    private final class DeleteTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CompanyProjectSubtask.this, "Task changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url =  address + "5labor_war/project/task";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return TinkloKontroleris.sendDelete(url, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Unable to get data from web!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            finish();
        }
    }

    public void editTaskClick(View view){
        EditText title = findViewById(R.id.taskEditText);
        String send = "{\"taskId\":" + currentTask.getId() + ",\"title\":\"" + title.getText().toString() + "\"}";
        EditTask create = new EditTask();
        create.execute(send);
    }

    private final class EditTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CompanyProjectSubtask.this, "Task changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "5labor_war/project/task";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return TinkloKontroleris.sendPut(url, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Unable to get data from web!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            finish();
        }
    }


}
