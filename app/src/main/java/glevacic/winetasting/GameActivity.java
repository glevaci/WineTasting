package glevacic.winetasting;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@EActivity
public class GameActivity extends AppCompatActivity {

    private static final String TABLE = "tasks";
    private static final String KEY_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TYPE = "type";
    private static final String[] COLUMNS = { KEY_DESCRIPTION, KEY_TYPE };

    private static Set<Integer> usedTasks = new HashSet<>();
    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final int numberOfTasks = (int) DatabaseUtils.queryNumEntries(database, TABLE);

        // display first task
        displayNextTask(numberOfTasks, database);

        Button buttonNext = (Button) findViewById(R.id.buttonGame_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextTask(numberOfTasks, database);
            }
        });
    }

    private void displayNextTask(int numberOfTasks, SQLiteDatabase database) {

        if (usedTasks.size() == numberOfTasks)
            usedTasks.clear();

        int taskId = getNextTaskId(numberOfTasks);
        usedTasks.add(taskId);

        // id is autoincremented
        // data is never deleted from table, so this cursor should never be null or empty
        Cursor cursor = database.query(TABLE,       // table
                            COLUMNS,                // column names
                            KEY_ID + " = ?",        // select
                            new String[] { String.valueOf(taskId) }, // selections args
                            null,                   // group by
                            null,                   // having
                            null,                   // order by
                            null);                  // limit

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