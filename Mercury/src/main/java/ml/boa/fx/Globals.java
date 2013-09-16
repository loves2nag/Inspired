package ml.boa.fx;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Globals extends Application {

    private static Globals instance;

    private int DragOverTilePos = -1;
    private int DraggedTilePos;
    private int ActualTilePos;
    public String[] ccyPairs;
    private int scr_w;
    private int scr_h;
    public boolean swap_flag=false;
    public ccytile adapter;
    public Context context;
    public boolean updateFlag = false;
    public int viewCount = -1;
    public String[] ccyPairsList;
    InputStream is;
    //private SharedPreferences share;

    private Globals(){}


    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }

        return instance;
    }

    public void setCcypairs(String[] strCcyPairs){
        ccyPairs = strCcyPairs;
    }

    public void updateSharedPreferences(){
        SharedPreferences share = context.getSharedPreferences("shared_data", 0);
        share.edit().putString("ccypairs", TextUtils.join(",", ccyPairs)).commit();
        Log.i("SHARED DATA", "CCY PAIRS = " + share.getString("ccypairs",""));
    }

    public String[] getCcyPairs(){
        return ccyPairs;
    }

    public int getScreenWidth(){
        return scr_w;
    }

    public int getScreenHeight(){
        return scr_h;
    }

    public void setScreenWidth(int width){
        scr_w = width;
    }

    public void setScreenHeight(int height){
        scr_h = height;
    }

    public void setDragOverTilePos(int pos){
        this.DragOverTilePos = pos;
    }

    public int getDragOverTilePos(){
        return this.DragOverTilePos;
    }

    public void setDraggedTilePos(int pos){
        this.DraggedTilePos = pos;
    }

    public int getDraggedTilePos(){
        return this.DraggedTilePos;
    }

    public void setActualTilePos(int pos){
        this.ActualTilePos = pos;
    }

    public int getActualTilePos(){
        return this.ActualTilePos;
    }

    public void swapDraggedItems(int drag, int dragged){
        String tmpCcyPair = ccyPairs[drag];
        ccyPairs[drag] = ccyPairs[dragged];
        ccyPairs[dragged] = tmpCcyPair;
        updateSharedPreferences();
        adapter.notifyDataSetChanged();
    }

    public void setContext( Context context){
        this.context = context;
    }

    public void setUpdateFlag(int pos){
        if (ccyPairs.length==pos){
            updateFlag = true;
        }
    }

    public String getCcyPairPrices(String ccyPair){
        String strCcyPair;

        try {
            is = context.getResources().getAssets().open("prices.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while((line = br.readLine()) != null){
                if (line.substring(0,6).equalsIgnoreCase(ccyPair)){
                    Log.i("FILE", "Returns Line = " + line);
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}