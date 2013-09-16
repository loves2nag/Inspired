package ml.boa.fx;

import android.content.ClipData;
import android.content.res.Configuration;
import android.content.Context;
import android.provider.CallLog;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by loves2nag on 6/16/13.
 */
public class ccytile extends BaseAdapter {

    private Context context;
    private final String[] ccypairs;
    private int scr_w;
    private int scr_h;
    private List<View> tiles;
    private View gridView;
    private String prices;
    Globals g = Globals.getInstance();
    InputStream is;
    private String ccyPair;
    private String[] rates;
    private String rate;
    private String[] Bid;
    private String[] Ask;
    private String bid;
    private String ask;
    private String bidPts;
    private String askPts;
    private String bidPts_;
    private String askPts_;


    public ccytile(Context context){
        this.context = context;
        this.ccypairs = g.getCcyPairs();
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  View gridView;
        g.viewCount = g.viewCount + 1;

        if (convertView == null){
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.fx_ccytiles, null);
        }else{
            gridView = (View)convertView;
        }

        Log.i("Adapter", "Ccy Pair " + ccypairs[position] + " View Item Created" );

        gridView.setOnDragListener(new MyDragListener());
        prices = g.getCcyPairPrices(ccypairs[position]);

        if (!prices.equalsIgnoreCase("")){
            //int scr_size = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            scr_w = context.getResources().getDisplayMetrics().widthPixels;
            scr_h = context.getResources().getDisplayMetrics().heightPixels;

            ccyPair = prices.substring(0,6);
            rates = TextUtils.split(prices.substring(7), ",");
            bid = rates[0].substring(0, rates[0].length()-3);
            bidPts = rates[0].substring(rates[0].length()-3, rates[0].length()-1);
            bidPts_ = rates[0].substring(rates[0].length()-1);
            ask = rates[1].substring(0, rates[1].length()-3);
            askPts = rates[1].substring(rates[1].length()-3, rates[1].length()-1);
            askPts_ = rates[1].substring(rates[1].length()-1);

            TextView ccypair = (TextView)gridView.findViewById(R.id.currency_pair);
            ccypair.setHeight((int)Math.round(scr_h*0.075));
            ccypair.setText(ccyPair.substring(0,3) + "/" + ccyPair.substring(3));

            TextView ccy_actual_left = (TextView)gridView.findViewById(R.id.rates_actual_left);
            ccy_actual_left.setHeight((int)Math.round(scr_h*0.035));
            ccy_actual_left.setWidth((int) Math.round(((scr_w - scr_w * 0.06) / 2) * 0.5));
            ccy_actual_left.setGravity(Gravity.BOTTOM);
            ccy_actual_left.setText(bid);

            TextView ccy_actual_right = (TextView)gridView.findViewById(R.id.rates_actual_right);
            ccy_actual_right.setHeight((int)Math.round(scr_h*0.035));
            ccy_actual_right.setWidth((int) Math.round(((scr_w - scr_w *0.06) / 2) * 0.5));
            ccy_actual_right.setGravity(Gravity.BOTTOM);
            ccy_actual_right.setText(ask);

            TextView ccy_decimal_left = (TextView)gridView.findViewById(R.id.rates_decimal_left);
            ccy_decimal_left.setHeight((int)Math.round(scr_h*0.075));
            ccy_decimal_left.setWidth((int) Math.round(((scr_w - scr_w *0.06) / 2) * 0.4));
            ccy_decimal_left.setGravity(Gravity.RIGHT);
            //ccy_decimal_left.setGravity(Gravity.CENTER_VERTICAL);
            //ccy_decimal_left.setPadding(0,0,0,0);
            ccy_decimal_left.setText(bidPts);

            TextView ccy_decimal_left_ = (TextView)gridView.findViewById(R.id.rates_decimal_left_);
            ccy_decimal_left_.setHeight((int)Math.round(scr_h*0.075));
            ccy_decimal_left_.setWidth((int) Math.round(((scr_w - scr_w *0.06) / 2) * 0.1));
            //ccy_decimal_left_.setGravity(Gravity.LEFT);
            ccy_decimal_left_.setGravity(Gravity.BOTTOM);
            ccy_decimal_left_.setText(bidPts_);

            TextView ccy_decimal_right = (TextView)gridView.findViewById(R.id.rates_decimal_right);
            ccy_decimal_right.setHeight((int)Math.round(scr_h*0.075));
            ccy_decimal_right.setWidth((int) Math.round(((scr_w - scr_w *0.06) / 2) * 0.4));
            //ccy_decimal_right.setGravity(Gravity.END);
            ccy_decimal_right.setGravity(Gravity.RIGHT);
            ccy_decimal_right.setText(askPts);

            TextView ccy_decimal_right_ = (TextView)gridView.findViewById(R.id.rates_decimal_right_);
            ccy_decimal_right_.setHeight((int)Math.round(scr_h*0.075));
            ccy_decimal_right_.setWidth((int) Math.round(((scr_w - scr_w *0.06) / 2) * 0.1));
            //ccy_decimal_right_.setGravity(Gravity.LEFT);
            ccy_decimal_right_.setGravity(Gravity.BOTTOM);
            ccy_decimal_right_.setText(askPts_);
        }

        Log.i("CCY PAIRS","Length = " + g.ccyPairs.length);
        Log.i("CURRENT VIEW","Position = " + g.viewCount);
        if(g.ccyPairs.length==g.viewCount){
            g.viewCount = -1;
            Log.i("UPDATING FLAG", "Before " + g.updateFlag);
            g.updateFlag=true;
            Log.i("UPDATING FLAG", "After " + g.updateFlag);
        }
        return  gridView;
    }

    @Override
    public int getCount(){
        return ccypairs.length;
    }

    @Override
    public Object getItem( int position ) {
        return gridView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}