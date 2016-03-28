package glevacic.winetasting;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemLongClick;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@EActivity
public class MainActivity extends AppCompatActivity {

    ListView drawerList = null;
    DrawerLayout drawerLayout = null;
    ActionBarDrawerToggle drawerToggle = null;

    private PlayerListAdapter playerListAdapter;
    private PlayerList playerList;
    private ExpandableListView expandableListView;

    private static final String TABLE = "tasks";
    private static final String KEY_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TYPE = "type";
    private static final String[] COLUMNS = {KEY_DESCRIPTION, KEY_TYPE};

    private SQLiteDatabase database;
    private int numberOfTasks;
    private Set<Integer> usedTasks = new HashSet<>();
    private Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (playerList.getPlayers().isEmpty()) {
            TextView txtView = (TextView) findViewById(R.id.textViewTask);
            txtView.setText(R.string.initialMessage);
        }
        ActiveStatus status = new ActiveStatus("Probni status", "opis statusa");

        Player ivica = new Player("Ivica");
        ivica.addActiveStatus(status);

        Player marica = new Player("Marica");
        marica.addActiveStatus(status);
        marica.addActiveStatus(status);

        Player jovanka = new Player("Jovanka");

        playerList = new PlayerList();
        playerList.addPlayer(ivica);
        playerList.addPlayer(marica);
        playerList.addPlayer(jovanka);

        expandableListView = (ExpandableListView) findViewById(R.id.drawer_list);
        playerListAdapter = new PlayerListAdapter(MainActivity.this, playerList.getPlayers());
        expandableListView.setAdapter(playerListAdapter);
        registerForContextMenu(expandableListView);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getReadableDatabase();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);

        displayNextTask();      // display first task
    }

    public PlayerList getPlayers() {
        return playerList;
    }

    @Click(R.id.buttonDrawer_newPlayer)
    public void addPlayer() {
        // TODO dialog, player name, ok
    }

    @ItemLongClick
    public void showOptions(Player player) {

    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenuInfo menuInfo) {

        if (view.getId() == R.id.drawer_list) {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        }*/

        /*if (v.getId()==R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(Countries[info.position]);
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }*/
    }

    @Click(R.id.buttonGame_next)
    public void displayNextTask() {

        if (usedTasks.size() == numberOfTasks)
            usedTasks.clear();

        int taskId = getNextTaskId(numberOfTasks);
        usedTasks.add(taskId);

        // id is autoincremented
        // data is never deleted from table, so this cursor will never be null or empty
        Cursor cursor = database.query(TABLE,                                   // table
                                        COLUMNS,                                // column names
                                        KEY_ID + " = ?",                        // select
                                        new String[] { String.valueOf(taskId) },// selections args
                                        null,                                   // group by
                                        null,                                   // having
                                        null,                                   // order by
                                        null);                                  // limit

        cursor.moveToFirst();
        TextView txtView = (TextView) findViewById(R.id.textViewTask);
        txtView.setText(cursor.getString(0));
        cursor.close();
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks)+1;
        while (usedTasks.contains(taskId))
            taskId = random.nextInt(numberOfTasks);
        return taskId;
    }
}