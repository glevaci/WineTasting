package glevacic.winetasting.activities;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

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

    private boolean waitingForPlayers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerList = new PlayerList();
        Player g = new Player("G");

        ActiveStatus status = new ActiveStatus("jao", "daj radi");
        g.addActiveStatus(status);
        playerList.addPlayer(g);
        setUpDrawer();
        setUpDatabase();
        setInitialTaskData();
        //displayInitialMessage();
        Button button = (Button) findViewById(R.id.buttonGame_next);
        button.setEnabled(true);
        startNextRound();
    }

    private void setInitialTaskData() {
        usedTasks = new HashSet<>();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        random = new Random();
    }

    private void displayInitialMessage() {
        TextView txtView = (TextView) findViewById(R.id.main_textViewTaskDescription);
        txtView.setText(R.string.initialMessage);
    }

    private void setUpDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getReadableDatabase();
    }

    private void setUpDrawer() {

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.drawer);
        playerListAdapter = new PlayerListAdapter(MainActivity.this, playerList.getPlayers());


        expandableListView.setAdapter(playerListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("onGroupClick:", "worked");
                parent.collapseGroup(groupPosition);
                return false;
            }
        });

        int groupCount = playerListAdapter.getGroupCount();
/*
        for (int i = 0; i < groupCount; ++i)
            expandableListView.expandGroup(i);
*/
        //registerForContextMenu(expandableListView);
    }
/*
    @Click(R.id.buttonDrawer_newPlayer)
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
        Player player = new Player(editText.getText().toString());
        playerList.addPlayer(player);
        playerListAdapter.notifyDataSetChanged();
        if (waitingForPlayers) {
            waitingForPlayers = false;
            startGame();
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
            textView.setText(R.string.dialog_warning_empty);
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            return;
        }

        List<Player> players = playerList.getPlayers();
        if (!players.isEmpty()) {
            for (Player player : players) {
                if (player.getName().equals(editText.getText().toString())) {
                    textView.setText(R.string.dialog_warning_double);
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    return;
                }
            }
        }
        // if everything is ok
        textView.setText("");
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
    }*/

    private void startGame() {
        startNextRound();
        Button button = (Button) findViewById(R.id.buttonGame_next);
        button.setEnabled(true);
    }

    /*@Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

        String title = ((TextView) info.targetView).getText().toString();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
*/
    @Click(R.id.buttonGame_next)
    public void startNextRound() {
        getNextPlayer();
        getNewTask();
    }

    private void getNextPlayer() {
        Player player = playerList.getNextPlayer();
        TextView textView = (TextView) findViewById(R.id.main_textViewPlayer);
        textView.setText(player.getName());
        // TODO show list of player's statuses
    }

    private void getNewTask() {

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
        TextView txtView = (TextView) findViewById(R.id.main_textViewTaskHeading);
        txtView.setText(taskHeading);
        txtView = (TextView) findViewById(R.id.main_textViewTaskDescription);
        txtView.setText(taskDescription);
    }

    private void applyStatusCard(String taskHeading, String taskDescription) {
        ActiveStatus status = new ActiveStatus(taskHeading, taskDescription);
        playerList.getCurrentPlayer().addActiveStatus(status);
        playerListAdapter.notifyDataSetChanged();
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks) + 1;
        while (usedTasks.contains(taskId))
            taskId = random.nextInt(numberOfTasks) + 1;

        return taskId;
    }
}