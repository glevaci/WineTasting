package glevacic.winetasting.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import glevacic.winetasting.R;

public class PlayerListAdapter extends ExpandableRecyclerAdapter<PlayerViewHolder, StatusViewHolder> {

    private LayoutInflater inflater;

    public PlayerListAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PlayerViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = inflater.inflate(R.layout.item_player, parentViewGroup, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public StatusViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = inflater.inflate(R.layout.item_status, childViewGroup, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(PlayerViewHolder playerViewHolder, int position, ParentListItem parentListItem) {
        Player player = (Player) parentListItem;
        playerViewHolder.getTvPlayerName().setText(player.getName());
    }

    @Override
    public void onBindChildViewHolder(StatusViewHolder statusViewHolder, int position, Object childListItem) {
        ActiveStatus status = (ActiveStatus) childListItem;
        statusViewHolder.getTvStatusHeading().setText(status.getTitle() + ':');
        statusViewHolder.getTvStatusDescription().setText(status.getDescription() + '\n');
    }
}
