package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bnd.lab.Company;
import com.example.bnd.lab.Project;
import com.example.bnd.lab.User;

public class CompanyActivity extends AppCompatActivity {
    Company currentCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        Intent from = this.getIntent();
        currentCompany = (Company) from.getSerializableExtra("company");
        TextView info = (TextView)findViewById(R.id.companyText);
        info.setText("Company - " + currentCompany);
    }

    public void companyUsers(View view){
        Intent newWindow = new Intent(CompanyActivity.this, com.example.bnd.CompanyUsersActivity.class);
        newWindow.putExtra("company", currentCompany);
        startActivity(newWindow);
    }

    public void companyProjects(View view){
        Intent newWindow = new Intent(CompanyActivity.this, com.example.bnd.CompanyProjectActivity.class);
        newWindow.putExtra("company", currentCompany);
        startActivity(newWindow);
    }

}
