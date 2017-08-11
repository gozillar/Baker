package com.example.relearn.bakers.ui;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relearn.bakers.IdlingResource.SimpleIdlingResource;
import com.example.relearn.bakers.R;
import com.example.relearn.bakers.adapter.HomeAdapter;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.rest.ApiClient;
import com.example.relearn.bakers.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView errorTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArrayList<Integer> mImages;
    private ApiClient apiClient;
    protected ArrayList<Recipe> recipes = new ArrayList<>();

    protected HomeAdapter homeAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected LayoutManagerType currentLayoutManagerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_home);

        mImages = new ArrayList<>();
        mImages.add(R.drawable.nutella);
        mImages.add(R.drawable.brownies);
        mImages.add(R.drawable.cheesecake);
        mImages.add(R.drawable.yellowcake);

        apiClient = new ApiClient();
        recyclerView = (RecyclerView) findViewById(R.id.recipeRecyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        } else {
            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null  && connectivityManager.getActiveNetworkInfo().isConnected()){
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey("recipe_contents")) {
                    ArrayList<Recipe> recipess = savedInstanceState
                            .getParcelableArrayList("recipe_contents");
                    int scrollPos = savedInstanceState.getInt("position");
                    recyclerView.scrollToPosition(scrollPos);

                    homeAdapter = new HomeAdapter(recipess, mImages);
                    recyclerView.setAdapter(homeAdapter);
                }
            } else {
                homeAdapter = new HomeAdapter(recipes, mImages);
                recyclerView.setAdapter(homeAdapter);
                progressBar.setVisibility(View.VISIBLE);

                getIdlingResource();
                loadRecipeData();
            }
        }else {
            Snackbar snackbar = Snackbar.make(recyclerView, R.string.failedInternetRequest, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipeData();
            }
        });

    }

    void loadRecipeData() {
        if(mIdlingResource != null){
            mIdlingResource.setIdleState(false);
        }
        recyclerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        ApiInterface apiInterface = apiClient.getService();
        Call<List<Recipe>> recipeCall = apiInterface.getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();

                    showUIDataView();
                    homeAdapter.refresh((ArrayList<Recipe>) recipes);
                    if(mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }

                } else {
                    //Toast.makeText(HomeActivity.this, R.string.failedRequest, Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(recyclerView, R.string.failedRequest, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showUIErrorMessage();
            }
        });

    }

    private void showUIErrorMessage() {
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showUIDataView() {
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(this, SPAN_COUNT);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(this);
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(this);
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                loadRecipeData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int scrollPosition = 0;
//        ArrayList<Recipe> recipeContents = recipes;
        outState.putParcelableArrayList("recipe_contents", recipes);
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        outState.putInt("position", scrollPosition);
    }
}
