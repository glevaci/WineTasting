package glevacic.winetasting.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import glevacic.winetasting.R;

public class StatusViewHolder extends ChildViewHolder {

    private TextView tvStatusHeading;
    private TextView tvStatusDescription;

    public StatusViewHolder(View itemView) {
        super(itemView);
        tvStatusHeading = (TextView) itemView.findViewById(R.id.item_status_heading);
        tvStatusDescription = (TextView) itemView.findViewById(R.id.item_status_description);
    }

    public TextView getTvStatusHeading() {
        return tvStatusHeading;
    }

    public TextView getTvStatusDescription() {
        return tvStatusDescription;
    }
}