package com.raghav.secondshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    private EditText profile, status;
    private Button savebtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        profile = findViewById(R.id.username_settings);
        status = findViewById(R.id.Status_settings);
        savebtn = findViewById(R.id.save_settings_btn);


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserName = profile.getText().toString();
                String getUserBio = status.getText().toString();
                HashMap<String, Object> profilemap = new HashMap<>();
                profilemap.put("Uid", FirebaseAuth.getInstance().getCurrentUser()
                        .getUid());
                profilemap.put("name", getUserName);
                profilemap.put("status", getUserBio);

                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser()
                                .getUid()).set(profilemap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SettingActivity.this, "Registered Succesfully", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(SettingActivity.this,DashBoard.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteref = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser()
                .getUid());
        noteref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String user = documentSnapshot.getString("name");
                            String bio = documentSnapshot.getString("status");

                            profile.setText(user);
                            status.setText(bio);
                        } else {
                            Toast.makeText(SettingActivity.this, "set username ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
