package com.london.gofor.insilocation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/4/29.
 */
public class ShowInMapActivity extends Activity{

    private ImageView zero;
    private ImageView startlocate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position);
        zero = (ImageView) findViewById(R.id.zero);
        startlocate = (ImageView) findViewById(R.id.startlocate);
        startlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start to locate
            }
        });
        startFlick(zero);
    }

    public void startFlick(View view) {
        if( null == view ){
            return;
        }

        Animation alphaAnimation = new AlphaAnimation( 1, 0 );
        alphaAnimation.setDuration( 300 );
        alphaAnimation.setInterpolator( new LinearInterpolator( ) );
        alphaAnimation.setRepeatCount( Animation.INFINITE );
        alphaAnimation.setRepeatMode( Animation.REVERSE );
        view.startAnimation( alphaAnimation );
    }

    public void stopFlick(View view) {
        if (null == view) return;
        view.clearAnimation();
    }
}
