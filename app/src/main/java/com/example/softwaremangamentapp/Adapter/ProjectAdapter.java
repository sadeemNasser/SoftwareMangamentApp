package com.example.softwaremangamentapp.Adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softwaremangamentapp.Model.Project;
import com.example.softwaremangamentapp.R;
import com.example.softwaremangamentapp.TaskActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ItemHolder> {
    Context context;
    private ArrayList<Project> items;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    private DatabaseReference mDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    Project project1;
    Project project2 = new Project();
    ArrayList<String> projectList = new ArrayList<String>();
    ArrayList<Project> projectList2 = new ArrayList<Project>();

    public ProjectAdapter(Context context, ArrayList<Project> items) {

        this.context = context;
        this.items = items;
    }
    ProjectAdapter( ArrayList<Project> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false));

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
         Project project = items.get(position);
        itemHolder.dragItem.setText(project.getProjectName());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void remove(Context context, int position) {
        String userId=firebaseAuth.getCurrentUser().getUid();
        Project project=items.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project p = snapshot.getValue(Project.class);
                    if (project.getProjectID() == p.getProjectID()) {
                        Log.i("REMOVE-one", "project id"+p.getProjectID());
                        mDatabase.child(snapshot.getKey()).setValue(null);
                        notifyDataSetChanged();



                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(context, "removed item " + position, Toast.LENGTH_SHORT).show();
    }

    private void open(Context context, int position) {
        Project project=items.get(position);
        viewProject(project);
    }
    public void getProjectFromPosition(final int projectPosition){

        //get user id
        String userId=firebaseAuth.getCurrentUser().getUid();
        String path = "Projects"+"/"+userId;

        DatabaseReference ref = database.getReference(path);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    project1 = child.getValue(Project.class);
                    projectList2.add(project1);
                }
                project2 = projectList2.get(projectPosition);
                viewProject(project2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    public void viewProject(Project project2){

        Intent i = (new Intent(context, TaskActivity.class));
        i.putExtra("projectId",project2.getProjectID());
        i.putExtra("projectName",project2.getProjectName());
        i.putExtra("sDate",project2.getProjectStartDate());
        i.putExtra("eDate",project2.getProjectEndDate());
        i.putExtra("projectD",project2.getProjectDescription());
        i.putExtra("totalCost",project2.getTotalCost());

        context.startActivity(i);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
                        open(itemView.getContext(), getAdapterPosition());
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
}

