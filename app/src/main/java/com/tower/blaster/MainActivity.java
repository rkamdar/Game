package com.tower.blaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    Context context;

    private static final int NEWGAME = 0;
    private static final int PLAYER = 1;
    private static final int COMPUTER = 2;
    private static final int END = 3;
    private static final int IN_ANIMATION = 4;

    private static final int DELAY = 5000; //delay for boxAnimation
    private static final int AUTODELAY = 5000; //delay for autopilot

    private static final String type1 = "Click on the building\nblock you want to use.";
    private static final String type2 = "Where do you want\nto place this block?";
    private static final String type3 = "Wait for the Viking to\ntake their turn.";
    private static final String type4 = "Do you want to use this\nbuilding block?";

    int noOfBoxes;

    int TURN = -1;

    int width;
    int height;

    int level = 0;
    int score = 0;
    int mod = 0;

    ArrayList<Integer> remBox;
    ArrayList<Integer> myBox;
    ArrayList<Integer> comBox;

    ArrayList<Boolean> posBox;

    //for mod3
    ArrayList<Integer> remComBox;

    //for mod4
    ArrayList<Integer> myExtraBox;
    ArrayList<Integer> comExtraBox;
    ArrayList<Boolean> posExtraBox;

    Integer select;
    Integer random;

    //for mod3
    Integer selectCom;
    Integer randomCom;

    boolean randomBit = false;

    ImageView[] myBoxes = new ImageView[10];
    ImageView[] comBoxes = new ImageView[10];
    ImageView selBox;
    ImageView ranBox;

    //for mod3
    ImageView selComBox;
    ImageView ranComBox;

    //for mod4
    ImageView[] myExtraBoxes = new ImageView[10];
    ImageView[] comExtraBoxes = new ImageView[10];

    TextView result;
    Button newGame;

    TextView dummy; //to be deleteed

    ImageButton yesButton;
    ImageButton skipButton;
    TextView desc;

    //for mod3
    TextView desc2;

    RelativeLayout startScreen;
    RelativeLayout mainScreen;
    RelativeLayout endScreen;

    //for mod3
    RelativeLayout mod3Layout;

    //for mod4
    RelativeLayout comExtra;
    RelativeLayout myExtra;

    ImageView from;
    ImageView to;

    ImageButton startButton;

    Button nextButton;

    ImageView boy;
    ImageView boy2;
    ImageView boy3;
    ImageView scoreBoard;
    ImageView curtain;
    ImageView skull;

    Random ran = new Random();

    Animation boxAnim;
    Animation brokenbricks;
    AnimationDrawable boyAnimation;
    AnimationDrawable boyAnimation2;
    AnimationDrawable boyAnimation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.outer_layout);

        context = this;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        level = 0;
        score = 0;

        Intent intent = getIntent();
        mod = intent.getIntExtra("mod",0);

        initAnims();
        findViews();
        init(0);
        drawBitmaps();
        setClickListner();
        timerAnims();
    }

    // Start scheduled Animations at fixed rate of "DELAY"
    // myBoxes random
    // comBoxes random
    private void timerAnims() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(TURN != END) {
                            for (int j = 0; j < 10; j++) {
                                myBoxes[j].clearAnimation();
                                comBoxes[j].clearAnimation();
                                if(mod == 4){
                                    myExtraBoxes[j].clearAnimation();
                                    comExtraBoxes[j].clearAnimation();
                                }
                            }
                            int i = ran.nextInt(10);
                            myBoxes[i].startAnimation(boxAnim);
                            i = ran.nextInt(10);
                            comBoxes[i].startAnimation(boxAnim);

                            i = ran.nextInt(10);
                            myExtraBoxes[i].startAnimation(boxAnim);
                            i = ran.nextInt(10);
                            comExtraBoxes[i].startAnimation(boxAnim);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,DELAY);
    }

    // To Load Animation in Animation Object
    private void initAnims() {
        boxAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.boxanim);
        //brokenbricks = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.brokenbricks);
        brokenbricks = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.ABSOLUTE,height/2);
        brokenbricks.setDuration(1000);
        brokenbricks.setFillAfter(true);
    }

    // OnClickListners for all buttons
    private void setClickListner() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level++;
                init(level);
                drawBitmaps();
                result.setText("");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mod == 3){
                    mod3Layout.setVisibility(View.VISIBLE);
                    startAutoPilot();
                }
                startScreen.setVisibility(View.INVISIBLE);
                mainScreen.setVisibility(View.VISIBLE);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TURN = COMPUTER;

                desc.setText(type3);

                yesButton.setVisibility(View.GONE);
                skipButton.setVisibility(View.GONE);

                remBox.add(select);

                select = random;
                random = 0;

                drawRanBox(3);
                drawSelBox(2);

                doMove();

                randomBit = false;
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TURN = PLAYER;
                desc.setText(type2);
                for(int i=0; i<10; i++){
                    drawMyBox(i, 1);
                }
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level = 0;
                score = 0;
                init(level);
                drawBitmaps();
                result.setText("");
            }
        });

        ranBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TURN != END && (random == 0 || mod == 4) && TURN != PLAYER && TURN != IN_ANIMATION) {
                    if(mod != 4) {
                        int val = ran.nextInt(remBox.size());
                        int val2 = remBox.get(val);
                        remBox.remove(val);
                        random = val2;
                        drawRanBox(3);
                    }else{
                        TURN = PLAYER;
                        desc.setText(type2);
                        for(int i=0; i<10; i++){
                            drawMyBox(i, 1);
                            drawMyExtraBox(i, 1);
                        }
                        drawRanBox(1);
                    }
                    randomBit = true;
                    if(mod != 3 && mod != 4) {
                        yesButton.setVisibility(View.VISIBLE);
                        skipButton.setVisibility(View.VISIBLE);
                        desc.setText(type4);
                    }else if(mod == 3 && mod != 4){
                        TURN = PLAYER;
                        desc.setText(type2);
                        for(int i=0; i<10; i++){
                            drawMyBox(i, 1);
                        }
                        if(ran.nextInt(10)<4){
                            if(ran.nextInt(2) == 0){
                                whiteMagic();
                                skull.setImageDrawable(getResources().getDrawable(R.drawable.white_skull));
                                skull.setVisibility(View.VISIBLE);
                                TURN = IN_ANIMATION;
                                Handler myHandler = new Handler();
                                myHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TURN = PLAYER;
                                        skull.setVisibility(View.INVISIBLE);
                                    }
                                }, 1000);
                            }else{
                                redMagic();
                                whiteMagic();
                                skull.setImageDrawable(getResources().getDrawable(R.drawable.skull));
                                skull.setVisibility(View.VISIBLE);
                                TURN = IN_ANIMATION;

                                Handler myHandler = new Handler();
                                myHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TURN = PLAYER;
                                        skull.setVisibility(View.INVISIBLE);
                                    }
                                }, 1000);
                            }
                        }
                    }
                }
            }
        });

        selBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!randomBit && TURN != END && TURN != IN_ANIMATION) {
                    TURN = PLAYER;
                    drawSelBox(1);
                    for (int i = 0; i < 10; i++) {
                        if(mod == 4){
                            drawMyExtraBox(i, 1);
                        }
                        drawMyBox(i, 1);
                    }
                    desc.setText(type2);
                }
            }
        });

        for(int i=0; i<10; i++){
            myExtraBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selected = 0;
                    if(TURN == PLAYER) {
                        TURN = COMPUTER;
                        switch (view.getId()) {
                            case R.id.my11:
                                selected = 0;
                                break;
                            case R.id.my12:
                                selected = 1;
                                break;
                            case R.id.my13:
                                selected = 2;
                                break;
                            case R.id.my14:
                                selected = 3;
                                break;
                            case R.id.my15:
                                selected = 4;
                                break;
                            case R.id.my16:
                                selected = 5;
                                break;
                            case R.id.my17:
                                selected = 6;
                                break;
                            case R.id.my18:
                                selected = 7;
                                break;
                            case R.id.my19:
                                selected = 8;
                                break;
                            case R.id.my20:
                                selected = 9;
                                break;
                        }

                        int mul = 0;

                        if (randomBit) {
                            int tempval = myExtraBox.get(selected);
                            myExtraBox.set(selected, random);
                            mul = calcExtraMultiplier(selected);
                            score = score + (random * mul);
                            drawScoreBox();
                            random = tempval;
                            drawMyExtraBox(selected, 0);
                            drawRanBox(3);
                            drawSelBox(2);
                            randomBit = false;
                        } else {
                            int temp = select;
                            select = myExtraBox.get(selected);
                            myExtraBox.set(selected, temp);
                            mul = calcExtraMultiplier(selected);
                            score = score + (temp * mul);
                            drawScoreBox();
                            drawMyExtraBox(selected, 0);
                            drawSelBox(2);
                        }

                        for (int i = 0; i < 10; i++) {
                            drawMyBox(i, 0);
                            drawMyExtraBox(i, 0);
                        }

                        boyAnimation.stop();
                        boyAnimation.start();

                        boyAnimation2.stop();
                        boyAnimation2.start();

                        boyAnimation3.stop();
                        boyAnimation3.start();

                        checkMy();

                        desc.setText(type3);

                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 10; i++) {
                                    drawMyExtraBox(i, 0);
                                }
                                if(mod == 4){
                                    doMoveMod4();
                                }else {
                                    doMove();
                                }
                            }
                        }, 500 * (mul + 1));
                    }
                    }
            });
        }


        for (int i=0; i<10; i++){
            myBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selected = 0;

                    if(TURN == PLAYER) {
                        TURN = COMPUTER;
                        switch (view.getId()) {
                            case R.id.my1:
                                selected = 0;
                                break;
                            case R.id.my2:
                                selected = 1;
                                break;
                            case R.id.my3:
                                selected = 2;
                                break;
                            case R.id.my4:
                                selected = 3;
                                break;
                            case R.id.my5:
                                selected = 4;
                                break;
                            case R.id.my6:
                                selected = 5;
                                break;
                            case R.id.my7:
                                selected = 6;
                                break;
                            case R.id.my8:
                                selected = 7;
                                break;
                            case R.id.my9:
                                selected = 8;
                                break;
                            case R.id.my10:
                                selected = 9;
                                break;
                        }

                        int mul = 0;

                        if (randomBit) {
                            if(mod == 4){
                                int tempval = myBox.get(selected);
                                myBox.set(selected, random);
                                mul = calcMultiplier(selected);
                                score = score + (random * mul);
                                drawScoreBox();
                                random = tempval;
                                drawMyBox(selected, 0);
                                drawRanBox(3);
                                drawSelBox(2);
                                randomBit = false;
                            }else {
                                remBox.add(select);
                                select = myBox.get(selected);
                                myBox.set(selected, random);
                                mul = calcMultiplier(selected);
                                score = score + (random * mul);
                                drawScoreBox();
                                random = 0;
                                drawMyBox(selected, 0);
                                drawRanBox(3);
                                drawSelBox(2);
                                randomBit = false;
                                yesButton.setVisibility(View.GONE);
                                skipButton.setVisibility(View.GONE);
                            }
                        } else {
                            int temp = select;
                            select = myBox.get(selected);
                            myBox.set(selected, temp);
                            mul = calcMultiplier(selected);
                            score = score + (temp*mul);
                            drawScoreBox();
                            drawMyBox(selected, 0);
                            drawSelBox(2);
                        }

                        for(int i=0; i<10; i++){
                            drawMyBox(i, 0);
                            if(mod == 4){
                                drawMyExtraBox(i, 0);
                            }
                        }

                        boyAnimation.stop();
                        boyAnimation.start();

                        boyAnimation2.stop();
                        boyAnimation2.start();

                        boyAnimation3.stop();
                        boyAnimation3.start();

                        checkMy();

                        desc.setText(type3);

                        if(mod == 3){
                            for(int i=0; i<10; i++){
                                drawMyBox(i, 0);
                            }
                        }else {
                            Handler myHandler = new Handler();
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 10; i++) {
                                        drawMyBox(i, 0);
                                    }
                                    if(mod == 4){
                                        doMoveMod4();
                                    }else {
                                        doMove();
                                    }
                                }
                            }, 500 * (mul + 1));
                        }
                    }
                }
            });
        }
    }

    private void startAutoPilot() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(TURN != END && TURN != NEWGAME) {
                            doMoveMod3();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,AUTODELAY);
    }

    private void doMoveMod3() {
            for(int i=0; i<10; i++) {
                drawComBox(i, 2);
            }

            final int place = (int) ((selectCom - 1)*10 / noOfBoxes);
            int nextplace = 0;
            if( comBox.get(place) > selectCom){
                if(place != 0)
                    nextplace = place - 1;
                else
                    nextplace = 0;
            }
            if( comBox.get(place) < selectCom){
                if(place != 9)
                    nextplace = place + 1;
                else
                    nextplace = 9;
            }
            if(posBox.get(place) == false) {
                int temp = comBox.get(place);
                comBox.set(place, selectCom);
                selectCom = temp;
                posBox.set(place, true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        randomCom = 0;
                        drawSelComBox(2);
                        drawRanComBox(3);
                        drawComBox(place, 3);
                    }
                }, 1000);


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        checkCom();
                    }
                }, 2000);
            }
            else if(posBox.get(nextplace) == false){
                int temp = comBox.get(nextplace);
                comBox.set(nextplace, selectCom);
                selectCom = temp;
                posBox.set(nextplace, true);

                Handler handler6 = new Handler();
                final int finalNextplace = nextplace;
                handler6.postDelayed(new Runnable() {
                    public void run() {
                        randomCom = 0;
                        drawSelComBox(2);
                        drawRanComBox(3);
                        drawComBox(finalNextplace, 3);
                    }
                }, 1000);


                Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        checkCom();
                    }
                }, 2000);
            }
            else{
                if(ran.nextInt(10)<4){
                    if(ran.nextInt(2) == 0){
                        redMagic();
                        skull.setImageDrawable(getResources().getDrawable(R.drawable.red_skull));
                        skull.setVisibility(View.VISIBLE);
                        //TURN = IN_ANIMATION;
                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //TURN = PLAYER;
                                skull.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);
                    }else{
                        redMagic();
                        whiteMagic();
                        skull.setImageDrawable(getResources().getDrawable(R.drawable.skull));
                        skull.setVisibility(View.VISIBLE);
                        //TURN = IN_ANIMATION;

                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //TURN = PLAYER;
                                skull.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);
                    }
                }
                int val = ran.nextInt(remComBox.size());
                int val2 = remComBox.get(val);
                remComBox.remove(val);
                randomCom = val2;
                remComBox.add(selectCom);

                int place2 = (int) ((randomCom - 1)*10 / noOfBoxes);

                int lower = 1 + (int)(place2*noOfBoxes/10);
                int upper = (int)((place2+1)*noOfBoxes/10);

                if(posBox.get(place2) == false){
                    selectCom = comBox.get(place2);
                    comBox.set(place2, randomCom);
                    posBox.set(place2, true);
                }else if(comBox.get(place2)>=lower && comBox.get(place2)<=upper){
                    if(randomCom > comBox.get(place2) && place2<9){
                        place2++;
                    }
                    else if(place2>0 && place2<9){
                        place2--;
                    }
                    selectCom = comBox.get(place2);
                    comBox.set(place2, randomCom);
                }else{
                    selectCom = comBox.get(place2);
                    comBox.set(place2, randomCom);
                    posBox.set(place2, true);
                }

                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    public void run() {
                        drawRanComBox(3);
                    }
                }, 1000);

                Handler handler4 = new Handler();
                final int finalPlace = place2;
                handler4.postDelayed(new Runnable() {
                    public void run() {
                        randomCom = 0;
                        drawSelComBox(2);
                        drawRanComBox(3);
                        drawComBox(finalPlace, 3);
                    }
                }, 2000);

                Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        checkCom();
                    }
                }, 2500);
            }
    }

    private void redMagic() {
        for(int j=0; j<5; j++){
            int tempval = myBox.get(j);
            myBox.set(j,myBox.get(9-j));
            myBox.set(9-j,tempval);
        }
        for(int j=0; j<10; j++){
            drawMyBox(j,0);
        }
    }

    private void whiteMagic() {
        for(int j=0; j<5; j++){
            int tempval = comBox.get(j);
            comBox.set(j, comBox.get(9-j));
            comBox.set(9-j, tempval);
        }
        for(int j=0; j<10; j++){
            drawComBox(j,0);
        }
        for(int j=1; j<11; j++) {
            int lower = 1 + (int) ((j - 1) * noOfBoxes / 10);
            int upper = (int) (j * noOfBoxes / 10);
            if (comBox.get(j-1) >= lower && comBox.get(j-1) <= upper) {
                posBox.set(j-1,true);
            } else {
                posBox.set(j-1,false);
            }
        }
    }

    // Calculates Multiplier for scoring mechanism
    private int calcMultiplier(int position){
        int mul = 1;
        int pos = position;
        int upper;
        int lower;
        while(pos <= 8){
            if(myBox.get(pos) == (myBox.get(pos+1)-1)){
                mul++;
                pos = pos+1;
            }
            else
                break;
        }
        upper = pos;
        pos = position;
        while(pos >= 1){
            if(myBox.get(pos) == (myBox.get(pos-1)+1)){
                mul++;
                pos = pos-1;
            }
            else
                break;
        }
        lower = pos;

        if(mod != 3) {
            if (mul > 1) {
                int delay = 1;
                for (int i = lower; i <= upper; i++) {
                    Handler scoreHandler = new Handler();
                    final int finalI = i;
                    scoreHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawMyBox(finalI, 4); // 4 for score
                        }
                    }, 500 * delay);
                    delay++;
                }
            }
        }
        return mul;
    }

    //for mod4
    private int calcExtraMultiplier(int position){
        int mul = 1;
        int pos = position;
        int upper;
        int lower;
        while(pos <= 8){
            if(myExtraBox.get(pos) == (myExtraBox.get(pos+1)-1)){
                mul++;
                pos = pos+1;
            }
            else
                break;
        }
        upper = pos;
        pos = position;
        while(pos >= 1){
            if(myExtraBox.get(pos) == (myExtraBox.get(pos-1)+1)){
                mul++;
                pos = pos-1;
            }
            else
                break;
        }
        lower = pos;

        if(mod != 3) {
            if (mul > 1) {
                int delay = 1;
                for (int i = lower; i <= upper; i++) {
                    Handler scoreHandler = new Handler();
                    final int finalI = i;
                    scoreHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawMyExtraBox(finalI, 4); // 4 for score
                        }
                    }, 500 * delay);
                    delay++;
                }
            }
        }
        return mul;
    }

    // Check if Player wins
    private void checkMy() {

        int temp = myBox.get(0);
        for(int i=1; i<10; i++){
            if(temp < myBox.get(i)){
                temp = myBox.get(i);
            }
            else
                return;
        }
        if(mod == 4) {
            temp = myExtraBox.get(0);
            for (int i = 1; i < 10; i++) {
                if (temp < myExtraBox.get(i)) {
                    temp = myExtraBox.get(i);
                } else
                    return;
            }
        }
        TURN = END;
        result.setText("Player Wins");
        newGame.setVisibility(View.VISIBLE);
        mainScreen.setVisibility(View.INVISIBLE);
        endScreen.setVisibility(View.VISIBLE);
        curtain.setVisibility(View.INVISIBLE);

        //scoring need to be checked for mod4 -- baki
        score = score + ((level+1)*1000);
        score = score + (myBox.get(0)*100);
        drawScoreBox();

        doWinAnim();
    }

    private void doWinAnim() {
        final int[] j = {9};
        final Animation[] animation = new Animation[10];
        for(int i=9; i>=0; i--){
            animation[i] = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.ABSOLUTE,(((height-100)/10)*(9-i))-ran.nextInt((height-100)/10));
            animation[i].setDuration(((9-i)*200)+500);
            animation[i].setFillAfter(true);
        }

        for(int i=9; i>=0; i--){
        Handler handler = new Handler();
            final int finalI = i;
            handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawComBox(finalI,5);
                if(mod == 4){
                    drawComExtraBox(finalI,5);
                }
                comBoxes[finalI].startAnimation(animation[finalI]);
                if(mod == 4){
                    comExtraBoxes[finalI].startAnimation(animation[finalI]);
                }
            }
        },100*(10-i));
        }
    }

    // AI move
    private void doMove() {
        if(TURN == COMPUTER) {
            TURN = IN_ANIMATION;

            desc.setText(type3);

            for(int i=0; i<10; i++) {
                drawComBox(i, 2);
            }

            final int place = (int) ((select - 1)*10 / noOfBoxes);
            int nextplace = 0;
            if( comBox.get(place) > select){
                if(place != 0)
                    nextplace = place - 1;
                else
                    nextplace = 0;
            }
            if( comBox.get(place) < select){
                if(place != 9)
                    nextplace = place + 1;
                else
                    nextplace = 9;
            }
            if(posBox.get(place) == false) {
                int temp = comBox.get(place);
                comBox.set(place, select);
                select = temp;
                posBox.set(place, true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(place, 3);
                    }
                }, 1000);


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else if(posBox.get(nextplace) == false){
                int temp = comBox.get(nextplace);
                comBox.set(nextplace, select);
                select = temp;
                posBox.set(nextplace, true);

                Handler handler6 = new Handler();
                final int finalNextplace = nextplace;
                handler6.postDelayed(new Runnable() {
                    public void run() {
                        random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(finalNextplace, 3);
                    }
                }, 1000);


                Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else{
                int val = ran.nextInt(remBox.size());
                int val2 = remBox.get(val);
                remBox.remove(val);
                random = val2;
                remBox.add(select);

                int place2 = (int) ((random - 1)*10 / noOfBoxes);

                int lower = 1 + (int)(place2*noOfBoxes/10);
                int upper = (int)((place2+1)*noOfBoxes/10);

                if(posBox.get(place2) == false){
                    select = comBox.get(place2);
                    comBox.set(place2, random);
                    posBox.set(place2, true);
                }else if(comBox.get(place2)>=lower && comBox.get(place2)<=upper){
                    if(random > comBox.get(place2) && place2<9){
                        place2++;
                    }
                    else if(place2>0 && place2<9){
                        place2--;
                    }
                    select = comBox.get(place2);
                    comBox.set(place2, random);
                }else{
                    select = comBox.get(place2);
                    comBox.set(place2, random);
                    posBox.set(place2, true);
                }

                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    public void run() {
                        drawRanBox(3);
                    }
                }, 1000);

                Handler handler4 = new Handler();
                final int finalPlace = place2;
                handler4.postDelayed(new Runnable() {
                    public void run() {
                        random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(finalPlace, 3);
                    }
                }, 2000);

                Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 3000);
            }

            //TURN = PLAYER;
        }
    }

    //mod4 move
    private void doMoveMod4() {
        if(TURN == COMPUTER) {
            TURN = IN_ANIMATION;

            desc.setText(type3);

            for(int i=0; i<10; i++) {
                drawComBox(i, 2);
                drawComExtraBox(i, 2);
            }

            final int place = (int) ((select - 1)*10 / noOfBoxes);
            int nextplace = 0;
            if( comBox.get(place) > select){
                if(place != 0)
                    nextplace = place - 1;
                else
                    nextplace = 0;
            }
            if( comBox.get(place) < select){
                if(place != 9)
                    nextplace = place + 1;
                else
                    nextplace = 9;
            }
            int nextplace2 = 0;
            if( comExtraBox.get(place) > select){
                if(place != 0)
                    nextplace2 = place - 1;
                else
                    nextplace2 = 0;
            }
            if( comExtraBox.get(place) < select){
                if(place != 9)
                    nextplace2 = place + 1;
                else
                    nextplace2 = 9;
            }
            if(posBox.get(place) == false) {
                int temp = comBox.get(place);
                comBox.set(place, select);
                select = temp;
                posBox.set(place, true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(place, 3);
                    }
                }, 1000);


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                            drawComExtraBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else if(posExtraBox.get(place) == false){
                int temp = comExtraBox.get(place);
                comExtraBox.set(place, select);
                select = temp;
                posExtraBox.set(place, true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComExtraBox(place, 3);
                    }
                }, 1000);


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                            drawComExtraBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else if(posBox.get(nextplace) == false){
                int temp = comBox.get(nextplace);
                comBox.set(nextplace, select);
                select = temp;
                posBox.set(nextplace, true);

                Handler handler6 = new Handler();
                final int finalNextplace = nextplace;
                handler6.postDelayed(new Runnable() {
                    public void run() {
                        //random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(finalNextplace, 3);
                    }
                }, 1000);


                Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                            drawComExtraBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else if(posExtraBox.get(nextplace2) == false){
                int temp = comExtraBox.get(nextplace2);
                comExtraBox.set(nextplace2, select);
                select = temp;
                posBox.set(nextplace2, true);

                Handler handler6 = new Handler();
                final int finalNextplace = nextplace2;
                handler6.postDelayed(new Runnable() {
                    public void run() {
                        //random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComExtraBox(finalNextplace, 3);
                    }
                }, 1000);


                Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                            drawComExtraBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 2000);
            }
            else{
                //int val = ran.nextInt(remBox.size());
                //int val2 = remBox.get(val);
                //remBox.remove(val);
                //random = val2;
                //remBox.add(select);

                int place2 = (int) ((random - 1)*10 / noOfBoxes);

                int lower = 1 + (int)(place2*noOfBoxes/10);
                int upper = (int)((place2+1)*noOfBoxes/10);

                if(posBox.get(place2) == false){
                    int tempval = random;
                    random = comBox.get(place2);
                    comBox.set(place2, tempval);
                    posBox.set(place2, true);
                }
                else if(posExtraBox.get(place2) == false){
                    int tempval = random;
                    random = comExtraBox.get(place2);
                    comExtraBox.set(place2, tempval);
                    posExtraBox.set(place2, true);
                }
                else if(!(comBox.get(place2)>=lower && comBox.get(place2)<=upper)){
                    int tempval = random;
                    random = comBox.get(place2);
                    comBox.set(place2, tempval);
                    posBox.set(place2, true);
                }
                else if(!(comExtraBox.get(place2)>=lower && comExtraBox.get(place2)<=upper)){
                    int tempval = random;
                    random = comExtraBox.get(place2);
                    comExtraBox.set(place2, tempval);
                    posExtraBox.set(place2, true);
                }
                else{
                    if(random > comBox.get(place2) && place2<9){
                        place2++;
                    }
                    else if(place2>0 && place2<9){
                        place2--;
                    }
                    int tempval = random;
                    random = comBox.get(place2);
                    comBox.set(place2, tempval);
                }

                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    public void run() {
                        drawRanBox(3);
                    }
                }, 1000);

                Handler handler4 = new Handler();
                final int finalPlace = place2;
                handler4.postDelayed(new Runnable() {
                    public void run() {
                        //random = 0;
                        drawSelBox(2);
                        drawRanBox(3);
                        drawComBox(finalPlace, 3);
                    }
                }, 2000);

                Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0; i<10; i++) {
                            drawComBox(i, 0);
                            drawComExtraBox(i, 0);
                        }
                        desc.setText(type1);
                        TURN = COMPUTER;
                        checkCom();
                    }
                }, 3000);
            }

            //TURN = PLAYER;
        }
    }

    // Check if Computer wins
    private void checkCom() {
        int temp = comBox.get(0);
        for(int i=1; i<10; i++){
            if(temp < comBox.get(i)){
                temp = comBox.get(i);
            }
            else
                return;
        }
        if(mod == 4){
            temp = comExtraBox.get(0);
            for(int i=1; i<10; i++){
                if(temp < comExtraBox.get(i)){
                    temp = comExtraBox.get(i);
                }
                else
                    return;
            }
        }
        TURN = END;
        result.setText("Computer Wins");
        newGame.setVisibility(View.VISIBLE);
        curtain.setVisibility(View.INVISIBLE);

        doLoseAnim();
    }

    private void doLoseAnim() {
        final int[] j = {9};
        final Animation[] animation = new Animation[10];
        for(int i=9; i>=0; i--){
            animation[i] = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.ABSOLUTE,(((height-100)/10)*(9-i))-ran.nextInt((height-100)/10));
            animation[i].setDuration(((9-i)*200)+500);
            animation[i].setFillAfter(true);
            animation[i].setRepeatCount(0);
        }

        for(int i=9; i>=0; i--){
            Handler handler = new Handler();
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawMyBox(finalI, 5);
                    myBoxes[finalI].startAnimation(animation[finalI]);
                    if(mod == 4){
                        drawMyExtraBox(finalI, 5);
                        myExtraBoxes[finalI].startAnimation(animation[finalI]);
                    }
                }
            },100*(10-i));
        }
    }

    // Draw all Bitmaps (only called when init / new level)
    private void drawBitmaps() {
        for(int i=0; i<10; i++){
            drawMyBox(i, 0);
            if(mod == 4){
                drawMyExtraBox(i, 0);
            }
        }

        for(int i=0; i<10; i++){
            drawComBox(i, 0);
            if(mod == 4){
                drawComExtraBox(i, 0);
            }
        }

        drawSelBox(2);

        drawRanBox(3);

        from.setImageBitmap(DrawBox.drawBit(context, (height - 100) / 8, (height - 100) / 8, 1, 2));

        to.setImageBitmap(DrawBox.drawBit(context, (height-100)/8, (height-100)/8, noOfBoxes, 2));

        drawScoreBox();

        if(mod == 3) {
            drawSelComBox(2);
            drawRanComBox(3);
        }
    }

    // Draw score depending on "score"
    private void drawScoreBox() {
        scoreBoard.setImageBitmap(DrawBox.drawScore(context, (width/3), (height - 100)/8, score));
    }

    // Draw MyBox at "place"
    // for: "selected"
    // 0 - No Arrow
    // 1 - Forward Arrow
    // 4 - Score blast
    // 5 - broken
    private void drawMyBox(int place, int selected){
        if(mod == 4){
            myBoxes[place].setImageBitmap(DrawBox.draw(context, width / 5, (height - 100) / 10, myBox.get(place), selected));
        }else {
            myBoxes[place].setImageBitmap(DrawBox.draw(context, width * 3 / 9, (height - 100) / 10, myBox.get(place), selected));
        }
    }
    private void drawMyExtraBox(int place, int selected){
        myExtraBoxes[place].setImageBitmap(DrawBox.draw(context, width / 5, (height - 100) / 10, myExtraBox.get(place), selected));
    }

    // Draw ComBox at "place"
    // for: "selected"
    // 0 - No Arrow
    // 2 - Backward Arrow
    // 3 - Backward Arrow selected
    // 4 - blast
    // 5 - broken
    private void drawComBox(int place, int selected){
        if(mod == 4){
            comBoxes[place].setImageBitmap(DrawBox.draw(context, width / 5, (height - 100) / 10, comBox.get(place), selected));
        }else {
            comBoxes[place].setImageBitmap(DrawBox.draw(context, width * 3 / 9, (height - 100) / 10, comBox.get(place), selected));
        }
    }
    private void drawComExtraBox(int place, int selected){
        comExtraBoxes[place].setImageBitmap(DrawBox.draw(context, width / 5, (height - 100) / 10, comExtraBox.get(place), selected));
    }
    // Draw "random" number Box
    // random 0: "?"
    // for: "selected"
    // 3 - random_brick style
    private void drawRanBox(int selected) {
        ranBox.setImageBitmap(DrawBox.drawBit(context, (height-100)/8, (height-100)/8, random, selected));
    }

    private void drawRanComBox(int selected) {
        ranComBox.setImageBitmap(DrawBox.drawBit(context, (height-100)/8, (height-100)/8, randomCom, selected));
    }

    // Draw "select" number Box
    // for: "selected"
    // 2 - select_brick not selected
    // 1 - select_brick selected
    private void drawSelBox(int selected) {
        selBox.setImageBitmap(DrawBox.drawBit(context, (height-100)/8, (height-100)/8, select, selected));
    }

    private void drawSelComBox(int selected){
        selComBox.setImageBitmap(DrawBox.drawBit(context, (height-100)/8, (height-100)/8, selectCom, selected));
    }

    // Initialization for all
    private void init(int level) {
        if(mod == 5)
            level = 7;
        switch (level){
            case 0: noOfBoxes = 50; break;
            case 1: noOfBoxes = 45; break;
            case 2: noOfBoxes = 42; break;
            case 3: noOfBoxes = 38; break;
            case 4: noOfBoxes = 35; break;
            case 5: noOfBoxes = 32; break;
            case 6: noOfBoxes = 30; break;
            case 7: noOfBoxes = 22; break;
            default: noOfBoxes = 25; break;
        }

        if(mod == 4){
            noOfBoxes = 42;
        }

        TURN = NEWGAME;

        remBox = new ArrayList<Integer>();
        myBox = new ArrayList<Integer>();
        comBox = new ArrayList<Integer>();

        posBox = new ArrayList<Boolean>();

        //for mod4
        myExtraBox = new ArrayList<Integer>();
        comExtraBox = new ArrayList<Integer>();
        posExtraBox = new ArrayList<Boolean>();

        remBox.clear();
        myBox.clear();
        comBox.clear();

        posBox.clear();

        //for mod4
        myExtraBox.clear();
        comExtraBox.clear();
        posExtraBox.clear();

        //for mod3
        remComBox = new ArrayList<Integer>();

        remComBox.clear();

        for(int i=1; i<(noOfBoxes+1); i++){
            remBox.add(i);
        }

        //for mod3
        for(int i=1; i<(noOfBoxes+1); i++){
            remComBox.add(i);
        }

        for(int j=1; j<11; j++){
            int val = ran.nextInt(remBox.size());
            int value = remBox.get(val);
            remBox.remove(val);
            myBox.add(value);
        }

        //for mod4
        if(mod == 4) {
            for (int j = 1; j < 11; j++) {
                int val = ran.nextInt(remBox.size());
                int value = remBox.get(val);
                remBox.remove(val);
                myExtraBox.add(value);
            }
        }

        if(mod == 3){
            for(int j=1; j<11; j++){
                int val = ran.nextInt(remComBox.size());
                int value = remComBox.get(val);
                remComBox.remove(val);
                comBox.add(value);
                int lower = 1 + (int) ((j - 1) * noOfBoxes / 10);
                int upper = (int) (j * noOfBoxes / 10);
                if (value >= lower && value <= upper) {
                    posBox.add(true);
                } else {
                    posBox.add(false);
                }
            }

            int val = ran.nextInt(remComBox.size());
            int value = remComBox.get(val);
            remComBox.remove(val);
            selectCom = value;

            randomCom = 0;
        }
        else {
            for (int j = 1; j < 11; j++) {
                int val = ran.nextInt(remBox.size());
                int value = remBox.get(val);
                remBox.remove(val);
                comBox.add(value);
                int lower = 1 + (int) ((j - 1) * noOfBoxes / 10);
                int upper = (int) (j * noOfBoxes / 10);
                if (value >= lower && value <= upper) {
                    posBox.add(true);
                } else {
                    posBox.add(false);
                }
            }

            if(mod == 4){
                for (int j = 1; j < 11; j++) {
                    int val = ran.nextInt(remBox.size());
                    int value = remBox.get(val);
                    remBox.remove(val);
                    comExtraBox.add(value);
                    int lower = 1 + (int) ((j - 1) * noOfBoxes / 10);
                    int upper = (int) (j * noOfBoxes / 10);
                    if (value >= lower && value <= upper) {
                        posExtraBox.add(true);
                    } else {
                        posExtraBox.add(false);
                    }
                }
            }
        }

        int val = ran.nextInt(remBox.size());
        int value = remBox.get(val);
        remBox.remove(val);
        select = value;

        random = 0;

        if(mod == 4){
            random = remBox.get(0);
            remBox.remove(0);
        }

        newGame.setVisibility(View.INVISIBLE);

        yesButton.setVisibility(View.GONE);

        skipButton.setVisibility(View.GONE);

        yesButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-100)/10));
        skipButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-100)/10));

        //yesButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)((height-100)/25));
        //yesButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) ((height - 100) / 25));
        //skipButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)((height-100)/25));

        desc.setText(type1);

        startScreen.setVisibility(View.VISIBLE);
        mainScreen.setVisibility(View.INVISIBLE);
        endScreen.setVisibility(View.INVISIBLE);

        boy.setBackgroundResource(R.drawable.boy_anim);
        boyAnimation = (AnimationDrawable) boy.getBackground();

        boy2.setBackgroundResource(R.drawable.boy_anim2);
        boyAnimation2 = (AnimationDrawable) boy2.getBackground();

        boy3.setBackgroundResource(R.drawable.boy_anim3);
        boyAnimation3 = (AnimationDrawable) boy3.getBackground();

        boy.getLayoutParams().height = (int) (1.5 * (height - 100) / 10);
        boy.getLayoutParams().width = (int) (1.5 * (height - 100) / 10);

        boy2.getLayoutParams().height = (int) (1.5 * (height - 100) / 10);
        boy2.getLayoutParams().width = (int) (1.5 * (height - 100) / 10);

        boy3.getLayoutParams().height = (int) (1.5 * (height - 100) / 10);
        boy3.getLayoutParams().width = (int) (1.5 * (height - 100) / 10);

        startButton.getLayoutParams().height = (height - 100)/10;

        if(mod == 1)
            curtain.setVisibility(View.INVISIBLE);
        else if(mod == 2)
            curtain.setVisibility(View.VISIBLE);
        else
            curtain.setVisibility(View.INVISIBLE);

        //for mod4
        if(mod == 4) {
            myExtra.setVisibility(View.VISIBLE);
            comExtra.setVisibility(View.VISIBLE);
        }
    }

    // findViewById for all views
    private void findViews() {
        myBoxes[0] = (ImageView) findViewById(R.id.my1);
        myBoxes[1] = (ImageView) findViewById(R.id.my2);
        myBoxes[2] = (ImageView) findViewById(R.id.my3);
        myBoxes[3] = (ImageView) findViewById(R.id.my4);
        myBoxes[4] = (ImageView) findViewById(R.id.my5);
        myBoxes[5] = (ImageView) findViewById(R.id.my6);
        myBoxes[6] = (ImageView) findViewById(R.id.my7);
        myBoxes[7] = (ImageView) findViewById(R.id.my8);
        myBoxes[8] = (ImageView) findViewById(R.id.my9);
        myBoxes[9] = (ImageView) findViewById(R.id.my10);

        comBoxes[0] = (ImageView) findViewById(R.id.com1);
        comBoxes[1] = (ImageView) findViewById(R.id.com2);
        comBoxes[2] = (ImageView) findViewById(R.id.com3);
        comBoxes[3] = (ImageView) findViewById(R.id.com4);
        comBoxes[4] = (ImageView) findViewById(R.id.com5);
        comBoxes[5] = (ImageView) findViewById(R.id.com6);
        comBoxes[6] = (ImageView) findViewById(R.id.com7);
        comBoxes[7] = (ImageView) findViewById(R.id.com8);
        comBoxes[8] = (ImageView) findViewById(R.id.com9);
        comBoxes[9] = (ImageView) findViewById(R.id.com10);

        selBox = (ImageView) findViewById(R.id.sel);

        ranBox = (ImageView) findViewById(R.id.ran);

        result = (TextView) findViewById(R.id.textView);

        newGame = (Button) findViewById(R.id.newGame);

        dummy = (TextView) findViewById(R.id.dummy);    //to be deleted

        yesButton = (ImageButton) findViewById(R.id.yesButton);

        skipButton = (ImageButton) findViewById(R.id.skipButton);

        desc = (TextView) findViewById(R.id.desc);

        startScreen = (RelativeLayout) findViewById(R.id.startScreen);

        mainScreen = (RelativeLayout) findViewById(R.id.mainScreen);

        from = (ImageView) findViewById(R.id.from);

        to = (ImageView) findViewById(R.id.to);

        startButton = (ImageButton) findViewById(R.id.startButton);

        endScreen = (RelativeLayout) findViewById(R.id.endScreen);

        nextButton = (Button) findViewById(R.id.nextButton);

        boy = (ImageView) findViewById(R.id.boy);

        boy2 = (ImageView) findViewById(R.id.boy2);

        boy3 = (ImageView) findViewById(R.id.boy3);

        scoreBoard = (ImageView) findViewById(R.id.scoreboard);

        curtain = (ImageView) findViewById(R.id.curtain);

        //formod3
        selComBox = (ImageView) findViewById(R.id.selCom);
        ranComBox = (ImageView) findViewById(R.id.ranCom);
        desc2 = (TextView) findViewById(R.id.desc2);
        mod3Layout = (RelativeLayout) findViewById(R.id.mod3layout);
        skull = (ImageView) findViewById(R.id.skull);

        //for mod4
        myExtraBoxes[0] = (ImageView) findViewById(R.id.my11);
        myExtraBoxes[1] = (ImageView) findViewById(R.id.my12);
        myExtraBoxes[2] = (ImageView) findViewById(R.id.my13);
        myExtraBoxes[3] = (ImageView) findViewById(R.id.my14);
        myExtraBoxes[4] = (ImageView) findViewById(R.id.my15);
        myExtraBoxes[5] = (ImageView) findViewById(R.id.my16);
        myExtraBoxes[6] = (ImageView) findViewById(R.id.my17);
        myExtraBoxes[7] = (ImageView) findViewById(R.id.my18);
        myExtraBoxes[8] = (ImageView) findViewById(R.id.my19);
        myExtraBoxes[9] = (ImageView) findViewById(R.id.my20);

        comExtraBoxes[0] = (ImageView) findViewById(R.id.com11);
        comExtraBoxes[1] = (ImageView) findViewById(R.id.com12);
        comExtraBoxes[2] = (ImageView) findViewById(R.id.com13);
        comExtraBoxes[3] = (ImageView) findViewById(R.id.com14);
        comExtraBoxes[4] = (ImageView) findViewById(R.id.com15);
        comExtraBoxes[5] = (ImageView) findViewById(R.id.com16);
        comExtraBoxes[6] = (ImageView) findViewById(R.id.com17);
        comExtraBoxes[7] = (ImageView) findViewById(R.id.com18);
        comExtraBoxes[8] = (ImageView) findViewById(R.id.com19);
        comExtraBoxes[9] = (ImageView) findViewById(R.id.com20);

        myExtra = (RelativeLayout) findViewById(R.id.myExtraLayout);
        comExtra = (RelativeLayout) findViewById(R.id.comExtraLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
