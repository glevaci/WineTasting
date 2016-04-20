package glevacic.winetasting.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import glevacic.winetasting.R;
import glevacic.winetasting.utils.ActiveStatus;
import glevacic.winetasting.utils.DatabaseHelper;
import glevacic.winetasting.utils.Player;
import glevacic.winetasting.utils.PlayerList;
import glevacic.winetasting.utils.PlayerListAdapter;

@EActivity
public class MainActivity extends AppCompatActivity {

    private static final String TABLE = "tasks";
    private static final String ID = "_id";
    public static final String HEADING = "heading";
    private static final String DESCRIPTION = "description";
    private static final String CARD_TYPE = "type";
    private static final String[] COLUMNS = {HEADING, DESCRIPTION, CARD_TYPE};
    private static final int STATUS = 3;

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
        Button button = (Button) findViewById(R.id.a_main_btn_next);
    }

    private void setInitialTaskData() {
        usedTasks = new HashSet<>();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        random = new Random();
    }

    private void displayInitialMessage() {
        TextView tv = (TextView) findViewById(R.id.a_main_jtv_task_description);
        tv.setText(R.string.initial_message);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.a_main_drawer_layout);
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
        int index = playerList.getPlayers().size();
        Player player = new Player(editText.getText().toString());
        playerList.addPlayer(player);
        playerListAdapter.notifyParentItemInserted(index);
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

        int taskId = getNextTaskId(numberOfTasks);
        usedTasks.add(taskId);

        // id is autoincremented, data is never deleted from table
        // => this cursor will never be null or empty
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
        displayTask(taskHeading, taskDescription);

        int cardType = cursor.getInt(cursor.getColumnIndex(CARD_TYPE));
        if (cardType == STATUS)
            applyStatusCard(taskHeading, taskDescription);

        cursor.close();
    }

    private void displayTask(String taskHeading, String taskDescription) {
        TextView tv = (TextView) findViewById(R.id.a_main_tv_task_heading);
        tv.setText(taskHeading);
        TextView jtv = (TextView) findViewById(R.id.a_main_jtv_task_description);
        jtv.setText(taskDescription + "\n");
    }

    private void applyStatusCard(String taskHeading, String taskDescription) {
        int parentIndex = playerList.getCurrentIndex();
        int childIndex = playerList.getCurrentPlayer().getChildItemList().size();

        ActiveStatus status = new ActiveStatus(taskHeading, taskDescription);
        playerList.getCurrentPlayer().addActiveStatus(status);
        playerListAdapter.notifyChildItemInserted(parentIndex, childIndex);
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks) + 1;
        while (usedTasks.contains(taskId))
            taskId = random.nextInt(numberOfTasks) + 1;

        return taskId;
    }
}