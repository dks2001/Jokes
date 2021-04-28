package com.example.jokes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arasthel.asyncjob.AsyncJob;
import com.example.jokes.controller.CardsDataAdapter;
import com.example.jokes.controller.JokeLikeListener;
import com.example.jokes.model.Joke;
import com.example.jokes.model.JokeManager;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardStack.CardEventListener, JokeLikeListener {

    CardStack mCardStack;
    CardsDataAdapter mCardAdapter;
    private List<Joke> allJokes = new ArrayList<>();


    private SensorManager sensorManager;
    private Sensor mAccellerometer;
    private ShakeDetector shakeDetector;

    private JokeManager jokeManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("jokes",loadJSONFromAsset());

        jokeManager = new JokeManager(this);

        mCardStack = (CardStack)findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.joke_card);
        mCardStack.setStackMargin(20);

        mCardAdapter = new CardsDataAdapter(this,0);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccellerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });



        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        try {
                            JSONObject rootObject = new JSONObject(loadJSONFromAsset());

                            JSONArray fatJokes = rootObject.getJSONArray("fat");
                            addJokeToArrayList(fatJokes,allJokes);

                            JSONArray stupidJokes = rootObject.getJSONArray("stupid");
                            addJokeToArrayList(stupidJokes,allJokes);

                            JSONArray uglyJokes = rootObject.getJSONArray("ugly");
                            addJokeToArrayList(uglyJokes,allJokes);

                            JSONArray nastyJokes = rootObject.getJSONArray("nasty");
                            addJokeToArrayList(nastyJokes,allJokes);

                            JSONArray hairyJokes = rootObject.getJSONArray("hairy");
                            addJokeToArrayList(hairyJokes,allJokes);

                            JSONArray baldJokes = rootObject.getJSONArray("bald");
                            addJokeToArrayList(baldJokes,allJokes);

                            JSONArray oldJokes = rootObject.getJSONArray("old");
                            addJokeToArrayList(oldJokes,allJokes);

                            JSONArray poorJokes = rootObject.getJSONArray("poor");
                            addJokeToArrayList(poorJokes,allJokes);

                            JSONArray shortJokes = rootObject.getJSONArray("short");
                            addJokeToArrayList(shortJokes,allJokes);

                            JSONArray skinnyJokes = rootObject.getJSONArray("skinny");
                            addJokeToArrayList(skinnyJokes,allJokes);

                            JSONArray tallJokes = rootObject.getJSONArray("tall");
                            addJokeToArrayList(tallJokes,allJokes);

                            JSONArray likeJokes = rootObject.getJSONArray("like");
                            addJokeToArrayList(likeJokes,allJokes);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        for(Joke joke: allJokes) {
                            mCardAdapter.add(joke.getJokeText());
                        }

                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();





        //mCardStack.setListener(this);
    }

    private void handleShakeEvent() {

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        Collections.shuffle(allJokes);
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        mCardAdapter.clear();
                        mCardAdapter = new CardsDataAdapter(MainActivity.this,0);
                        for(Joke joke: allJokes) {
                            mCardAdapter.add(joke.getJokeText());
                        }

                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("jokes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void addJokeToArrayList(JSONArray jsonArray, List<Joke> arrayList)
    {
        try {
            if(jsonArray !=null) {
                for(int i=0;i<jsonArray.length();i++) {
                    arrayList.add(new Joke(jsonArray.getString(i),false));
                }
            }

        } catch(Exception e)  {
            e.printStackTrace();
        }
    }

    @Override
    public boolean swipeEnd(int section, float distance) {
        return false;
    }

    @Override
    public boolean swipeStart(int section, float distance) {
        return false;
    }

    @Override
    public boolean swipeContinue(int section, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void discarded(int mIndex, int direction) {

    }

    @Override
    public void topCardTapped() {

    }

    @Override
    public void jokeIsLiked(Joke joke) {

        if(joke.isJokeLiked()) {
            jokeManager.saveJoke(joke);
        } else {
            jokeManager.deleteJoke(joke);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(MainActivity.this,FavJokeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(shakeDetector,mAccellerometer,SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}