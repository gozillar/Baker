package com.example.relearn.bakers.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.relearn.bakers.R;
import com.example.relearn.bakers.adapter.HomeAdapter;
import com.example.relearn.bakers.model.Recipe;
import com.example.relearn.bakers.rest.ApiClient;
import com.example.relearn.bakers.rest.ApiInterface;
import com.example.relearn.bakers.rest.NetworkUtils;
import com.example.relearn.bakers.ui.HomeActivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakersWidget extends AppWidgetProvider {

    private ApiClient apiClient = new ApiClient();

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(BakersWidget.class.getSimpleName(), "action received: " + action);
        super.onReceive(context, intent);
    }

    /*static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Recipe> recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakers_widgets);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preference = sharedPreferences.getString(context.getResources()
                .getString(R.string.listPreferencekey), context.getResources()
                .getString(R.string.nutella_pie_key));

        String name;
        if (preference == context.getResources().getString(R.string.nutella_pie_key)){
            name = "Nutella Pie";
        }else if (preference == context.getResources().getString(R.string.Brownies_key)){
            name = "Brownies";
        }else if (preference == context.getResources().getString(R.string.yello_cake_key)){
            name ="Yellow Cake";
        }else{
            name = "Cheese Cake";
        }

        views.setTextViewText(R.id.recipeNames, name);

        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.relativeLayout, pendingIntent);

        Intent intentService = new Intent(context, WidgetService.class);

        intentService.putParcelableArrayListExtra("arrayString", recipe);
        views.setRemoteAdapter(R.id.listViewss, intentService);
        //views.setRemoteAdapter(appWidgetId, R.id.listViewss, intentService);
        views.setPendingIntentTemplate(R.id.listViewss, pendingIntent);
        //views.setEmptyView(R.id.listViewss, R);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }*/

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String arrayString) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakers_widgets);

        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceString = sharedPreferences.getString(context.getResources().getString(R.string.listPreferencekey), context.getResources().getString(R.string.nutella_pie_key));

        String name;

        if (preferenceString == context.getResources().getString(R.string.nutella_pie_key)){
            name = "Nutella Pie";
        }else if (preferenceString == context.getResources().getString(R.string.Brownies_key)){
            name = "Brownies";
        }else if (preferenceString == context.getResources().getString(R.string.yello_cake_key)){
            name ="Yellow Cake";
        }else{
            name = "Cheese Cake";
        }

        views.setTextViewText(R.id.recipeName, name);
        Intent intent = new Intent(context,HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.relativeLayout, pendingIntent);

        Intent intentService = new Intent(context, WidgetService.class);

        intentService.putExtra("arrayString", arrayString);
        views.setRemoteAdapter(appWidgetId, R.id.listViewss, intentService);
        views.setPendingIntentTemplate(R.id.listViewss, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

/*
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (final int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);

            ApiInterface apiInterface = apiClient.getService();
            Call<List<Recipe>> recipeCall = apiInterface.getRecipes();

            recipeCall.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        List<Recipe> recipes = response.body();
                        ArrayList<Recipe> recipes1 = (ArrayList) recipes;
                        *//*RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.bakers_widget);

                        for (Recipe recipe : recipes) {
                            updateViews.setTextViewText(R.id.recipeNames, recipe.getName());
                            updateViews.setTextViewText(R.id.);
                        }*//*
                        String s= null;
                        updateAppWidget(context, appWidgetManager, appWidgetId, recipes1);




                    } else {
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_error_layout);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_error_layout);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            });
        }

    }*/

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (final int appWidgetId : appWidgetIds) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isNetworkAvailable = manager.getActiveNetworkInfo().isAvailable();
            if (isNetworkAvailable == false) {

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_error_layout);
                appWidgetManager.updateAppWidget(appWidgetId, views);

            }else {

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        URL url = NetworkUtils.getUrl();
                        String s = null;

                        try {
                            s = NetworkUtils.getResponseFromHttpUrl(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return s;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        updateAppWidget(context, appWidgetManager, appWidgetId, s);
                    }
                }.execute();
            }

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

