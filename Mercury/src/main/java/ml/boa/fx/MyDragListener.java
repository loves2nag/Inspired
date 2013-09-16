package ml.boa.fx;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class MyDragListener implements AdapterView.OnDragListener{

    Globals g = Globals.getInstance();


    public int getHoveredItem( View grdItem, DragEvent drgEvt ){
        GridView grd = (GridView) grdItem.getParent();

        float cur_X_pos = drgEvt.getX();
        float cur_Y_pos = drgEvt.getY();

        try{
            for (int item = 0;item<grd.getChildCount();item++){
                float pos_X = grd.getChildAt(item).getX();
                float pos_Y = grd.getChildAt(item).getY();
                int width = grd.getChildAt(item).getWidth();
                int height = grd.getChildAt(item).getHeight();

                float item_end_X_pos = pos_X + (float)width;
                float item_end_Y_pos = pos_Y + (float)height;
                //Log.i("Hovered", "Item  = " + item);
                //Log.i("Hovered", "Position X = " + pos_X + " Y = " + pos_Y);

                if ((cur_X_pos >= pos_X && cur_X_pos <= item_end_X_pos) && (cur_Y_pos >= pos_Y && cur_Y_pos <= item_end_Y_pos)){
                    //g.setDragOverTilePos(item);
                    g.setDragOverTilePos(grd.getPositionForView(grd.getChildAt(item)));
                    //item = grd.getPositionForView(grd.getChildAt(item));
                    Log.i("Hovered", " On Item = " + grd.getPositionForView(grd.getChildAt(item)) + " Position X = " + pos_X + " Y = " + pos_Y);
                    g.setActualTilePos(grd.getPositionForView(grd.getChildAt(item)));
                    return item;
                }
            }
        }catch (NullPointerException e){
            Log.i("Exception", e.getMessage());
        }

        return -1;
    }

    @Override
    public boolean onDrag( View view, DragEvent dragEvent){

        boolean result = true;
        int pos;
        int pos_;
        GridView parent = (GridView) view.getParent();

        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED :
                break;
            case DragEvent.ACTION_DRAG_ENTERED :
                pos = getHoveredItem(view, dragEvent);
                Log.i("Hovered", "Item  = " + pos + " Last Visible Position = " + parent.getLastVisiblePosition());
                Log.i("Hovered", "Item  = " + pos + " First Visible Position = " + parent.getFirstVisiblePosition());
                Log.i("CHECK", "Tile Actual Pos = " + g.getActualTilePos() + " Child Count = " + parent.getCount());

                if ((parent.getLastVisiblePosition() == pos || parent.getLastVisiblePosition() - 1 == pos) && g.getActualTilePos() < parent.getCount()) {
                    Log.i( "DRAG ENTERED", "DOWN");
                    if (parent.getLastVisiblePosition() == pos){parent.smoothScrollToPosition(g.getActualTilePos() + 1);}
                    if (parent.getLastVisiblePosition() - 1 == pos){parent.smoothScrollToPosition(g.getActualTilePos() + 2);}
                    //parent.smoothScrollByOffset(view.getHeight());
                    //parent.smoo
                }else if((parent.getFirstVisiblePosition() == pos || parent.getFirstVisiblePosition() + 1 == pos) && g.getActualTilePos() > 0 ) {
                    Log.i( "DRAG ENTERED", "UP");
                    if (parent.getFirstVisiblePosition() == pos){parent.smoothScrollToPosition(g.getActualTilePos() - 1);}
                    if (parent.getFirstVisiblePosition() + 1 == pos){parent.smoothScrollToPosition(g.getActualTilePos() - 2);}
                    //parent.smoothScrollByOffset(view.getHeight());
                }

                break;
            case DragEvent.ACTION_DRAG_EXITED :
                pos = g.getDragOverTilePos();

                if ((parent.getLastVisiblePosition() == pos || parent.getLastVisiblePosition() - 1 == pos) && g.getActualTilePos() < parent.getCount()) {
                    Log.i( "DRAG EXITED", "DOWN");
                    parent.smoothScrollBy(view.getHeight() + (int) Math.round( view.getHeight() * 0.2), 1000);
                }else if((parent.getFirstVisiblePosition() == pos || parent.getFirstVisiblePosition() + 1 == pos) && g.getActualTilePos() > 0 ) {
                    Log.i( "DRAG EXITED", "UP");
                    parent.smoothScrollBy(- view.getHeight() - (int) Math.round( view.getHeight() * 0.2), 1000);
                }

                break;
            case DragEvent.ACTION_DRAG_LOCATION :
                break;
            case DragEvent.ACTION_DROP :
                pos_ = g.getDraggedTilePos();
                pos = g.getDragOverTilePos();
                Log.i("Dragged Tile Pos", " = " + pos_);
                Log.i("Dragged Over Tile Pos", " = " + pos);

                if (pos == pos_){
                    //view.setVisibility(View.VISIBLE);
                    Log.i("DROPPED", "NO SWAP REQUIRED, DROPPED ON SAME POSITION");
                }else if (pos == -1) {
                    Log.i("DROPPED", "NO SWAP REQUIRED, NO DRAG HAPPENED");
                }
                else  {
                    Log.i("DROPPED", "DO NOTIFY ADAPTER HERE");
                    g.swapDraggedItems( pos, pos_);

                }

                break;
            case DragEvent.ACTION_DRAG_ENDED :
                break;
            //default:
            //  result=false;
            // break;
        }
        return result;
    }
}