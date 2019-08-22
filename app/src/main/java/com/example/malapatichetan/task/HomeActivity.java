package com.example.malapatichetan.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.example.malapatichetan.task.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.master.glideimageview.GlideImageView;

import java.util.Date;
import java.util.HashSet;


public class HomeActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private FloatingActionButton fabbtn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText titleUp;
    private EditText noteUp;
    private Button btnDelete;
    private Button btnUpdate;

    private String title;
    private String note;
    private String postKey;
   // GlideImageView glideImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.loadUrl("https://www.google.com/");

        /*glideImageView = findViewById(R.id.glideImageview);
        glideImageView.loadImageUrl("https://www.pexels.com/photo/abandoned-forest-industry-nature-34950/");
*/
        toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Task App");

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);


        mDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setReverseLayout(true);
        LayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(LayoutManager);
        fabbtn = findViewById(R.id.fabbtn);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mydialouge = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
               View myview = inflater.inflate(R.layout.custominputfield,null);
                mydialouge.setView(myview);

                final AlertDialog dialouge = mydialouge.create();

                final EditText title = myview.findViewById(R.id.editTitle);
                final EditText note = myview.findViewById(R.id.editNote);
                Button btnSave = myview.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mtitle = title.getText().toString().trim();
                        String mnote = note.getText().toString().trim();
                        if (TextUtils.isEmpty(mtitle)){
                            title.setError("Required Field..");
                            return;
                        }
                        if (TextUtils.isEmpty(mnote)){
                            note.setError("Required Field..");
                            return;
                        }
                        String id = mDatabase.push().getKey();
                        String date = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mtitle,mnote,date,id);

                        mDatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_LONG).show();
                        dialouge.dismiss();
                    }
                });
                dialouge.show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.itemdata,
                        MyViewHolder.class,
                        mDatabase
                ) {

            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postKey= getRef(position).getKey();
                        title = model.getTitle();
                        note = model.getNote();
                        updateData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setTitle(String title){
            TextView mtitle = view.findViewById(R.id.title);
            mtitle.setText(title);
        }
        public void setNote(String note){
            TextView mnote = view.findViewById(R.id.note);
            mnote.setText(note);
        }
        public void setDate(String date){
            TextView mdate = view.findViewById(R.id.date);
            mdate.setText(date);
        }
    }

    public void updateData(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myview = inflater.inflate(R.layout.updateinputfield,null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        titleUp = myview.findViewById(R.id.editTitleUpd);
        noteUp = myview.findViewById(R.id.editNoteUpd);

        titleUp.setText(title);
        titleUp.setSelection(title.length());

        noteUp.setText(note);
        noteUp.setSelection(note.length());

        btnDelete = myview.findViewById(R.id.btnDeleteUpd);
        btnUpdate = myview.findViewById(R.id.btnUpdateUpd);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = titleUp.getText().toString().trim();
                note = noteUp.getText().toString().trim();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(title,note,mDate,postKey);
                mDatabase.child(postKey).setValue(data);
                dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(postKey).removeValue();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                alert.setMessage("Are you sure? "+mAuth.getCurrentUser().getEmail())
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        }).setNegativeButton("Cancel", null);
                AlertDialog alert1 = alert.create();
                alert1.show();
                break;

            case R.id.refresh:
                Toast.makeText(getApplicationContext(),"refreshed",Toast.LENGTH_LONG).show();
                break;

            case R.id.help:
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setTitle("How is it? ").setMessage("Was this helpful?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Thanks for your Feed back",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"We will make it better!!",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                break;

            case R.id.star:
                Toast.makeText(getApplicationContext(),"selected",Toast.LENGTH_LONG).show();
                break;

            case R.id.delete:
                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           mAuth.signOut();
                        }/*else{
                            displayMessage(getString(R.string.user_deletion_error));
                        }*/
                    }
                });

        }
        return super.onOptionsItemSelected(item);
    }

}
