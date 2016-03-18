package glevacic.winetasting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public class LegalAgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_age);
    }

    @Click(R.id.buttonLegalAge_agree)
    void agree() {
        Intent intent = new Intent(this, AddPlayersActivity_.class);
        startActivity(intent);
    }
}
