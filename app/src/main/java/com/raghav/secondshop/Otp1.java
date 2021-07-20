package com.raghav.secondshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class Otp1 extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText t1;
    private ProgressDialog loadingBar;
    private Button b1;


    SignInButton button;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp1);
        t1=findViewById(R.id.t1);
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        b1=(Button)findViewById(R.id.b1);

        loadingBar=new ProgressDialog(this);
        ccp.registerCarrierNumberEditText(t1);

        mAuth = FirebaseAuth.getInstance();

//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();

//       /**/ mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//
//            button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t1.getText().toString().isEmpty())
                {
                    Toast.makeText(Otp1.this, "Enter Valid Number pls...", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(Otp1.this, Otp2.class);
                    intent.putExtra("mobile", ccp.getFullNumberWithPlus().toString());
                    startActivity(intent);
                }
            }
        });


    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("success", "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("fail", "Google sign in failed", e);
//                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//    private void firebaseAuthWithGoogle(String idToken) {
//
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "signInWithCredential:success");
//                            startActivity(new Intent(MainActivity.this,Dashboard.class));
//                            //  updateUI(user);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Sign In Fail", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });
//
//
//
//    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent i=new Intent(Otp1.this, DashBoard.class);
            startActivity(i);

        }
    }


}
