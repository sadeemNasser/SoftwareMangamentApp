package com.example.softwaremangamentapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.softwaremangamentapp.Model.Project;
import com.example.softwaremangamentapp.Model.ProjectItem;
import com.example.softwaremangamentapp.Model.TaskInfo;
import com.example.softwaremangamentapp.Model.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;

public class TaskActivity extends AppCompatActivity {

    Switch status;
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
    private TextView duration;
    Double totalCost=0.0;
    private ListView list;
    Bundle extras;
    private ArrayAdapter adapter;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //status = itemView.findViewById(R.id.status);


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
        System.out.println("***********************");
        System.out.println(projecId);
        list = findViewById(R.id.list2);
        firebaseAuth = FirebaseAuth.getInstance();
        getTasks();

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

    public void getTasks(){

        String userId=firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId).child(projecId).child("Tasks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()){


                    TaskInfo task = child.getValue(TaskInfo.class);
//                    String prId=task.getProjectID();
//                    if(prId.equals(projecId))
                        TaskList.add(task.getTaskName());
                        totalCost+=task.getTaskCost();

                }
                ProjectCost.setText("Project Total cost: "+totalCost);
                viewTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview,TaskList);


    }

    private void viewTask() {
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTaskFromPosition(position);

            }
        });
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //startActivity(position);
    }

    public void getTaskFromPosition(final int taskPosition){

        //get user id
        String userId=firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId).child(projecId).child("Tasks");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {


                    TaskInfo task = child.getValue(TaskInfo.class);
//                    String prId=task.getProjectID();
//                    if(prId.equals(projecId))

                        taskInfos.add(task);

                }
                TaskInfo task2 = taskInfos.get(taskPosition);
                viewtask(task2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

//        holder.status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
//                builder1.setMessage("هل أنت متأكد أنه تم ايجاد المفقود وتريد إغلاق طلبك ؟");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "نعم",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                // dialog.cancel();
//
//                                databaseReferenceUserReport.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                            Report rep = snapshot.getValue(Report.class);
//                                            if (report.getDate() == rep.getDate()) {
//
//                                                databaseReferenceUserReport.child(snapshot.getKey()).child("ReportStatus").setValue("مغلق");
//                                                notifyDataSetChanged();
//                                                holder.status.setText("مغلق");
//                                                holder.status.setChecked(false);
//                                                holder.status.setEnabled(false);
//                                                holder.status.setAlpha((float) 0.5);
//
//
//                                            }
//
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                                Toast.makeText(context, "تم إغلاق بلاغك", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        });
//
//                builder1.setNegativeButton(
//                        "إلغاء الامر",
//                        (dialog, id) -> {
//                            holder.status.setChecked(true);
//                            dialog.cancel();
//                        });
//
//                AlertDialog alert11 = builder1.create();
//
//                alert11.show();
//                alert11.setCanceledOnTouchOutside(false);
//
//            }
//        });

        //        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(MyReport.this);
//                builder1.setMessage("هل أنت متأكد من حذف هذا البلاغ ؟");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "نعم",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                final int position = viewHolder.getAdapterPosition();
//                                deletedReport = newList.get(position);
//                                System.out.println("Here"+position);
//                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//
//
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                            Report rep = snapshot.getValue(Report.class);
//                                            if (deletedReport.getDate() == rep.getDate()) {
//                                                reference.child(snapshot.getKey()).removeValue();
//                                                newList.remove(position);
//                                                adapter.notifyItemRangeChanged(position,newList.size());
//                                                adapter.updateList(newList);
//
//                                                final Snackbar snackBar = Snackbar.make(recyclerView, "تم الحذف", Snackbar.LENGTH_LONG);
//                                                snackBar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
////                            snackBar.setAction("تراجع", v -> {
////                                snackBar.dismiss();
////                                findViewById(R.id.noReports).setVisibility(View.GONE);
////                                reference.child(snapshot.getKey()).setValue(deletedReport);
////                                newList.add(position, deletedReport);
////                                adapter.notifyItemInserted(position);
////                                adapter.updateList(newList);
//
//
//
//
//                                                // }).show();
//
//
//                                            }
//
//
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//
//                                });
//                            }
//
//                        });
//                builder1.setNegativeButton(
//                        "إلغاء الامر",
//                        (dialog, id) -> {
//                            adapter.updateList(newList);
//                            dialog.cancel();
//                        });
//
//                AlertDialog alert11 = builder1.create();
//
//                alert11.show();
//                alert11.setCanceledOnTouchOutside(false);}
//
//
//
//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MyReport.this, R.color.darkRed))
//                        .addActionIcon(R.drawable.ic_delete_black_24dp)
//                        .addSwipeRightLabel("حذف")
//                        .create()
//                        .decorate();
//
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };

    }

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
        task.setTaskCost(Double.parseDouble(taskCost.getText().toString().trim()));

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
