package glevacic.winetasting.utils;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import glevacic.winetasting.R;

public class PlayerViewHolder extends ParentViewHolder {

    private TextView tvPlayerName;
    private ImageButton imageButton;

    public PlayerViewHolder(final View itemView) {
        super(itemView);

        tvPlayerName = (TextView) itemView.findViewById(R.id.item_player_name);
        imageButton = (ImageButton) itemView.findViewById(R.id.item_expand_arrow);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                    imageButton.setBackgroundResource(R.drawable.ic_expand_more);
                }
            }
        });
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded)
            imageButton.setBackgroundResource(R.drawable.ic_expand_less);
        else
            imageButton.setBackgroundResource(R.drawable.ic_expand_more);
    }

    public TextView getTvPlayerName() {
        return tvPlayerName;
    }
}