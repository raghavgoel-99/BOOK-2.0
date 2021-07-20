package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.raghav.secondshop.Model.chatmodel;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    ImageView mimageviewofuser;
    FirestoreRecyclerAdapter<chatmodel,NoteViewHolder> chatAdapter;
    RecyclerView mrecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat Section");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        mrecyclerview=findViewById(R.id.recyclerview);


        // Query query=firebaseFirestore.collection("Users");
        Query query=firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("chat");
        FirestoreRecyclerOptions<chatmodel> allusername=new FirestoreRecyclerOptions.Builder<chatmodel>().setQuery(query,chatmodel.class).build();

        chatAdapter=new FirestoreRecyclerAdapter<chatmodel, NoteViewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull chatmodel chatmodel) {

                noteViewHolder.particularusername.setText(chatmodel.getName());
//                String uri=chatmodel.getImage();
//                if(!uri.isEmpty())
//                Picasso.get().load(uri).into(mimageviewofuser);
                if(chatmodel.getStatus().equals("Online"))
                {
                    noteViewHolder.statusofuser.setText(chatmodel.getStatus());
                }
                else
                {
                    noteViewHolder.statusofuser.setText(chatmodel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(android.R.color.darker_gray);
                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ChatActivity.this,specificchat.class);
                        intent.putExtra("name",chatmodel.getName());
                        intent.putExtra("receiveruid",chatmodel.getUid());
//                        intent.putExtra("imageuri",chatmodel.getImage());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                return new NoteViewHolder(view);
            }
        };


        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);

    }
    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            mimageviewofuser=itemView.findViewById(R.id.imageviewofuser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null)
        {
            chatAdapter.stopListening();
        }
    }

}