package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp2 extends AppCompatActivity {
    private EditText t2;
    private Button b2;
    private String phonenumber,otpid;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp2);
        phonenumber =getIntent().getStringExtra("mobile").toString();
        t2=findViewById(R.id.t2);
        b2=(Button)findViewById(R.id.b2);
        mauth= FirebaseAuth.getInstance();
        initialotp();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t2.getText().toString().isEmpty()) {
                    Toast.makeText(Otp2.this, t2.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(Otp2.this, "Enter Otp pls...", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });



    }
    private void initialotp()
    {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks( new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;

                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Otp2.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(Otp2.this, "Wrong Otp ... Please Enter valid Otp", Toast.LENGTH_LONG).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//       PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60,TimeUnit.SECONDS,   (Activity) TaskExecutors.MAIN_THREAD,
//               new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                   @Override
//                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                       otpid=s;
//
//                   }
//                   @Override
//                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                       signInWithPhoneAuthCredential(phoneAuthCredential);
//
//                   }
//                   @Override
//                   public void onVerificationFailed(@NonNull FirebaseException e) {
//                       Toast.makeText(Otp.this,e.getMessage(), Toast.LENGTH_SHORT).show();
//                       Toast.makeText(Otp.this, "Wrong Otp ... Please Enter valid Otp", Toast.LENGTH_LONG).show();
//                   }
//               });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Otp2.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Otp2.this, DashBoard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        } else
                        {
                            String e=task.getException().toString();
                            Toast.makeText(Otp2.this,"Error: "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    }
