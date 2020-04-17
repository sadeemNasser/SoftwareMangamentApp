package com.example.softwaremangamentapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import android.widget.Toast;

import com.example.softwaremangamentapp.Adapter.ProjectAdapter;
import com.example.softwaremangamentapp.Model.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.addProject1)
    FloatingActionButton _addProject;
    private PopupWindow pw;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatePickerDialog picker;
    private EditText projectName;
    private EditText projectDesc;
    private EditText startDate;
    private EditText endDate;
    private static ProjectAdapter adapter;
    private Project project;
    private Project project1;
    private ArrayList<String> projectList = new ArrayList<String>();
    private static ArrayList<Project> projectList2 = new ArrayList<Project>();

    public static void updateList(ArrayList<Project> newList) {
        projectList2 = new ArrayList<>();
        projectList2.addAll(newList);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        project = new Project();
        firebaseAuth = FirebaseAuth.getInstance();
        final RecyclerView recyclerView = findViewById(R.id.list1);
        recyclerView.setAdapter(new ProjectAdapter(this, projectList2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userId = firebaseAuth.getCurrentUser().getUid();
        String path = "Projects" + "/" + userId;
        DatabaseReference ref = database.getReference(path);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();
                projectList2.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    project1 = child.getValue(Project.class);
                    projectList.add(project1.getProjectName());
                    projectList2.add(project1);
                }
                adapter.notifyDataSetChanged();
                adapter.updateList(projectList2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        adapter = new ProjectAdapter(MainActivity.this, projectList2);


        _addProject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initiatePopupWindow();
            }
        });

    }

    @SuppressWarnings("deprecation")
    private void initiatePopupWindow() {

        LayoutInflater inflater = (LayoutInflater) MainActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.addprojectdoalog, null);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        pw = new PopupWindow(layout, lp.width, lp.height, true);
        pw.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);


        ImageButton btncancel = (ImageButton) layout.findViewById(R.id.button_close);
        btncancel.setOnClickListener(cancel_click);

        Button createBTN = (Button) layout.findViewById(R.id.create);
        createBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CreateProject();
            }
        });

        projectName = (EditText) layout.findViewById(R.id.projectName);

        projectDesc = (EditText) layout.findViewById(R.id.projectDesc);

        startDate = (EditText) layout.findViewById(R.id.startDate);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        endDate = (EditText) layout.findViewById(R.id.endDate);
        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    private View.OnClickListener cancel_click = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();

        }
    };

    private void CreateProject() {

        project.setProjectName(projectName.getText().toString().trim());
        project.setProjectDescription(projectDesc.getText().toString().trim());
        project.setProjectStartDate(startDate.getText().toString().trim());
        project.setProjectEndDate(endDate.getText().toString().trim());


        //get user id
        String userId = firebaseAuth.getCurrentUser().getUid();
        String path = "Projects" + "/" + userId;
        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        String key = mDatabase.push().getKey();
        project.setProjectID(key);
        mDatabase.child(key).setValue(project);
        Toast.makeText(MainActivity.this, "Successfully created", Toast.LENGTH_LONG).show();
        pw.dismiss();


    }
    private void remove(Context context, int position) {
        String userId=firebaseAuth.getCurrentUser().getUid();
        Project project=projectList2.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project p = snapshot.getValue(Project.class);
                    if (project.getProjectID() == p.getProjectID()) {
                        Log.i("REMOVE-one", "project id"+p.getProjectID());
                        mDatabase.child(snapshot.getKey()).setValue(null);
                        adapter.notifyDataSetChanged();
                        adapter.updateList(projectList2);



                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(context, "removed item " + position, Toast.LENGTH_SHORT).show();
    }
    void logoutUser(){
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
