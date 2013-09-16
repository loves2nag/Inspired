package ml.boa.fx;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class rates extends Activity {

    private int scr_w;
    private int scr_h;
    private GridView grd;
    //public ccytile adapter;
    private boolean WIGGLE = false;
    private int dragged_Index = -1;
    Globals g = Globals.getInstance();
    //private Handler ratesGet;
    Thread ratesGet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        g.setContext(this);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fx_rates);

        //int scr_size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        g.setScreenWidth(getResources().getDisplayMetrics().widthPixels);
        g.setScreenHeight(getResources().getDisplayMetrics().heightPixels);
        scr_w = g.getScreenWidth();
        scr_h = g.getScreenHeight();


        FrameLayout topbar = (FrameLayout)findViewById(R.id.toolbar);
        topbar.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        topbar.setMinimumHeight((int)Math.round(scr_h*0.05));
        ImageButton settings = (ImageButton)findViewById(R.id.settings);
        settings.setRight((int) Math.round(scr_w * 0.1));

        FrameLayout sales = (FrameLayout)findViewById(R.id.sales_cr);
        sales.setMinimumHeight((int)Math.round(scr_h*0.05));
        ImageView cr = (ImageView)findViewById(R.id.copyright_img);
        TextView email = (TextView)findViewById(R.id.sales);
        email.setText("Test Nag");

        settings.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ON CLICK", "SETTINGS BUTTON");
                Intent intent = new Intent(rates.this, ccypairsettings.class);
                rates.this.startActivity(intent);
            }
        });

        grd = (GridView)findViewById(R.id.grdRates);
        grd.setSmoothScrollbarEnabled(true);
        grd.setPadding((int) Math.round(scr_w * 0.02), (int) Math.round(scr_h * 0.02), (int) Math.round(scr_w * 0.02), (int) Math.round(scr_h * 0.02));
        grd.setVerticalSpacing((int) Math.round(scr_w * 0.02));
        grd.setHorizontalSpacing((int) Math.round(scr_w * 0.02));
        grd.setLongClickable(true);

        g.adapter = new ccytile(rates.this); //, TextUtils.split(share.getString("ccypairs", ""), ","));
        grd.setAdapter(g.adapter);

        grd.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View tile, int i, long l) {
                //startWiggle();
                g.setDraggedTilePos(i);

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(tile);
                tile.startDrag(null, shadowBuilder, null, 0);
                //tile.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        /*Thread getRates = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("UPDATING DATA", "NAG");
            }
        });

        do{
            if(g.updateFlag){
                try {
                    Log.i("GETRATES", "BEFORE WAITING");
                    ratesGet.wait(1000);
                    Log.i("GETRATES", "AFTER WAITING");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ratesGet.removeCallbacks(getRates);
                ratesGet.post(getRates);
            }
        }while (true);*/

        ratesGet = new Thread(){
            public void run(){
                do{
                    Log.i("CHECKING FLAG", "Flag = " + g.updateFlag);
                    if(g.updateFlag){
                        try {
                            Log.i("UPDATING DATA", "NAG");
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Log.i("UPDATING DATA", "INSIDE");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                g.adapter.notifyDataSetChanged();
                            }
                        });
                        //g.adapter.notifyDataSetChanged();
                    }/*else{
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                }while(true);
            }
        };

        //ratesGet.start();
    }

    private class swapGridItems extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... test){
            do{
                Log.i("SWAP LOOP", "REPORTING Flag = " + g.swap_flag);
                g.adapter.notifyDataSetChanged();
                g.swap_flag = false;
            }while(!g.swap_flag);
            return null;
        }

        protected void onPostExecute(Void test){
            Log.i("SWAP", "NOTIFYING GRID");
            g.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume(){
        Log.i("RESUMED", "ACTIVITY");
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        if (WIGGLE){
            stopWiggle();
            //WIGGLE = false;
        }else {
            Log.i("BACK PRESSED", "Stopping Thread");
            if (ratesGet!=null) ratesGet.interrupt();
            super.onBackPressed();
        }
    }

    private void startWiggle() {
        Animation wiggle = wiggleAnimation();

        for (int i=0; i < grd.getChildCount(); i++) {
            View child = grd.getChildAt(i);
            child.startAnimation(wiggle);
        }

        WIGGLE = true;
    }

    private void stopWiggle() {
        for (int i=0; i < grd.getChildCount(); i++) {
            View child = grd.getChildAt(i);
            child.clearAnimation();
        }

        WIGGLE = false;
    }

    private Animation wiggleAnimation(){
        Log.i("wiggleItems", "Started Wiggling");
        Animation wiggle = new RotateAnimation(-2.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        wiggle.setRepeatMode(Animation.REVERSE);
        wiggle.setRepeatCount(Animation.INFINITE);
        wiggle.setDuration(60);
        wiggle.setInterpolator(new AccelerateDecelerateInterpolator());
        return wiggle;
    }
}