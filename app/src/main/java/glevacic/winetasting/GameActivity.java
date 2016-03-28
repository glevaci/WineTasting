package glevacic.winetasting;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@EActivity
public class GameActivity extends AppCompatActivity {

    private static final String TABLE = "tasks";
    private static final String KEY_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TYPE = "type";
    private static final String[] COLUMNS = { KEY_DESCRIPTION, KEY_TYPE };

    private SQLiteDatabase database;
    private int numberOfTasks;
    private Set<Integer> usedTasks = new HashSet<>();
    private Random random = new Random();

    private static List<Player> players = new ArrayList<>();

    public static void removePlayer(String playerName) {
        for (Player player : players)
            if (player.getName().equals(playerName))
                players.remove(player);
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static List<Player> getAllPlayers() {
        return players;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (players.isEmpty()) {
            Intent intent = new Intent(this, AddPlayersActivity_.class);
            startActivityForResult(intent, RESULT_OK);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getReadableDatabase();
        numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);

        displayNextTask();      // display first task
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
        TextView txtView = (TextView) findViewById(R.id.textViewGame_task);
        txtView.setText(cursor.getString(0));
        cursor.close();
    }

    private int getNextTaskId(int numberOfTasks) {
        // indices in table start from 1
        int taskId = random.nextInt(numberOfTasks)+1;
        while (usedTasks.contains(taskId))
            taskId = random.nextInt(numberOfTasks)+1;
        return taskId;
    }
}