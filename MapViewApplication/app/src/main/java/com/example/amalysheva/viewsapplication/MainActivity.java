package com.example.amalysheva.viewsapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener, BalloonFragment.OnBalloonFragmentInteractionListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    public void onFragmentInteraction(String link, Integer id) {
        BalloonFragment fragment = (BalloonFragment) getFragmentManager()
                .findFragmentById(R.id.listFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link, id);
        }
    }


    @Override
    public void onShowBalloon(Integer integer) {
        MapFragment fragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        fragment.showBalloon(integer);
    }

    @Override
    public void onChangeText(Integer integer, String text) {
        MapFragment fragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        fragment.changeBalloon(integer, text);
    }

    @Override
    public void onDeleteBalloon(Integer integer) {
        MapFragment fragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        fragment.deleteBalloon(integer);
    }
}
