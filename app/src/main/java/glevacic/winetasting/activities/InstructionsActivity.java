package glevacic.winetasting.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import glevacic.winetasting.R;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        String htmlPlayers = "<html><body style=\"text-align:justify\"> <p> %s </p> </body></Html>";
        WebView wvPlayersDescription = (WebView) findViewById(R.id.a_instructions_wv_players_description);
        String description = String.format(htmlPlayers, getResources().getString(R.string.instructions_players_description));
        wvPlayersDescription.loadData(description, "text/html; charset=utf-8", "utf-8");

        String htmlCards = "<html><body style=\"text-align:justify\"> " +
                "<p> %s </p>" +
                "<ul> " +
                        "<li> %s </li>" +
                        "<li> %s </li>" +
                        "<li> %s </li>" +
                "</ul></body></Html>";

        WebView wvCardsDescription = (WebView) findViewById(R.id.a_instructions_wv_cards_description);
        description = String.format(htmlCards,
                getResources().getString(R.string.instructions_cards_description),
                getResources().getString(R.string.instructions_card_action),
                getResources().getString(R.string.instructions_card_mandatory),
                getResources().getString(R.string.instructions_card_status)
        );
        wvCardsDescription.loadData(description, "text/html; charset=utf-8", "utf-8");
    }
}
