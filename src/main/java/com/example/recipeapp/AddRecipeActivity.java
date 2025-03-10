package com.example.recipeapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText titleEdit;
    private EditText ingredientsEdit;
    private EditText instructionsEdit;
    private Button selectImageButton;
    private ImageView imageView;
    private Button submitButton;
    private Uri selectedImage;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    imageView.setImageURI(uri);
                    selectedImage = uri;
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        titleEdit = findViewById(R.id.editTextTitle);
        ingredientsEdit = findViewById(R.id.editTextIngredients);
        instructionsEdit = findViewById(R.id.editTextInstructions);
        selectImageButton = findViewById(R.id.buttonSelectImage);
        imageView = findViewById(R.id.imageView);
        submitButton = findViewById(R.id.buttonSubmit);

        selectImageButton.setOnClickListener((v) -> {
            // Launch the photo picker and let the user choose only images.
            Object PickVisualMedia;
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        submitButton.setOnClickListener((v) -> {
            String title = titleEdit.getText().toString();
            String ingredients = ingredientsEdit.getText().toString();
            String instructions = instructionsEdit.getText().toString();
            if(title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || selectedImage == null){
                Toast.makeText(this, "All field must be filled!", Toast.LENGTH_LONG).show();
            } else {
                submit();
            }
        });
    }

    private void submit() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("/");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fileName = UUID.randomUUID().toString() + ".jpeg";

        storageRef.child(fileName)
                .putFile(selectedImage)
                .addOnSuccessListener(taskSnapshot -> {

                    Map<String, Object> recipe = new HashMap<>();
                    recipe.put("title", titleEdit.getText().toString());
                    recipe.put("ingredients", ingredientsEdit.getText().toString());
                    recipe.put("instructions", instructionsEdit.getText().toString());
                    recipe.put("imagePath", fileName);

                    db.collection("recipes")
                            .add(recipe)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Recipe was added successfully!", Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "An error occurred while inserting to db!", Toast.LENGTH_LONG).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "An error occurred while uploading image!", Toast.LENGTH_LONG).show();
                });
    }

}