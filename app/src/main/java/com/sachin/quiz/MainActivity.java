package com.sachin.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sachin.quiz.Adapters.CategoryAdapter;
import com.sachin.quiz.Models.CategoryModel;
import com.sachin.quiz.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CategoryAdapter adapter;
    ArrayList<CategoryModel> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = (DrawerLayout) findViewById(R.id.main);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        menu = findViewById(R.id.menu);

        header = navigationView.getHeaderView(0);

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();


        binding.shimmerDialog.startShimmer();
        binding.shimmerDialog.setVisibility(View.VISIBLE);
        binding.rvCategory.setVisibility(View.GONE);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list);
        binding.rvCategory.setAdapter(adapter);


        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    binding.shimmerDialog.stopShimmer();
                    binding.shimmerDialog.setVisibility(View.GONE);
                    binding.rvCategory.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                    binding.shimmerDialog.stopShimmer();
                    binding.shimmerDialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.shimmerDialog.stopShimmer();
                binding.shimmerDialog.setVisibility(View.GONE);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.share){
                    String shareBody = "Hye, I am Using best online quiz app"+"http://play.google.com/store/apps/details?id="+MainActivity.this.getPackageName();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.rate) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+MainActivity.this.getPackageName())));
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.privacy) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("Add Privacy Policy Link")));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (item.getItemId()==R.id.spinne) {
                    startActivity(new Intent(MainActivity.this, SpinnerActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }
}