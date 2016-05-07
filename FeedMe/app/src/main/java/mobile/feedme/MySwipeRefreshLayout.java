package mobile.feedme;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by stevy_000 on 5/5/2016.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean handled = true;
//
//        final int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                if (event.getY() > getHeight() / 3) {
//                    handled = false;
//                    event.setAction(MotionEvent.ACTION_CANCEL);
//                    handled = false;
//                }
//            }
//            break;
//        }
//        return super.onTouchEvent(event);
//    }
}