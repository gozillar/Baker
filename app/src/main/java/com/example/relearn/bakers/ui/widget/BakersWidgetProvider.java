package com.example.relearn.bakers.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.relearn.bakers.R;
import com.example.relearn.bakers.model.Ingredient;
import com.example.relearn.bakers.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by relearn on 8/5/2017.
 */

public class BakersWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Recipe recipe;
    List<Ingredient> ingredients;
    ArrayList<Ingredient> ing;

    public BakersWidgetProvider(Context context, Intent intent) {
        mContext = context;
        recipe = intent.getParcelableExtra(Intent.EXTRA_TEXT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String preferenceString = sharedPreferences.getString(mContext.getResources().getString(R.string.listPreferencekey), mContext.getResources().getString(R.string.nutella_pie_key));

        int position;

        if (preferenceString == mContext.getResources().getString(R.string.nutella_pie_key)){
            position = 0;
        }else if (preferenceString == mContext.getResources().getString(R.string.Brownies_key)){
            position = 1;
        }else if (preferenceString == mContext.getResources().getString(R.string.yello_cake_key)){
            position = 2;
        }else{
            position = 3;
        }

        ingredients = recipe.getIngredients();

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        String ingredientss = ingredients.get(position).getIngredient();
        String measure = ingredients.get(position).getMeasure();
        double quantity = ingredients.get(position).getQuantity();

        views.setTextViewText(R.id.ingredientNames, ingredientss);
        views.setTextViewText(R.id.ingredientQuantitys, quantity + measure.toLowerCase() + "s");

        Intent intent = new Intent();
        intent.putExtra("position", position);
        views.setOnClickFillInIntent(R.id.recipeRecyclerView, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
