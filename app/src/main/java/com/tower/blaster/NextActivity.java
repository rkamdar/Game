package com.tower.blaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


public class NextActivity extends Activity {

    ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        nextButton = (ImageButton) findViewById(R.id.nextButton);

        final Intent temp;
        if(result.equals("win")){
            temp = new Intent(this,MainActivity.class);
            if(intent.getBooleanExtra("mod5",false)){
                int mod = intent.getIntExtra("mod", 0);
                int level = intent.getIntExtra("level", 0);
                mod++;
                if(mod == 5){
                    mod = 1;
                    level++;
                }
                temp.putExtra("mod", mod);
                temp.putExtra("level", level);
                temp.putExtra("score", intent.getIntExtra("score", 0));
                temp.putExtra("mod5", true);
            } else {
                temp.putExtra("mod", intent.getIntExtra("mod", 0));
                temp.putExtra("level", intent.getIntExtra("level", 0) + 1);
                temp.putExtra("score", intent.getIntExtra("score", 0));
            }
        } else {
            temp = new Intent(this,ModActivity.class);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(temp);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
