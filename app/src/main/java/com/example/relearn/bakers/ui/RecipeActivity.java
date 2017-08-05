package com.example.relearn.bakers.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.relearn.bakers.ui.fragments.IngredientsFragment;
import com.example.relearn.bakers.R;
import com.example.relearn.bakers.ui.fragments.RecipeDetailsFragment;
import com.example.relearn.bakers.ui.fragments.StepFragment;
import com.example.relearn.bakers.ui.fragments.VideoFragment;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.model.Step;

public class RecipeActivity extends AppCompatActivity implements RecipeDetailsFragment.RecipeClickListener, StepFragment.OnFragmentInteractionListener {

    Recipe recipe;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recipe = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        actionBar.setTitle(recipe.getName());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipeContainer, new RecipeDetailsFragment().newInstance(recipe))
                .commit();
    }

    @Override
    public void onIngredientsSelected(View v, Recipe recipe) {
        if (v.getId() == R.id.ingredientCardView) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeContainer, new IngredientsFragment().newInstance(recipe))
                    .addToBackStack(IngredientsFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onStepSelected(int position) {

        Step step = recipe.getSteps().get(position);
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        String sDescription = step.getShortDescription();
        bundle.putString("description", step.getDescription());
        bundle.putString("videoURL", step.getVideoURL());
        bundle.putString("thumbnailUrl", step.getThumbnailURL());

        if (bundle != null) {

            videoFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeContainer, videoFragment)
                    .addToBackStack(StepFragment.class.getSimpleName())
                    .commit();
            actionBar.setTitle(sDescription);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
