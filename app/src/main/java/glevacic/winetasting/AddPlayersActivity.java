package glevacic.winetasting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class AddPlayersActivity extends AppCompatActivity {

    private EditText nameEditText;
    private static List<String> playerNames = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapter;

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changeButtonClickability();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        nameEditText = (EditText) findViewById(R.id.editTextAddPlayers_playerName);
        nameEditText.addTextChangedListener(textWatcher);

        ListView listView = (ListView) findViewById(R.id.listViewAddPlayers_existingPlayers);

        for (Player player  : GameActivity.getAllPlayers())
            playerNames.add(player.getName());

        arrayAdapter = new ArrayAdapter<String>(this,
                                                android.R.layout.simple_list_item_1,
                                                playerNames);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        if (playerNames.isEmpty()) {
            // TODO warn
        }

        else {
            setResult(1);
            finish();
        }
    }

    private void changeButtonClickability() {
        Button button = (Button) findViewById(R.id.buttonAddPlayers_add);
        String str = nameEditText.getText().toString();
        if (str.trim().isEmpty())
            button.setEnabled(false);
        else
            button.setEnabled(true);
    }

    @Click(R.id.buttonAddPlayers_add)
    public void addPlayer() {
        String name = nameEditText.getText().toString();
        Player player = new Player(name);
        GameActivity.addPlayer(player);
        nameEditText.getText().clear();
        playerNames.add(name);
        arrayAdapter.notifyDataSetChanged();
    }
}
