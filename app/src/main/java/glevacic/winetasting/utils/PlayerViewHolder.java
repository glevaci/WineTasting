package glevacic.winetasting.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import org.androidannotations.annotations.Click;

import glevacic.winetasting.R;

public class PlayerViewHolder extends ParentViewHolder {

    private TextView tvPlayerName;

    public PlayerViewHolder(View itemView) {
        super(itemView);
        tvPlayerName = (TextView) itemView.findViewById(R.id.item_player_name);

        tvPlayerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            }
        });
    }

    public TextView getTvPlayerName() {
        return tvPlayerName;
    }
}