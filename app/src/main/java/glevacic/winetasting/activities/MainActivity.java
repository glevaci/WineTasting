package glevacic.winetasting.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import glevacic.winetasting.R;
import glevacic.winetasting.dialogs.InfoDialogFragment;
import glevacic.winetasting.dialogs.PlayerNameDialogFragment;
import glevacic.winetasting.utils.ContextMenuRecyclerView;
import glevacic.winetasting.utils.ContextMenuRecyclerView.RecyclerContextMenuInfo;
import glevacic.winetasting.utils.DatabaseHelper;
import glevacic.winetasting.utils.Player;
import glevacic.winetasting.utils.PlayerList;
import glevacic.winetasting.utils.PlayerListAdapter;
import glevacic.winetasting.utils.Status;

import static android.view.Gravity.LEFT;
import static glevacic.winetasting.dialogs.PlayerNameDialogFragment.ADD_NEW_PLAYER;
import static glevacic.winetasting.utils.CardTypes.REMOVE_ALL_STATUSES;
import static glevacic.winetasting.utils.CardTypes.REMOVE_RANDOM_PLAYER_STATUSES;
import static glevacic.winetasting.utils.CardTypes.REMOVE_RANDOM_STATUSES_FROM_GAME;
import static glevacic.winetasting.utils.CardTypes.STATUS;
import static glevacic.winetasting.utils.DatabaseHelper.CARD_TYPE;
import static glevacic.winetasting.utils.DatabaseHelper.COLUMNS;
import static glevacic.winetasting.utils.DatabaseHelper.DESCRIPTION;
import static glevacic.winetasting.utils.DatabaseHelper.HEADING;
import static glevacic.winetasting.utils.DatabaseHelper.ID;
import static glevacic.winetasting.utils.DatabaseHelper.TABLE;

/* TODO
* dodati globalne statuse (type == 7)
* dodati popis aktivnih statusa za trenutnog igrača?
*/

@EActivity
public class MainActivity extends AppCompatActivity {

    private PlayerListAdapter playerListAdapter;
    private PlayerList playerList;
    private DrawerLayout drawer;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private int numberOfTasks;
    private Set<Integer> usedTasks;
    private Random random;
    private boolean waitingForPlayers;

    private static final String preferencesFile = "sharedPreferences";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.a_main_drawer_layout);

        Intent intent = getIntent();
        Boolean newGame = intent.getBooleanExtra("newGame", false);
        sharedPreferences = getSharedPreferences(preferencesFile, MODE_PRIVATE);
        if (newGame) {
            sharedPreferences.edit().clear().commit();
        }

        restoreDataFromSharedPreferences();
        playerListAdapter = new PlayerListAdapter(this, playerList.getPlayers());
        setUpRecyclerView();
        setUpDrawer();
        updateViewIfNeeded();
        setUpDatabaseAndTaskData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        database = databaseHelper.getReadableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        database.close();
        saveDataToSharedPreferences();
    }

    private void setUpDatabaseAndTaskData() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getReadableDatabase();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        usedTasks = new HashSet<>();
        random = new Random();
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("waitingForPlayers", waitingForPlayers);
        editor.putInt("numberOfTasks", numberOfTasks);
        editor.putBoolean("drawerOpened", drawer.isDrawerOpen(LEFT));
        Gson gson = new Gson();
        editor.putString("playerList", gson.toJson(playerList));
        editor.putString("usedTasks", gson.toJson(usedTasks));
        editor.apply();
    }

    private void restoreDataFromSharedPreferences() {
        waitingForPlayers = sharedPreferences.getBoolean("waitingForPlayers", true);
        numberOfTasks = sharedPreferences.getInt("numberOfTasks", 0);

        Gson gson = new Gson();
        String playerListJson = sharedPreferences.getString("playerList", "");
        if (playerListJson.equals("")) {
            playerList = new PlayerList();
        } else {
            playerList = gson.fromJson(playerListJson, PlayerList.class);
        }

        String usedTasksJson = sharedPreferences.getString("usedTasks", "");
        if (usedTasksJson.equals("")) {
            usedTasks = new HashSet<>();
        } else {
            usedTasks = gson.fromJson(usedTasksJson, new TypeToken<Set<Integer>>() {
            }.getType());
        }
    }

    private void updateViewIfNeeded() {
        if (sharedPreferences.getBoolean("drawerOpened", false)) {
            drawer.openDrawer(LEFT);
        }

        if (playerList.getPlayers().size() > 0) {
            String taskHeading = sharedPreferences.getString("taskHeading", null);
            String taskDescription = sharedPreferences.getString("taskDescription", null);
            if (taskHeading != null && taskDescription != null) {
                displayTask(taskHeading, taskDescription);
                TextView tv = (TextView) findViewById(R.id.a_main_tv_player_name);
                tv.setText(playerList.getCurrentPlayer().getName());
                Button button = (Button) findViewById(R.id.a_main_btn_next);
                button.setEnabled(true);
            }
        }
    }

    private void setUpDrawer() {

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
            }

            @Override
            public void onDrawerClosed(View view) {
                // start game on first closing of drawer
                if (waitingForPlayers && playerList.getPlayers().size() > 0) {
                    waitingForPlayers = false;
                    startGame();
                }
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });

        if (playerList.getPlayers().size() == 0) {
            drawer.openDrawer(LEFT);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

            String heading = getResources().getString(R.string.dialog_intro_heading);
            String message = getResources().getString(R.string.dialog_intro_message);
            DialogFragment fragment = InfoDialogFragment.newInstance(heading, message);
            fragment.show(getSupportFragmentManager(), "infoDialog");
        }
    }

    private void setUpRecyclerView() {
        ContextMenuRecyclerView recyclerView = (ContextMenuRecyclerView) findViewById(R.id.dr_rv_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playerListAdapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        RecyclerContextMenuInfo info = (RecyclerContextMenuInfo) menuInfo;
        menu.setHeaderTitle(playerList.getPlayers().get(info.position).getName());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_player_options, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RecyclerContextMenuInfo info = (RecyclerContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_player_delete:
                onDeleteClicked(info.position);
                return true;
            case R.id.menu_player_rename:
                onRenameClicked(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void onRenameClicked(final int i) {
        String title = playerList.getPlayers().get(i).getName();
        PlayerNameDialogFragment fragment = PlayerNameDialogFragment.newInstance(title, i);
        fragment.show(getSupportFragmentManager(), "renamePlayerDialog");
    }

    public void renamePlayer(int i, EditText editText) {
        playerList.getPlayers().get(i).setName(editText.getText().toString());
        playerListAdapter.notifyParentItemChanged(i);
    }

    private void onDeleteClicked(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Želite li stvarno obrisati igrača "
                + playerList.getPlayers().get(position).getName() + "?");

        alertDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deletePlayer(position);
            }
        });
        alertDialog.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public void deletePlayer(int position) {
        playerListAdapter.notifyParentItemRemoved(position);
        playerList.getPlayers().remove(position);

        if (playerList.getPlayers().size() == 0) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }

    @Click(R.id.dr_btn_new_player)
    public void showNewPlayerDialog() {
        String heading = getResources().getString(R.string.dialog_new_player_heading);
        DialogFragment fragment = PlayerNameDialogFragment.newInstance(heading, ADD_NEW_PLAYER);
        fragment.show(getSupportFragmentManager(), "newPlayerDialog");
    }


    public void addNewPlayer(EditText editText) {
        int i = playerList.getPlayers().size();
        Player player = new Player(editText.getText().toString());
        playerList.addPlayer(player);
        playerListAdapter.notifyParentItemInserted(i);
        if (i == 0) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void checkPlayerName(EditText editText, TextView textView, AlertDialog dialog) {
        if (editText.getText().toString().isEmpty()) {
            textView.setText(R.string.dialog_new_player_name_empty);
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            return;
        }

        List<Player> players = playerList.getPlayers();
        if ((players != null) && (!players.isEmpty())) {
            for (Player player : players) {
                if (player.getName().equals(editText.getText().toString())) {
                    textView.setText(R.string.dialog_new_player_name_exists);
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    return;
                }
            }
        }
        // if everything is ok
        textView.setText("");
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
    }

    private void startGame() {
        startNextRound();
        Button button = (Button) findViewById(R.id.a_main_btn_next);
        button.setEnabled(true);
    }

    @Click(R.id.a_main_btn_next)
    public void startNextRound() {
        getNextPlayer();
        getNextTask();
    }

    private void getNextPlayer() {
        Player player = playerList.getNextPlayer();
        TextView tv = (TextView) findViewById(R.id.a_main_tv_player_name);
        tv.setText(player.getName());

        // TODO show list of player's statuses
    }

    private void getNextTask() {

        if (usedTasks.size() == numberOfTasks) {
            restartGame();
        }

        int taskId = getNextTaskId(numberOfTasks);
        usedTasks.add(taskId);

        // id is autoincremented, data is never deleted from table
        // so this cursor will never be null or empty
        Cursor cursor = database.query(TABLE,           // table
                COLUMNS,                                // column names
                ID + " = ?",                            // select
                new String[]{String.valueOf(taskId)},   // selections args
                null,                                   // group by
                null,                                   // having
                null,                                   // order by
                null);                                  // limit

        cursor.moveToFirst();

        String taskHeading = cursor.getString(cursor.getColumnIndex(HEADING));
        String taskDescription = cursor.getString(cursor.getColumnIndex(DESCRIPTION));

        int cardType = cursor.getInt(cursor.getColumnIndex(CARD_TYPE));

        if (cardType == STATUS)
            applyStatusCard(taskHeading, taskDescription);

        else if (cardType == REMOVE_ALL_STATUSES)
            removeAllStatusesFromGame();

        else if (cardType == REMOVE_RANDOM_PLAYER_STATUSES)
        // we need to update taskDescription with random player's name
            taskDescription = removeRandomPlayerStatuses(taskDescription);

        else if (cardType == REMOVE_RANDOM_STATUSES_FROM_GAME)
            removeRandomStatusesFromGame();

        displayTask(taskHeading, taskDescription);
        saveTaskInfoToSharedPreferences(taskHeading, taskDescription);
        cursor.close();
    }

    private void saveTaskInfoToSharedPreferences(String taskHeading, String taskDescription) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("taskHeading", taskHeading);
        editor.putString("taskDescription", taskDescription);
        editor.apply();
    }

    private void restartGame() {
        usedTasks.clear();
        removeAllStatusesFromGame();

        String title = "Kraj";
        String message = "Sve kartice su iskorištene, igra kreće ispočetka.";
        DialogFragment fragment = InfoDialogFragment.newInstance(title, message);
        fragment.show(getSupportFragmentManager(), "infoDialog");
    }

    private void applyStatusCard(String taskHeading, String taskDescription) {
        int parentIndex = playerList.getCurrentIndex();
        int childIndex = playerList.getCurrentPlayer().getChildItemList().size();

        Status status = new Status(taskHeading, taskDescription);
        playerList.getCurrentPlayer().addStatus(status);
        playerListAdapter.notifyChildItemInserted(parentIndex, childIndex);
    }

    private void removeRandomStatusesFromGame() {
        Random rand = new Random();
        for (int i = 0; i < playerList.getPlayers().size(); ++i) {
            for (int st = 0; st < playerList.getPlayers().get(i).getNumberOfStatuses(); ++st) {
                int chance = rand.nextInt(3);
                if (chance != 0) {
                    playerList.getPlayers().get(i).getChildItemList().remove(st);
                    playerListAdapter.notifyChildItemRemoved(i, st);
                }
            }
        }
    }

    @NonNull
    private String removeRandomPlayerStatuses(String taskDescription) {
        int i = random.nextInt(playerList.getPlayers().size());
        String name = playerList.getPlayers().get(i).getName();
        taskDescription = name + " " + taskDescription;
        int count = playerList.getPlayers().get(i).getNumberOfStatuses();
        playerListAdapter.notifyChildItemRangeRemoved(i, 0, count);
        playerList.getPlayers().get(i).removeAllStatuses();
        return taskDescription;
    }

    private void removeAllStatusesFromGame() {
        for (int i = 0; i < playerList.getPlayers().size(); ++i) {
            int count = playerList.getPlayers().get(i).getNumberOfStatuses();
            playerListAdapter.notifyChildItemRangeRemoved(i, 0, count);
            playerList.getPlayers().get(i).removeAllStatuses();
        }
    }

    private void displayTask(String taskHeading, String taskDescription) {
        TextView tv = (TextView) findViewById(R.id.a_main_tv_task_heading);
        tv.setText(taskHeading);
        TextView jtv = (TextView) findViewById(R.id.a_main_jtv_task_description);
        jtv.setText(taskDescription);
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks) + 1;
        while (usedTasks.contains(taskId)) {
            taskId = random.nextInt(numberOfTasks) + 1;
        }
        return taskId;
    }
}