package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecipeActivity extends AppCompatActivity {

    private ImageView image;
    private TextView ingredientsText;
    private TextView instructionsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Recipe recipe = intent.getExtras().getParcelable("recipe");

        getSupportActionBar().setTitle(recipe.getTitle());

        image = (ImageView)findViewById(R.id.imageView);
        instructionsText = (TextView)findViewById(R.id.textViewInstructions);
        ingredientsText=(TextView)findViewById(R.id.textViewIngredients);


        ingredientsText.setText(recipe.getIngredients());
        instructionsText.setText(recipe.getInstructions());
        Glide.with(this).load(recipe.getImageUrl()).into(image);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}