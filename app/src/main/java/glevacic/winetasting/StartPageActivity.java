package glevacic.winetasting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    @Click(R.id.buttonStartPage_start)
    void startMainActivity() {
        Intent intent = new Intent(this, LegalAgeActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.buttonStartPage_rules)
    void startRulesActivity() {
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }
}
