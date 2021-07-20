package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.raghav.secondshop.Model.cartmodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rc;
    private FirebaseFirestore db;
    private String currentuserID;
    private FirestoreRecyclerAdapter adapter1;
    private ProgressDialog loadingBar;
    private String user, saveCurrentTime, productRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new ProgressDialog(this);

        currentuserID = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
        db = FirebaseFirestore.getInstance();

        rc = findViewById(R.id.recyclerview_Cart);
        DocumentReference noteref = db.collection("users").document(currentuserID);


        noteref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.getString("name");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        Query q = db.collection("CART OF" + currentuserID);

        FirestoreRecyclerOptions<cartmodel> options = new FirestoreRecyclerOptions.Builder<cartmodel>()
                .setQuery(q, cartmodel.class)
                .build();

        adapter1 = new FirestoreRecyclerAdapter<cartmodel, ProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, int position, @NonNull final cartmodel model) {
                holder.productname.setText(model.getProduct());
                holder.productprice.setText(model.getPrice());
                holder.location.setText(model.getLocation());
                holder.cart_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("CART OF" + currentuserID).document(model.getProduct()).delete();
                        Toast.makeText(CartActivity.this, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.ordernow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("Products Category").document(model.getPid()).get().
                                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        loadingBar.setTitle("Placing Order");
                                        loadingBar.setMessage("Dear User, please wait while we are processing your order.");
                                        loadingBar.setCanceledOnTouchOutside(false);
                                        loadingBar.show();

                                        HashMap<String, Object> productMap = new HashMap<>();

                                        productMap.put("username", user);
                                        productMap.put("pname", model.getProduct());
                                        productMap.put("price", model.getPrice());
                                        productMap.put("location", model.getLocation());
                                        productMap.put("pid", model.getPid());

                                        db.collection("Ordered By"+ currentuserID).document(model.getPid()).set(productMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(CartActivity.this, "Ordered Placed Successfully", Toast.LENGTH_LONG).show();
                                                        loadingBar.dismiss();
                                                        Intent i = new Intent(CartActivity.this, DashBoard.class);
                                                        startActivity(i);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CartActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                                        //  Toast.makeText(ty.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
//                                } catch (Exception e) {
//                                    Toast.makeText(CartActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//
//                                }
//                            } else {
//                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
//                            }
//                        }
                                        db.collection("CART OF" + currentuserID).document(model.getProduct()).delete();

                                    } else {
                                        Toast.makeText(CartActivity.this,"Product is Removed By Owner",Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(CartActivity.this,"Try Again Later",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
//                        Calendar calendar = Calendar.getInstance();
//                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//                        String saveCurrentDate = currentDate.format(calendar.getTime());
//                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//                        saveCurrentTime = currentTime.format(calendar.getTime());
//                        productRandomKey = saveCurrentDate + saveCurrentTime;

//                        sms = "Ordered by ->" + user + "\n"
//                                + "productName ->" + model.getProduct() + "\n"
//                                + "quantity ->" + model.getQuantity() + "\n"
//                                + "price ->" + model.getPrice() + "\n"
//                                + "Location ->" + model.getLocation();

//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                                try {
//                                    SmsManager sm = SmsManager.getDefault();
//
//                                    sm.sendTextMessage(Number, null, sms, null, null);


                    }
                });
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(CartActivity.this, ProductDetailActivity.class);
//
//                        startActivity(i);
//                    }
//                });

            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartdesign, parent, false);
                return new ProductsViewHolder(view);
            }

        };
        rc.setAdapter(adapter1);
        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView productname, productprice, cart_remove, location;
        private Button ordernow;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_remove = itemView.findViewById(R.id.cart_remove);
            ordernow = itemView.findViewById(R.id.cart_ordernow);
            productname = itemView.findViewById(R.id.cart_pName);
            productprice = itemView.findViewById(R.id.cart_pPrice);
            location = itemView.findViewById(R.id.cart_location);

        }
    }

}
