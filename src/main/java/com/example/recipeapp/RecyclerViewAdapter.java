package com.example.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    private Context mContext;
    private List<Recipe> mData = new ArrayList<>();
    public RecyclerViewAdapter(Context mContext){
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        LayoutInflater minflater= LayoutInflater.from(mContext);
        view = minflater.inflate(R.layout.cardview_recipe,viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        Recipe recipe = mData.get(i);

        myHolder.titleText.setText(recipe.getTitle());
        myHolder.ingredientsText.setText(recipe.getIngredients());
        Glide.with(mContext).load(recipe.getImageUrl()).into(myHolder.image);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext,RecipeActivity.class);
                intent.putExtra("recipe", recipe);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<Recipe> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

            TextView titleText;
            TextView ingredientsText;
            ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            titleText=(TextView) itemView.findViewById(R.id.textViewTitle);
            ingredientsText=(TextView) itemView.findViewById(R.id.textViewIngredients);
            image=(ImageView) itemView.findViewById(R.id.imageView);
        }

    }
}
