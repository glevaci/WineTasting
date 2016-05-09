package glevacic.winetasting.utils;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import glevacic.winetasting.R;

public class PlayerViewHolder extends ParentViewHolder {

    private TextView tvPlayerName;
    private ImageButton imageButton;

    public PlayerViewHolder(final View itemView) {
        super(itemView);

        tvPlayerName = (TextView) itemView.findViewById(R.id.item_player_name);
        imageButton = (ImageButton) itemView.findViewById(R.id.item_expand_arrow);
    }

    public TextView getTvPlayerName() {
        return tvPlayerName;
    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }
}