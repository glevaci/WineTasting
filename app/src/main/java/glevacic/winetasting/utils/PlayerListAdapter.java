package glevacic.winetasting.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import glevacic.winetasting.R;

public class PlayerListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Player> players;

    public PlayerListAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @Override
    public int getGroupCount() {
        return players.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return players.get(groupPosition).getActiveStatuses().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return players.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return players.get(groupPosition).getActiveStatuses().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)
    {
        Player group = (Player) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_player, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.player_name);
        tv.setText(group.getName());

        // TODO check if long click works
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent)
    {
        ActiveStatus status = (ActiveStatus) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_status, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_status_heading);
        textView.setText(status.toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
