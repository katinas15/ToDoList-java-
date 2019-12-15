package com.example.bnd.UserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bnd.R;
import com.example.bnd.REST.RESTController;
import com.example.bnd.Objektai.Project;
import com.example.bnd.Objektai.Task;

import static com.example.bnd.UserActivities.MainActivity.address;

public class SubtaskActivity extends AppCompatActivity {

    Task currentTask;
    String token;
    Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask);
        Intent from = this.getIntent();
        currentProject = (Project) from.getSerializableExtra("project");
        token = (String)from.getSerializableExtra("token");
        currentTask = (Task)from.getSerializableExtra("task");
        TextView info = (TextView)findViewById(R.id.taskInfo);
        info.setText("Task - " + currentTask);
    }

    public void deleteTaskClick(View view){
        String send = "{\"reqToken\":" + token + ",\"taskId\":" + currentTask.getId() + "}";
        DeleteTask create = new DeleteTask();
        create.execute(send);
    }

    private final class DeleteTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SubtaskActivity.this, "Task changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "5labor_war/project/task";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return RESTController.sendDelete(url, postDataParams);
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
        String send = "{\"reqToken\":" + token + ",\"taskId\":" + currentTask.getId() + ",\"title\":\"" + title.getText().toString() + "\"}";
        EditTask create = new EditTask();
        create.execute(send);
    }

    public void toggleTaskClick(View view){
        if(currentTask.getCompletedBy() == null){
            String send = "{\"reqToken\":" + token + ",\"taskId\":\"" + currentTask.getId() + "\", \"completed\":\"" + true + "\"}";
            EditTask complete = new EditTask();
            complete.execute(send);
        } else {
            String send = "{\"reqToken\":" + token + ",\"taskId\":\"" + currentTask.getId() + "\", \"reopen\":\"" + true + "\"}";
            EditTask complete = new EditTask();
            complete.execute(send);
        }
    }


    private final class EditTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SubtaskActivity.this, "Task changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "5labor_war/project/task";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return RESTController.sendPut(url, postDataParams);
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

    public void createSubtaskCilck(View view){
        EditText title = findViewById(R.id.subtaskEditText);
        String send = "{\"reqToken\":" + token + ",\"taskId\":" + currentTask.getId() + ",\"projectId\":" + currentProject.getId() + ",\"title\":\"" + title.getText().toString() + "\"}";
        CreateNewTask create = new CreateNewTask();
        create.execute(send);
    }

    private final class CreateNewTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String dataParams = params[0];
            System.out.println("ISSIUSTA: " + dataParams);
            String url = address + "5labor_war/project/task";
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
                finish();
            }
        }

    }
}
