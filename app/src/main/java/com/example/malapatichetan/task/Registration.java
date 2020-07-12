package com.example.malapatichetan.task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnReg;
    private TextView loginTxt;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialouge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDialouge = new ProgressDialog(this);
        email = findViewById(R.id.emailReg);
        pass = findViewById(R.id.passwordReg);
        btnReg = findViewById(R.id.RegBtn);
        loginTxt = findViewById(R.id.LogInText);


        loginTxt.setOnClickListener(new View.OnClickListener() {                                    // Login Into App
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {                                      // Registration For User
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mpass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mpass)){
                    pass.setError("Required Field..");
                    return;
                }

                mDialouge.setMessage("Processing...");                                              //progress of a task like you want user to wait until the task is completed
                mDialouge.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {  //To create a new user account with a password
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(getApplicationContext(),"sucessfull",Toast.LENGTH_LONG).show();
                              startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                              mDialouge.dismiss();
                          }else {
                              Toast.makeText(getApplicationContext(),"wrong user or password",Toast.LENGTH_LONG).show();
                              mDialouge.dismiss();
                          }
                    }
                });
            }
        });
    }
}
