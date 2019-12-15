package com.example.bnd.CompanyActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bnd.R;

public class CompanyActivity extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        Intent from = this.getIntent();
        token = (String)from.getSerializableExtra("token");
    }

    public void companyUsers(View view){
        Intent newWindow = new Intent(CompanyActivity.this, CompanyUsersActivity.class);
        newWindow.putExtra("token", token);
        startActivity(newWindow);
    }

    public void companyProjects(View view){
        Intent newWindow = new Intent(CompanyActivity.this, CompanyProjectActivity.class);
        newWindow.putExtra("token", token);
        startActivity(newWindow);
    }

}
