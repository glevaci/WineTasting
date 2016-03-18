package glevacic.winetasting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public class AddPlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
    }

    @Click(R.id.buttonAddPlayers_play)
    void play() {
        Intent intent = new Intent(this, GameActivity_.class);
        startActivity(intent);
    }
}
