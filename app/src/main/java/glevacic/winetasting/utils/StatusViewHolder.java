package glevacic.winetasting.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bluejamesbond.text.DocumentView;

import glevacic.winetasting.R;

public class StatusViewHolder extends ChildViewHolder {

    private DocumentView dvStatus;

    public StatusViewHolder(View itemView) {
        super(itemView);
        dvStatus = (DocumentView) itemView.findViewById(R.id.item_status);
    }

    public DocumentView getDvStatus() {
        return dvStatus;
    }

}