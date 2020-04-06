package com.example.softwaremangamentapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.softwaremangamentapp.R;


public class TaskItem extends AppCompatActivity {
    private TextView textViewItemStartDate;
    private TextView textViewItemEndDate;
    private TextView textViewTasktName;
    private TextView textViewtaskR;
    private TextView textViewtaskC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewItemStartDate=findViewById(R.id.textViewItemStartDate);
        textViewItemEndDate=findViewById(R.id.textViewItemEndDate);
        textViewTasktName=findViewById(R.id.textViewTaskName);
        textViewtaskR=findViewById(R.id.taskR);
        textViewtaskC=findViewById(R.id.taskC);
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
