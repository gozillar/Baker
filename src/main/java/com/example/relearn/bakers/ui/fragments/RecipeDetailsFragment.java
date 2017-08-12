package com.example.relearn.bakers.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.relearn.bakers.R;
import com.example.relearn.bakers.adapter.StepAdapter;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.model.Step;

import java.util.List;

public class RecipeDetailsFragment extends Fragment implements StepAdapter.ListItemClickListener {

    StepAdapter stepsAdapter;
    List<Step> stepsList;
    RecyclerView stepsRecyclerView;
    Recipe recipe;

    RecipeClickListener callback;

    public RecipeDetailsFragment() {

    }

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Intent.EXTRA_TEXT, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Intent.EXTRA_TEXT);
            stepsList = recipe.getSteps();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        CardView ingredientCardView = (CardView) view.findViewById(R.id.ingredientCardView);
        ingredientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onIngredientsSelected(v, recipe);
                }
            }
        });

        stepsRecyclerView = (RecyclerView) view.findViewById(R.id.stepsRV);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepAdapter(stepsList, this);
        stepsRecyclerView.setAdapter(stepsAdapter);
        /*stepsRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onStepSelected(stepsRecyclerView.getChildAdapterPosition(v));
            }
        });*/

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        stepsRecyclerView.addItemDecoration(itemDecoration);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeClickListener) {
            callback = (RecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement RecipeClickListener");
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        callback.onStepSelected(clickedItemIndex);
    }

    public interface RecipeClickListener {
        void onIngredientsSelected(View v, Recipe recipe);
        void onStepSelected(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}