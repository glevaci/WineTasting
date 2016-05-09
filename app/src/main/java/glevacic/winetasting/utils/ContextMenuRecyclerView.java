package glevacic.winetasting.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerContextMenuInfo contextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return contextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getChildAdapterPosition(originalView);
        if (longPressPosition >= 0) {
            int parentIndex = getParentIndex(longPressPosition);
            long longPressId = getAdapter().getItemId(parentIndex);
            contextMenuInfo = new RecyclerContextMenuInfo(parentIndex, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    private int getParentIndex(int position) {
        PlayerListAdapter playerListAdapter = (PlayerListAdapter) getAdapter();

        int i = 0;
        int parentIndex = -1;
        while (i <= position) {
            if (playerListAdapter.getItemViewType(i) == 0)
                ++parentIndex;
            ++i;
        }
        return parentIndex;
    }

    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }

        final public int position;
        final public long id;
    }
}
