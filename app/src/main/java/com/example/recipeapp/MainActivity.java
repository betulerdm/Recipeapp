package com.example.recipeapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView myrecyclerView;
    private FloatingActionButton addButton;
    private RecyclerViewAdapter myAdapter;
    private List<Recipe> recipe1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerView_id);
        addButton = (FloatingActionButton) findViewById(R.id.addFAB);

        myAdapter = new RecyclerViewAdapter(this);
        myrecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        myrecyclerView.setAdapter(myAdapter);

        addButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivity(intent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipes")
                .addSnapshotListener((value, error) -> parseDocuments(value));
    }

    private int finishedRecipeCount = 0;

    private void parseDocuments(@Nullable QuerySnapshot value) {
        List<Recipe> recipeList = new ArrayList<>();

        List<DocumentSnapshot> docs = value.getDocuments();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("/");

        for (DocumentSnapshot doc : docs) {
            Recipe r = doc.toObject(Recipe.class);

            recipeList.add(r);

            storageRef.child(r.getImagePath()).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        r.setImageUrl(uri.toString());
                        if(++finishedRecipeCount == docs.size()) {
                            updateRecyclerViewData(recipeList);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MainActivity", e.toString());
                    });
        }
    }

    private void updateRecyclerViewData(List<Recipe> data) {
        myAdapter.updateData(data);
    }

}
