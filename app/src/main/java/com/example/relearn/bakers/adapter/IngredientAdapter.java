package com.example.relearn.bakers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.relearn.bakers.model.Ingredient;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter  extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private static final String TAG = IngredientAdapter.class.getSimpleName();
    private List<Ingredient> items = new ArrayList<>();

    public IngredientAdapter(List<Ingredient> arrayList) {
        this.items = arrayList;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card, parent, false);
        return new IngredientViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, int position) {

        Ingredient ingredients = (Ingredient) items.get(position);
        if (ingredients != null) {
            holder.ingredientName.setText(ingredients.getIngredient());
            holder.ingredientQuantity.setText(String.valueOf(ingredients.getQuantity()));
            holder.ingredientMeasure.setText(ingredients.getMeasure());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void refill(Recipe recipes, int type) {
        if (items != null) {
            items.clear();
        }
        items.addAll(recipes.getIngredients());
        notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientName, ingredientQuantity, ingredientMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredientName);
            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredientQuantity);
            ingredientMeasure = (TextView) itemView.findViewById(R.id.ingredientMeasure);
        }

    }

}