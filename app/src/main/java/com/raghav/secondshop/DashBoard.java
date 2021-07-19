package com.raghav.secondshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.raghav.secondshop.Model.Mainmodel;
import com.squareup.picasso.Picasso;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
private FloatingActionButton fbmain;
    private RecyclerView rc;
    private FirestoreRecyclerAdapter adapter;
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    TextView username;
    Toolbar toolbar;
    private String currentuserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        rc = findViewById(R.id.recyclerview_1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        rc.setLayoutManager(gridLayoutManager);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        fbmain=findViewById(R.id.main_fab);
        currentuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.header_username);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        rc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if(dy>0)
                    fbmain.hide();
                else
                    fbmain.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        fbmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashBoard.this,AddProductActivity.class);
                startActivity(i);
            }
        });
        Query q = db.collection("Products Category");

        FirestoreRecyclerOptions<Mainmodel> options = new FirestoreRecyclerOptions.Builder<Mainmodel>()
                .setQuery(q, Mainmodel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Mainmodel, ProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull final Mainmodel model) {
                Picasso.get().load(model.getImage()).into(holder.productimage);

                holder.pname.setText(model.getPname());
                holder.price.setText(model.getPrice());
                holder.description.setText(model.getDescription());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), ProductDetailActivity.class);
                        i.putExtra("image", model.getImage());
                        i.putExtra("price", model.getPrice());
                        i.putExtra("desc", model.getDescription());
                        i.putExtra("name", model.getPname());
                        i.putExtra("pid",model.getPid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainmodel, parent, false);
                return new ProductsViewHolder(view);
            }

        };

        rc.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_orders) {
//            Intent intent = new Intent(DashBoard.this, OrderHistoryActivity.class);
//            startActivity(intent);
        }else if (id == R.id.nav_myproducts) {
            Intent intent = new Intent(DashBoard.this, MyProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_mychats) {
            Intent intent = new Intent(DashBoard.this, ChatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_cart) {
            Intent intent = new Intent(DashBoard.this, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DashBoard.this, SettingActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashBoard.this, Otp1.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentuserID = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
        noteref = db.collection("users").document(currentuserID);
        noteref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(DashBoard.this, e.toString(), Toast.LENGTH_LONG).show();

                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String user = snapshot.getString("name");
                    username.setText(user);
                }
                else{
                    Toast.makeText(DashBoard.this, "Set username first", Toast.LENGTH_LONG).show();
                    SendUserToSettingActivity();
                }

            }
        });



    }

    private void SendUserToSettingActivity() {

        Intent i = new Intent(DashBoard.this, SettingActivity.class);
        startActivity(i);
    }
    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView pname, description, price;
        private ImageView productimage;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.pName);
            price = itemView.findViewById(R.id.pPrice);
            description = itemView.findViewById(R.id.pDescription);
            productimage = itemView.findViewById(R.id.image);
        }

    }
}