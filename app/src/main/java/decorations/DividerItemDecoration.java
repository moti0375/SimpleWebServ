package decorations;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by BartovMoti on 12/06/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;


    public DividerItemDecoration(Drawable divider) {
        this.mDivider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildAdapterPosition(view) == 0 ){  //Checking if it's the first item
            return;
        }

        outRect.top = mDivider.getIntrinsicHeight();
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount - 1; i++){
            View view = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            int dividerTop = view.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);

        }

    }
}
