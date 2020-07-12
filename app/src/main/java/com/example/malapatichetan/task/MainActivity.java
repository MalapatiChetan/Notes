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

public class MainActivity extends AppCompatActivity {

    private TextView signup;
    private EditText email;
    private EditText pass;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialouge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailEdit);
        pass = findViewById(R.id.passwordEdit);
        btnLogin = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signupText);
        mAuth = FirebaseAuth.getInstance();
        mDialouge = new ProgressDialog(this);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {                                    //User Login Into An App
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
                mDialouge.setMessage("Processing...");
                mDialouge.show();

                mAuth.signInWithEmailAndPassword(mEmail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {       //When a user signs in to your app, pass the user's email address and password to signInWithEmailAndPassword
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                              Toast.makeText(getApplicationContext()," Login sucessfull",Toast.LENGTH_LONG).show();
                              startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                              mDialouge.dismiss();
                        }else {
                              Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();
                              mDialouge.dismiss();
                        }
                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {                                      //SignUp For New Users
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });
    }
}
