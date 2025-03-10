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

//        recipe1.add(new Recipe("carrot cake","2 cups white sugar /n"+
//                "¼ cups vegetable oil/n"+
//                "4 eggs/n"+
//                "2 teaspoons vanilla extract/n"+
//                "2 cups all-purpose flour/n"+
//                "2 teaspoons baking soda/n"+
//                "2 teaspoons baking powder/"+
//                "2 teaspoons ground cinnamon/n"+
//                "½ teaspoon salt/n"+
//                "3 cups grated carrots/n"+
//                "1 cup chopped pecans","Method","/n" +
//                "Preheat oven to 350°F (175°C) and grease a cake pan.\n" +
//                "In a bowl, mix flour, baking powder, baking soda, cinnamon, and salt.\n" +
//                "In another bowl, whisk sugar, oil, and eggs until well combined.\n" +
//                "Gradually add dry ingredients to the wet mixture, stirring until smooth.\n" +
//                "Fold in grated carrots.\n" +
//                "Pour batter into the prepared pan and bake for 30-35 minutes.\n" +
//                "Let the cake cool before frosting./n",R.drawable.carrot));
//        recipe1.add(new Recipe("Donut","1 c. whole milk" +
//                "1/4 c. plus 1 tsp. granulated sugar, divided" +
//                "1 packet (or 2 1/4 tsp.) active dry yeast " +
//                "4 c. all-purpose flour, plus more if needed" +
//                "1/2 tsp. kosher salt" +
//                "6 tbsp. melted butter" +
//                "2 large eggs" +
//                "1/2 tsp. pure vanilla extract" +
//                "Canola or vegetable oil, for frying","Method","\n" +
//                "Grease a large bowl with cooking spray and set aside. In a small, microwave-safe bowl or glass measuring cup, add milk. Microwave until lukewarm, 40 seconds. Add a teaspoon of sugar and stir to dissolve, then sprinkle over yeast and let sit until frothy, about 8 minutes.\n" +
//                "Make glaze: In a large bowl, whisk together milk, powdered sugar, and vanilla until smooth. Set aside.\n" +
//                "Line a large baking sheet with paper towels. In a large dutch oven over medium heat, heat 2'' oil to 350°. Cook doughnuts, in batches, until deeply golden on both sides, about 1 minute per side. Holes will cook even faster!\n" +
//                "Transfer doughnuts to paper towel-lined baking sheet to drain and cool slightly. Dip into glaze, then place onto a cooling rack (or eat immediately!)",R.drawable.donut));
//
//        recipe1.add(new Recipe("Dosa","3 cups rice" +
//                "1 cup urad daal (split, skinless black gram)" +
//                "3/4 teaspoon fenugreek seeds" +
//                "Salt (to taste)" +
//                "Vegetable / canola / sunflower cooking oil","Method",
//                "Wash the rice and urad daal well. Add the fenugreek seeds to the mix and fill enough water in the rice-daal bowl to cover them about 2-inch deep. Soak overnight.\n" +
//                        "Put some cooking oil in a small bowl and keep ready. You will also need a bowl of ice cold water, a large, flat nonstick pan, 2 sheets of paper towel, a ladle, a spatula, and a basting brush.\n" +
//                        "When the upper surface begins to look cooked (it will no longer look soft or runny), flip the dosa. By this time, ideally, the surface that was underneath should be light golden in color. Cook for 1 minute after flipping.\n" +
//                        "The dosa is almost done. Fold it in half and allow to cook for 30 seconds more.",R.drawable.dosa));
//
//        recipe1.add(new Recipe("Pancake","1 1/4 cups milk" +
//                "1 egg" +
//                "3 tablespoons butter melted" +
//                "1 1/2 cups all-purpose flour" +
//                "3 1/2 teaspoons baking powder" +
//                "1 teaspoon salt" +
//                "1 tablespoon white sugar","Method",
//                "In a large bowl, sift together the flour, baking powder, salt and sugar. Make a well in the center and pour in the milk, egg and melted butter; mix until smooth." +
//                        "Heat a lightly oiled griddle or frying pan over medium high heat. Pour or scoop the batter onto the griddle, using approximately 1/4 cup for each pancake. Brown on both sides and serve hot.",R.drawable.pancake));
//
//        recipe1.add(new Recipe("Sorbe","2 cups frozen mango chunks" +
//                "1 cup frozen mixed berries (such as strawberries, blueberries, and raspberries)" +
//                "1/4 cup fresh lime juice" +
//                "1/3 cup honey or agave syrup" +
//                "1/2 cup cold water" ,"Method",
//                "Ace the frozen mango chunks and mixed berries in a blender.\n" +
//                        "Add fresh lime juice, honey or agave syrup, and cold water to the blender.\n" +
//                        "Blend the mixture until smooth. If it's too thick, you can add a little more water to reach your desired consistency.\n" +
//                        "Taste the sorbet mixture and adjust the sweetness or tartness by adding more honey or lime juice if needed.\n" +
//                        "Once blended, pour the sorbet mixture into a shallow dish or pan.\n" +
//                        "Cover and place it in the freezer for at least 4 hours or until it's firm.\n" +
//                        "Before serving, let the sorbet sit at room temperature for a few minutes to soften slightly.\n" +
//                        "Scoop the sorbet into bowls or cones, and enjoy your homemade mango-berry sorbet!",R.drawable.sorbe));

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
