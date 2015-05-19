package com.tower.blaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Rushabh on 11/9/2014.
 */
public class ModActivity extends Activity{

    ImageView mod1,mod2,mod3,mod4,mod5;

    int width;
    int height;

    View.OnClickListener listner1;
    View.OnClickListener listner2;
    View.OnClickListener listner3;
    View.OnClickListener listner4;
    View.OnClickListener listner5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);

        listner1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mod", 1);
                startActivity(intent);
            }
        };

        listner2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mod", 2);
                startActivity(intent);
            }
        };

        listner3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mod", 3);
                startActivity(intent);
            }
        };

        listner4 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mod", 4);
                startActivity(intent);
            }
        };

        listner5 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mod", 5);
                startActivity(intent);
            }
        };

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        mod1 = (ImageView) findViewById(R.id.mod1);
        mod2 = (ImageView) findViewById(R.id.mod2);
        mod3 = (ImageView) findViewById(R.id.mod3);
        mod4 = (ImageView) findViewById(R.id.mod4);
        mod5 = (ImageView) findViewById(R.id.mod5);

        mod1.getLayoutParams().width = height * 2 / 3;
        mod2.getLayoutParams().width = height * 2 / 3;
        mod3.getLayoutParams().width = height * 2 / 3;
        mod4.getLayoutParams().width = height * 2 / 3;
        mod5.getLayoutParams().width = height * 2 / 3;

        mod1.getLayoutParams().height = height * 2 / 3;
        mod2.getLayoutParams().height = height * 2 / 3;
        mod3.getLayoutParams().height = height * 2 / 3;
        mod4.getLayoutParams().height = height * 2 / 3;
        mod5.getLayoutParams().height = height * 2 / 3;

        mod1.setOnClickListener(listner1);
        mod2.setOnClickListener(listner2);
        mod3.setOnClickListener(listner3);
        mod4.setOnClickListener(listner4);
        mod5.setOnClickListener(listner5);
    }
}
