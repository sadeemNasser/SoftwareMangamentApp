package com.example.softwaremangamentapp.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.softwaremangamentapp.R;

public class ProjectItem extends AppCompatActivity {

    private TextView textViewItemStartDate;
    private TextView textViewItemEndDate;
    private TextView textViewProjectName;
    private TextView textViewtaskDes;
    private TextView textViewtaskC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewItemStartDate=findViewById(R.id.textViewItemStartDate);
        textViewItemEndDate=findViewById(R.id.textViewItemEndDate);
        textViewProjectName=findViewById(R.id.textViewProjectName);
        textViewtaskDes = findViewById(R.id.ProjectD);
        textViewtaskC = findViewById(R.id.projectC);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            textViewProjectName.setText(extras.getString("ProjectName"));
            textViewtaskDes.setText(extras.getString("ProjectD"));
            textViewItemStartDate.setText(extras.getString("Psdate"));
            textViewItemEndDate.setText(extras.getString("PeDate"));
            textViewtaskC.setText(extras.getString("ProjectCost"));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
