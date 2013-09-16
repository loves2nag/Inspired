package ml.boa.fx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class splash extends Activity {

    Globals g = Globals.getInstance();
    IntentLauncher launcher;

    private TextView checkIC;
    private static long SLEEP_TIME = 60;
    private static final int REQUEST_CODE = 0;
    private String connect_txt = "Checking Internet Connection...";
    private boolean connect_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        g.setContext(this);

        SharedPreferences share = getApplicationContext().getSharedPreferences("shared_data", 0);
        share.edit().remove("ccypairs").commit();

        if (share.getString("ccypairs", "").equals("")){
            share.edit().putString("ccypairs", TextUtils.join(",", getResources().getStringArray(R.array.ccypairs)).toString()).commit();
        }

        g.ccyPairs = TextUtils.split( share.getString("ccypairs", ""), ",");
        g.setUpCcyPairSettings();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fx_splash);

        g.scr_size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        g.scr_w = getResources().getDisplayMetrics().widthPixels;
        g.scr_h = getResources().getDisplayMetrics().heightPixels;


        checkIC = ( TextView )findViewById(R.id.check);
        checkIC.setTextColor(getResources().getColor(android.R.color.white));
        checkIC.setText(connect_txt);

        TextView scroll_txt = (TextView)findViewById(R.id.scr_txt);
        scroll_txt.setTextColor(getResources().getColor(android.R.color.white));
        scroll_txt.setPaddingRelative((int) Math.round(g.scr_w * 0.025),(int) Math.round(g.scr_h * 0.75),(int) Math.round(g.scr_w * 0.025),(int) Math.round(g.scr_h * 0.075));
        scroll_txt.setTextSize(12);
        scroll_txt.setText(Html.fromHtml(getResources().getString(R.string.copyright)));
        scroll_txt.setMovementMethod(new ScrollingMovementMethod());
        Linkify.addLinks(scroll_txt, Linkify.EMAIL_ADDRESSES);
        scroll_txt.setMovementMethod(LinkMovementMethod.getInstance());

        TextView copyright_txt = (TextView)findViewById(R.id.cp_txt);
        copyright_txt.setTextColor(getResources().getColor(android.R.color.black));
        copyright_txt.setText(Html.fromHtml(getResources().getString(R.string.cp_title)));
        copyright_txt.setHeight((int) Math.round(g.scr_h * 0.075));
        copyright_txt.setPaddingRelative((int) Math.round(g.scr_w * 0.025),0,(int) Math.round(g.scr_w * 0.5),0);
        TextView version = (TextView)findViewById(R.id.version);
        version.setHeight((int) Math.round(g.scr_h * 0.075));
        version.setPaddingRelative((int) Math.round(g.scr_w * 0.5), 0, (int) Math.round(g.scr_w * 0.025), 0);
        version.setTextColor(getResources().getColor(android.R.color.white));
        version.setText(getResources().getString(R.string.version));
        version.setLeft((int) Math.round(g.scr_w * 0.025));
        networkConnectivity x = new networkConnectivity();
        x.execute();

        launcher = new IntentLauncher();
        launcher.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            //displayNetworkState(isNetworkEnable());
        }
    }

    /**
     * Internet Connection Activity Check
     */
    private void IsNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() ) {
            if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI){
                connect_txt = "Using WI-FI Service";
            }else  if( cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE ){
                connect_txt = "Using Cellular Service";
            }else  if( cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_BLUETOOTH ){
                connect_txt = "Using BlueTooth Service";
            }
            connect_flag = true;
        }
        else {connect_txt = "No Connectivity, Plz Check";}
    }

    private class IntentLauncher extends Thread {
        private volatile long interval = SLEEP_TIME;
        private final Object lockObj = new Object();

        @Override
        public void run() {
            synchronized (lockObj){
                try {
                    Log.i("THREAD", "Sleeping " + interval + " Seconds");
                    lockObj.wait(interval * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (connect_flag){
                    Log.i("MOVING TO RATES", "CREATING RATES");
                    Intent intent = new Intent(splash.this, rates.class);
                    splash.this.startActivity(intent);
                    splash.this.finish();
                }else{Log.i("STAYING INTO SPLASH", "NOT CREATING RATES");
                }
            }
        }

        public void setInterval(long newInterval){
            synchronized (lockObj){
                this.interval = newInterval;
                lockObj.notify();
            }
        }
    }

    private class networkConnectivity extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... test){
            IsNetworkAvailable();
            if (connect_flag) priceCheck();
            return null;
        }

        protected void onPostExecute(Void test){
            checkIC.setText(connect_txt);
            launcher.setInterval(0);
        }
    }

    private void priceCheck(){
        int x = 0;
    }
}