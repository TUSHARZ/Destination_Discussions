package com.example.tushar.destinationdiscussions;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class splash extends AppCompatActivity {

    @BindView(R.id.animate)
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        YoYo.with(Techniques.Wobble).duration(3500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                startActivity(new Intent(splash.this,MainActivity.class));
            }
        }).playOn(layout);




    }
}
