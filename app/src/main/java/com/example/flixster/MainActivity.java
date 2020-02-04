package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //Create the Adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a layout view.
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Async Internet handler.
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Success");
                //Create a JSONObject to hold the data we got from the server
                JSONObject jsonObject = json.jsonObject;
                //Try to parse through it.
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.e(TAG, "Results: " + results.toString());

                    //Calling our Movie.java class to handle the Parsing logic.
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //Key doesn't exist or other issue...
                    Log.e(TAG, "Oops.. JSON had a problem");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failure");
            }
        });
    }
}
