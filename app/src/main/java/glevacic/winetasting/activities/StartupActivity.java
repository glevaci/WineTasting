package glevacic.winetasting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import glevacic.winetasting.R;

@EActivity
public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    @Click(R.id.a_startup_btn_start)
    void startNewGame() {
        Intent intent = new Intent(this, MainActivity_.class);
        intent.putExtra("newGame", true);
        startActivity(intent);
    }

    @Click(R.id.a_startup_btn_continue)
    void continueGame() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.a_startup_btn_instructions)
    void startRulesActivity() {
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }
}
