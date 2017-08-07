package com.example.relearn.bakers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.relearn.bakers.R;
import com.example.relearn.bakers.ui.RecipeActivity;
import com.example.relearn.bakers.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Recipe> recipeList;
    ArrayList<Integer> mImages;

    public HomeAdapter(ArrayList<Recipe> arrayList, ArrayList<Integer> images) {
        recipeList = arrayList;
        mImages = images;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,
                parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {

        final Recipe recipes = recipeList.get(position);

        holder.recipeName.setText(recipes.getName());
        if (recipes.getImage() == "") {
            Glide.with(holder.itemView.getContext())
                    .load(mImages.get(position)).into(holder.recipeImage);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(recipes.getImage()).into(holder.recipeImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, recipes);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void refresh(ArrayList<Recipe> recipes) {
        if (recipeList != null) {
            recipeList.clear();
        }
        recipeList.addAll(recipes);
        notifyDataSetChanged();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeName;
        private ImageView recipeImage;

        public HomeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
        }

    }

}
