package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private String productName, Description;
    private String Image, Price,ProductID;
    private ImageView ProductImage;
    private TextView pname, pprice, pdesc;
    private ImageView plus, minus;
    private Button addtocart, ordernow;
    private ProgressDialog loadingBar;
    private FirebaseFirestore db;
    private String location2, status,username,ownername;
    private String saveCurrentDate, saveCurrentTime, productRandomKey, currentuserID;
    private DocumentReference noteref,noteref2;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Product Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new ProgressDialog(this);
        ProductImage = findViewById(R.id.Detailimage);
        pname = findViewById(R.id.detailname);
        pprice = findViewById(R.id.detail_price);
        pdesc = findViewById(R.id.detail_description);
        addtocart = findViewById(R.id.Add_to_cart);
        ordernow = findViewById(R.id.Order_Now);
        productName = getIntent().getExtras().get("name").toString();
        Image = getIntent().getExtras().get("image").toString();
        Price = getIntent().getStringExtra("price");
        Description = getIntent().getExtras().get("desc").toString();
        ProductID=getIntent().getExtras().get("pid").toString();

        Toast.makeText(ProductDetailActivity.this,ProductID.substring(23),Toast.LENGTH_SHORT).show();
        pname.setText(productName);
        pprice.setText(Price);
        pdesc.setText(Description);
        Picasso.get().load(Image).into(ProductImage);


        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProductDetailActivity.this, ImageActivity.class);
                i.putExtra("IMAGE", Image);
                startActivity(i);
            }
        });
//        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        db = FirebaseFirestore.getInstance();

        currentuserID = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
        noteref = db.collection("users").document(currentuserID);

        noteref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            username = documentSnapshot.getString("name");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, "ERROR "+ e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        noteref2 = db.collection("users").document(ProductID.substring(23));

        noteref2.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            ownername = documentSnapshot.getString("name");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, "Product Not Exist "+ e.toString(), Toast.LENGTH_SHORT).show();
            }
        });



        Spinner location = findViewById(R.id.location);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.Spinner_items,
//                android.R.layout.simple_spinner_item
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        location.setAdapter(adapter);
//        location.setOnItemSelectedListener(getApplicationContext());

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CART();
            }
        });

        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> chatMap = new HashMap<>();
                chatMap.put("Product", productName);
                // productMap.put("username", Username.getText().toString());
                chatMap.put("price", Price);
                chatMap.put("status","Online");
                chatMap.put("name",ownername);
                chatMap.put("uid",ProductID.substring(23));
                db.collection("users").document(currentuserID).collection("chat").document(ProductID.substring(23)).set(chatMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                           Toast.makeText(ProductDetailActivity.this,"You Can now chat with owner in your Chat Section",Toast.LENGTH_SHORT).show();
                        }
                    });
                HashMap<String, Object> chatMap2 = new HashMap<>();
                chatMap.put("Product", productName);
                // productMap.put("username", Username.getText().toString());
                chatMap2.put("price", Price);
                chatMap2.put("status","Online");
                chatMap2.put("name", username);
                chatMap2.put("uid",currentuserID);
                db.collection("users").document(ProductID.substring(23)).collection("chat").document(currentuserID).set(chatMap2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProductDetailActivity.this,"succcessfully",Toast.LENGTH_SHORT).show();

                            }
                        });

//                    FirebaseDatabase.getInstance().getReference().child("Tokens").child("z6ZhzkOSQEfbIZQY7yTd5vlQi193").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            String usertoken = dataSnapshot.getValue(String.class);
//                            sendNotifications(usertoken,"ORDER", sms);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                productRandomKey = saveCurrentDate + saveCurrentTime;


//                sms = "Ordered by ->" + Username.getText().toString() + "\n"
//                        + "productName ->" + productName + "\n"
//                        + "quantity ->" + String.format("%d", q) + "\n"
//                        + "price ->" + String.format("%d", totalprice) + "\n"
//                        + "Location ->" + location2;
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                        sendSms();
//                    } else {
//                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
//                    }
//                }

            }
        });
    }

    private void CART() {
        try {
            loadingBar.setTitle("please wait ...");
            loadingBar.setMessage("ADDING TO CART");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("Product", productName);
            // productMap.put("username", Username.getText().toString());
            productMap.put("price", Price);
            productMap.put("location", location2);
            productMap.put("pid",ProductID);
            db.collection("CART OF" + currentuserID).document(productName).set(productMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProductDetailActivity.this, "Add to cart successful", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(ProductDetailActivity.this, CartActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();

                }
            });
            // Toast.makeText(ProductDetailActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ProductDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        location2 = adapterView.getSelectedItem().toString();
//        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }

    //    private void UpdateToken(){
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        String refreshToken= FirebaseInstanceId.getInstance().getToken();
//        Token token= new Token(refreshToken);
//        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
//    }
//
//    public void sendNotifications(String usertoken, String title, String message) {
//        Data data = new Data(title, message);
//        NotificationSender sender = new NotificationSender(data , usertoken);
//        apiService.sendNotifcation(sender).enqueue(new Callback<Myresponse>() {
//            @Override
//            public void onResponse(Call<Myresponse> call, Response<Myresponse> response) {
//                if (response.code() == 200) {
//                    if (response.body().success != 1) {
//                        Toast.makeText(ProductDetailActivity.this, "Failed ", Toast.LENGTH_LONG);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Myresponse> call, Throwable t) {
//
//            }
//        });
//    }
    private void sendSms() {
        try {
//                SmsManager sm = SmsManager.getDefault();
//
//                sm.sendTextMessage(Number, null, SMS, null, null);



            status = "In Processed";
            loadingBar.setTitle("Placing Order");
            loadingBar.setMessage("Dear User, please wait while we are processing your order.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("userPhone", currentuserID);
            productMap.put("pname", productName);
            productMap.put("price", Price);
            productMap.put("location", location2);
            productMap.put("image", Image);
            productMap.put("pid", productRandomKey);
            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("Status", status);
            productMap.put("username", username);

            db.collection("Admin View").document(productRandomKey + currentuserID).set(productMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(ProductDetailActivity.this, "Ordered Placed Successfully", Toast.LENGTH_LONG).show();
//                            loadingBar.dismiss();
//                            Intent i = new Intent(ProductDetailActivity.this, Product2Activity.class);
//                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();

                }
            });
            db.collection("Ordered By" + currentuserID).document(productRandomKey).set(productMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProductDetailActivity.this, "Ordered Placed Successfully", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(ProductDetailActivity.this, DashBoard.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();

                }
            });
            // Toast.makeText(ProductDetailActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ProductDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }
}
