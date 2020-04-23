package com.example.softwaremangamentapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.softwaremangamentapp.R;


public class TaskItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewItemStartDate = findViewById(R.id.textViewItemStartDate);
        TextView textViewItemEndDate = findViewById(R.id.textViewItemEndDate);
        TextView textViewTasktName = findViewById(R.id.textViewTaskName);
        TextView textViewtaskR = findViewById(R.id.taskR);
        TextView textViewtaskC = findViewById(R.id.taskC);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            textViewItemStartDate.setText(extras.getString("Tsdate"));
            textViewItemEndDate.setText(extras.getString("TeDate"));
            textViewTasktName.setText(extras.getString("TaskName"));
            textViewtaskR.setText(extras.getString("TaskResource"));
            Double cost = extras.getDouble("TaskCost");
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
