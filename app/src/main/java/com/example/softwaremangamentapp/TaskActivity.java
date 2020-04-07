package com.example.softwaremangamentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softwaremangamentapp.Adapter.TaskAdapter;
import com.example.softwaremangamentapp.Model.TaskInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private ImageButton btncancel;
    private PopupWindow pw;
    private EditText taskName;
    private EditText taskRes;
    private EditText taskCost;
    private EditText startDate;
    private EditText endDate;
    private Button createBTN;
    private Button ProjectInfo;
    private TextView ProjectCost;
    Double totalCost=0.0;
    private RecyclerView list;
    Bundle extras;
    TaskInfo task;
    private TaskAdapter adapter;
    String projecId = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    FloatingActionButton buttonCreate;
    ArrayList<String> TaskList = new ArrayList<String>();
    ArrayList<TaskInfo> taskInfos = new ArrayList<TaskInfo>();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        buttonCreate = findViewById(R.id.addTask1);
        ProjectInfo = findViewById(R.id.ProjectInfo);
        extras = getIntent().getExtras();
        ProjectCost = (TextView) findViewById(R.id.projectC);
        if (extras != null)
            projecId = extras.getString("projectId");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = new TaskInfo();
        firebaseAuth = FirebaseAuth.getInstance();
        list = findViewById(R.id.list2);
        final RecyclerView recyclerView = findViewById(R.id.list2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new TaskAdapter(this,taskInfos,projecId));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userId=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference refrence =database.getReference().child("Projects").child(userId).child(projecId);
        DatabaseReference ref = database.getReference().child("Projects").child(userId).child(projecId).child("Tasks");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TaskList.clear();
                taskInfos.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    task = child.getValue(TaskInfo.class);
                    TaskList.add(task.getTaskName());
                    taskInfos.add(task);
                    totalCost+=task.getTaskCost();
                    refrence.child("totalCost").setValue(totalCost);

                }
                adapter.updateList(taskInfos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        adapter = new TaskAdapter(TaskActivity.this,taskInfos,projecId);


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) TaskActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.addtaskdialog,null);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.FILL_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                pw = new PopupWindow(layout, lp.width, lp.height, true);
                pw.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);


                btncancel = (ImageButton) layout.findViewById(R.id.button_close);
                btncancel.setOnClickListener(cancel_click);

                createBTN = (Button) layout.findViewById(R.id.create);
                createBTN.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AddTask();
                    }
                });

                taskName=(EditText) layout.findViewById(R.id.taskName);

                startDate=(EditText) layout.findViewById(R.id.startDate);
                startDate.setInputType(InputType.TYPE_NULL);
                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(TaskActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });
                endDate=(EditText) layout.findViewById(R.id.endDate);
                endDate.setInputType(InputType.TYPE_NULL);
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(TaskActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                taskRes=(EditText) layout.findViewById(R.id.taskRes);

                taskCost=(EditText) layout.findViewById(R.id.taskCost);

            }
        });
        if (extras != null)
            projecId = extras.getString("projectId");
        list = findViewById(R.id.list2);
        firebaseAuth = FirebaseAuth.getInstance();

        ProjectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProject();
            }
        });
    }

    private View.OnClickListener cancel_click = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();

        }
    };

    public void viewtask(TaskInfo task){
        Intent i = (new Intent(this, TaskItem.class));
        i.putExtra("TaskName",task.getTaskName());
        i.putExtra("Tsdate",task.getTaskStartDate());
        i.putExtra("TeDate",task.getTaskEndDate());
        i.putExtra("TaskResource",task.getTaskResource());
        i.putExtra("TaskCost",task.getTaskCost());
        i.putExtra("TaskPid",task.getProjectID());
        i.putExtra("projectId", projecId );;
        startActivity(i);

    }

    public void viewProject(){
        Intent i = new Intent(this, ProjectItem.class);
        i.putExtra("ProjectName",extras.getString("projectName"));
        i.putExtra("Psdate",extras.getString("sDate"));
        i.putExtra("PeDate",extras.getString("eDate"));
        i.putExtra("ProjectD",extras.getString("projectD"));
        i.putExtra("ProjectCost",totalCost);
        startActivity(i);

    }

    private void  AddTask(){

        TaskInfo task =new TaskInfo();

        task.setTaskName(taskName.getText().toString().trim());
        task.setTaskStartDate(startDate.getText().toString().trim());
        task.setTaskEndDate(endDate.getText().toString().trim());
        task.setTaskResource(taskRes.getText().toString().trim());
        Double cost = Double.parseDouble(taskCost.getText().toString());
        task.setTaskCost(cost);
        task.setStatues("NotYet");

        String userId=firebaseAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId).child(projecId).child("Tasks");
        String key = mDatabase.push().getKey();
        mDatabase.child(key).setValue(task);
        Toast.makeText(TaskActivity.this,"Successfully created",Toast.LENGTH_LONG).show();
        pw.dismiss();

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