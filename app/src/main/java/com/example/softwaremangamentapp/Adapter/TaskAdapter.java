package com.example.softwaremangamentapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softwaremangamentapp.Model.TaskInfo;
import com.example.softwaremangamentapp.R;
import com.example.softwaremangamentapp.TaskItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerobranch.layout.SwipeLayout;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ItemHolder> {
    private Context context;
    private ArrayList<TaskInfo> items;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private String projectID="";
    private DatabaseReference mDatabase;
    private DatabaseReference MDatabase;
    private TaskInfo task;

    public TaskAdapter(Context context, ArrayList<TaskInfo> items,String id) {
        this.projectID=id;
        notifyDataSetChanged();
        this.context = context;
        this.items = items;
    }
    TaskAdapter( ArrayList<TaskInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_taskitem, viewGroup, false);
        return new ItemHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
         TaskInfo task = items.get(position);
        if (task.getStatues().equals("done")) {
            itemHolder.dragItem.setBackgroundResource(R.drawable.p);

        }
        itemHolder.dragItem.setText(task.getTaskName());

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void check(Context context, int position) {

        String userId=firebaseAuth.getCurrentUser().getUid();
        TaskInfo task=items.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId).child(projectID).child("Tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TaskInfo t = snapshot.getValue(TaskInfo.class);
                    if (task.getTaskName() == t.getTaskName()) {
                        mDatabase.child(snapshot.getKey()).child("statues").setValue("done");
                        notifyDataSetChanged();
                        updateList(items);




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }   private void remove(Context context, int position) {

        String userId=firebaseAuth.getCurrentUser().getUid();
        TaskInfo task=items.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId).child(projectID).child("Tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TaskInfo  t= snapshot.getValue(TaskInfo.class);
                    if (task.getTaskName() == t.getTaskName()) {
                        mDatabase.child(snapshot.getKey()).setValue(null);
                        notifyDataSetChanged();
                        updateList(items);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void open(Context context, int position) {
        TaskInfo task=items.get(position);
        viewTasks(task);
    }

    private void viewTasks(TaskInfo task){
        Intent i = (new Intent(context, TaskItem.class));
        i.putExtra("TaskName",task.getTaskName());
        i.putExtra("Tsdate",task.getTaskStartDate());
        i.putExtra("TeDate",task.getTaskEndDate());
        i.putExtra("TaskResource",task.getTaskResource());
        i.putExtra("TaskCost",task.getTaskCost());
        i.putExtra("TaskPid",task.getProjectID());

        context.startActivity(i);

    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView dragItem;
        ImageView leftView;
        ImageView rightView;
        SwipeLayout swipeLayout;

        ItemHolder(@NonNull final View itemView) {
            super(itemView);
            dragItem = itemView.findViewById(R.id.drag_item);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            leftView = itemView.findViewById(R.id.left_view);
            rightView = itemView.findViewById(R.id.right_view);
            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != NO_POSITION) {
                        remove(itemView.getContext(), getAdapterPosition());
                    }
                }
            });

            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != NO_POSITION) {
                        check(itemView.getContext(), getAdapterPosition());
                    }
                }
            });
            dragItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(itemView.getContext(), getAdapterPosition());

                }
            });
        }
    }
    public void updateList(ArrayList<TaskInfo> newList) {
        items = new ArrayList<>();
        items.addAll(newList);
        notifyDataSetChanged();


    }
}

