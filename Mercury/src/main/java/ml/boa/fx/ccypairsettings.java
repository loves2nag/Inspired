package ml.boa.fx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by loves2nag on 7/28/13.
 */
public class ccypairsettings extends Activity {

    Globals g = Globals.getInstance();
    private int scr_w;
    private int scr_h;
    private GridView grd;
    private String[] ccypairs;
    private ccypair adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fx_ccypairsettings);

        scr_w = g.getScreenWidth();
        scr_h = g.getScreenHeight();
        ccypairs = getResources().getStringArray(R.array.ccypairs);

        ImageButton rates_ = (ImageButton)findViewById(R.id.rates_);

        rates_.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ccypairsettings.this.finish();
            }
        });

        grd = (GridView)findViewById(R.id.grdCcypairs);
        grd.setSmoothScrollbarEnabled(true);
        grd.setPadding((int) Math.round(scr_w * 0.02), (int) Math.round(scr_h * 0.02), (int) Math.round(scr_w * 0.02), (int) Math.round(scr_h * 0.02));
        grd.setVerticalSpacing((int) Math.round(scr_w * 0.005));
        //grd.setHorizontalSpacing((int) Math.round(scr_w * 0.02));
        grd.setClickable(true);
        adapter = new ccypair(ccypairsettings.this);
        grd.setAdapter(adapter);

    }

    public void update(View view){
        Log.i("CLICKED", "LISTENER" + view.getId());
        Log.i("CLICKED", "LISTENER" + R.id.yes_no);
        Log.i("CLICKED", "LISTENER" + view.getTag());
    }

}