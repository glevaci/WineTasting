package glevacic.winetasting.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import glevacic.winetasting.R;
import glevacic.winetasting.utils.Status;
import glevacic.winetasting.utils.DatabaseHelper;
import glevacic.winetasting.utils.Player;
import glevacic.winetasting.utils.PlayerList;
import glevacic.winetasting.utils.PlayerListAdapter;

/* TODO
* dodati globalne statuse (type == 7)
* dodati popup za dodavanje igrača na početku
* preinačiti Player tj PlayerListItem?
* dodati context menu za igrače - brisanje, promjena imena
* dodati popis aktivnih statusa za trenutnog igrača?
*/

@EActivity
public class MainActivity extends AppCompatActivity {

    private static final String TABLE = "tasks";
    private static final String ID = "_id";
    public static final String HEADING = "heading";
    private static final String DESCRIPTION = "description";
    private static final String CARD_TYPE = "type";
    private static final String[] COLUMNS = {HEADING, DESCRIPTION, CARD_TYPE};
    private static final int STATUS = 3;
    private static final int REMOVE_ALL_STATUSES = 4;
    private static final int REMOVE_RANDOM_PLAYER_STATUSES = 5;
    private static final int REMOVE_RANDOM_STATUSES_FROM_GAME = 6;

    private PlayerListAdapter playerListAdapter;
    private PlayerList playerList;

    private SQLiteDatabase database;
    private int numberOfTasks;
    private Set<Integer> usedTasks;
    private Random random;

    private boolean waitingForPlayers = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawer();
        setUpDatabase();
        setInitialTaskData();
        displayInitialMessage();
    }

    private void displayInitialMessage() {
        // TODO show dialog
    }

    private void setInitialTaskData() {
        usedTasks = new HashSet<>();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        random = new Random();
    }

    private void setUpDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getReadableDatabase();
    }

    private void setUpDrawer() {

        playerList = new PlayerList();
        playerListAdapter = new PlayerListAdapter(this, playerList.getPlayers());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dr_rv_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playerListAdapter);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.a_main_drawer_layout);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
            }

            @Override
            public void onDrawerClosed(View view) {
                if (waitingForPlayers && playerList.getPlayers().size() > 0) {
                    waitingForPlayers = false;
                    startGame();
                }
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });

        drawer.openDrawer(Gravity.LEFT);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        showInfoDialog("Dodavanje igrača", "Prije početka igre trebate dodati igrače!");
    }

    @Click(R.id.dr_btn_new_player)
    public void showNewPlayerDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_player, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Novi igrač");
        dialogBuilder.setMessage("Ime igrača:");

        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_editText);
        final TextView textView = (TextView) dialogView.findViewById(R.id.dialog_textViewWarning);

        dialogBuilder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialogOkButtonClicked(editText);
            }
        });

        dialogBuilder.setNegativeButton("Odustani", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // OK button must be initially disabled because editText is empty
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        editText.addTextChangedListener(createTextWatcher(editText, textView, dialog));
    }

    private void dialogOkButtonClicked(EditText editText) {
        int i = playerList.getPlayers().size();
        Player player = new Player(editText.getText().toString());
        playerList.addPlayer(player);
        playerListAdapter.notifyParentItemInserted(i);

        if (i == 0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.a_main_drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private TextWatcher createTextWatcher(final EditText editText,
                                          final TextView textView,
                                          final AlertDialog dialog) {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPlayerName(editText, textView, dialog);
            }
        };
    }

    private void checkPlayerName(EditText editText, TextView textView, AlertDialog dialog) {
        if (editText.getText().toString().isEmpty()) {
            textView.setText(R.string.dialog_warning_name_empty);
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            return;
        }

        List<Player> players = playerList.getPlayers();
        if (!players.isEmpty()) {
            for (Player player : players) {
                if (player.getName().equals(editText.getText().toString())) {
                    textView.setText(R.string.dialog_warning_name_exists);
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

        if (usedTasks.size() == numberOfTasks)
            restartGame();

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
        cursor.close();
    }

    private void restartGame() {
        usedTasks.clear();
        removeAllStatusesFromGame();
        showInfoDialog("Kraj", "Sve kartice su iskorištene, igra kreće ispočetka.");
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
        taskDescription = name  + " " + taskDescription;
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

    private void showInfoDialog(String heading, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(heading);
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void displayTask(String taskHeading, String taskDescription) {
        TextView tv = (TextView) findViewById(R.id.a_main_tv_task_heading);
        tv.setText(taskHeading);
        TextView jtv = (TextView) findViewById(R.id.a_main_jtv_task_description);
        jtv.setText(taskDescription + "\n");
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks) + 1;
        while (usedTasks.contains(taskId))
            taskId = random.nextInt(numberOfTasks) + 1;
        return taskId;
    }
}