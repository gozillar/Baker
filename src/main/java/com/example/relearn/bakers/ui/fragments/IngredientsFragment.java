package com.example.relearn.bakers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.relearn.bakers.R;
import com.example.relearn.bakers.adapter.IngredientAdapter;
import com.example.relearn.bakers.model.Ingredient;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.ui.HomeActivity;

import java.util.List;

public class IngredientsFragment extends Fragment {

    Recipe recipe;
    List<Ingredient> ingredients;
    RecyclerView recyclerView;
    IngredientAdapter ingredientsAdapter;
    private static final String SAVED_LAYOUT_MANAGER = "classname.recycler.layout";
    Parcelable savedRecyclerLayoutState;
    protected RecyclerView.LayoutManager layoutManager;


    public IngredientsFragment() {

    }

    public static IngredientsFragment newInstance(Recipe recipe) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Intent.EXTRA_TEXT, recipe);
        ingredientsFragment.setArguments(args);
        return ingredientsFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        outstate.putInt(SAVED_LAYOUT_MANAGER, scrollPosition);
    }


//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//             savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
//            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Intent.EXTRA_TEXT);
            ingredients = recipe.getIngredients();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.ingredientsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            int scrollPos = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
            recyclerView.scrollToPosition(scrollPos);
        }

        ingredientsAdapter = new IngredientAdapter(ingredients);
        recyclerView.setAdapter(ingredientsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        int scrollposition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollposition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
            recyclerView.scrollToPosition(scrollposition);
        }
    }*/

}
