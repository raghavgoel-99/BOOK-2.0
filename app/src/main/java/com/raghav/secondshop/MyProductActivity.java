package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.raghav.secondshop.Model.Mainmodel;
import com.raghav.secondshop.Model.cartmodel;
import com.raghav.secondshop.Model.myproductmodel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MyProductActivity extends AppCompatActivity {
    private RecyclerView myproduct_rc;
    private FirebaseFirestore db;
    private String currentuserID;
    private FirestoreRecyclerAdapter adapter;
    private ProgressDialog loadingBar;
    private String user, saveCurrentTime, productRandomKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        myproduct_rc=findViewById(R.id.recyclerview_my_product);
        toolbar.setTitle("My Products");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        currentuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query q = db.collection("users").document(currentuserID).collection("Products");

        FirestoreRecyclerOptions<myproductmodel> options = new FirestoreRecyclerOptions.Builder<myproductmodel>()
                .setQuery( q,myproductmodel.class ).build();

        adapter = new FirestoreRecyclerAdapter<myproductmodel,MyProductActivity.ProductsViewHolder1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyProductActivity.ProductsViewHolder1 holder,int position,@NonNull final myproductmodel model) {
                Picasso.get().load(model.getImage()).into(holder.myproduct_image);
                holder.time.setText(model.getTime());
                holder.date.setText(model.getDate());
                holder.pname.setText(model.getPname());
                holder.price.setText(model.getPrice());
                holder.description.setText(model.getDescription());
                holder.myproduct_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MyProductActivity.this,"Button clicked",Toast.LENGTH_SHORT).show();
                         db.collection("users").document(currentuserID).collection("Products").document(model.getPid()+currentuserID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 db.collection("Products Category").document(model.getPid()+currentuserID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         Toast.makeText(MyProductActivity.this,"Product Successfully Removed",Toast.LENGTH_SHORT).show();
                                     }
                                 })
                                         .addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull @NotNull Exception e) {
                                                 Toast.makeText(MyProductActivity.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                             }
                                         });
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull @NotNull Exception e) {
                                 Toast.makeText(MyProductActivity.this,"Try Again Later",Toast.LENGTH_SHORT).show();

                             }
                         });


                    }
                });
            }

            @NonNull
            @Override
            public MyProductActivity.ProductsViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_design, parent, false);
                return new ProductsViewHolder1(view);
            }

        };

        myproduct_rc.setAdapter(adapter);
        myproduct_rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.startListening();


    }
    private class ProductsViewHolder1 extends RecyclerView.ViewHolder {
        private TextView pname, description, price,date,time,myproduct_remove;
        private ImageView myproduct_image;

        public ProductsViewHolder1(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.myproduct_pName);
            price = itemView.findViewById(R.id.myproduct_pPrice);
            description = itemView.findViewById(R.id.myproduct_pDescription);
            myproduct_image= itemView.findViewById(R.id.myproduct_image);
            time = itemView.findViewById(R.id.myproduct_TimeAdded);
            date = itemView.findViewById(R.id.myproduct_DateAdded);
            myproduct_remove=itemView.findViewById(R.id.myproduct_remove);
        }

    }
}
