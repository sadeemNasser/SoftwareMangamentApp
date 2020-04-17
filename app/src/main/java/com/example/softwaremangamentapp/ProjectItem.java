package com.example.softwaremangamentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.softwaremangamentapp.R;

public class ProjectItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewItemStartDate = findViewById(R.id.textViewItemStartDate);
        TextView textViewItemEndDate = findViewById(R.id.textViewItemEndDate);
        TextView textViewProjectName = findViewById(R.id.textViewProjectName);
        TextView textViewtaskDes = findViewById(R.id.ProjectD);
        TextView textViewtaskC = findViewById(R.id.projectC);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            textViewProjectName.setText(extras.getString("ProjectName"));
            textViewtaskDes.setText(extras.getString("ProjectD"));
            textViewItemStartDate.setText(extras.getString("Psdate"));
            textViewItemEndDate.setText(extras.getString("PeDate"));
            Double cost =extras.getDouble("ProjectCost");
            textViewtaskC.setText(cost.toString());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
