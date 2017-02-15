package info.dylansymons.fpfrhelper.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerList;

public class GameActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYERLIST = "PLAYER_LIST";
    private PlayerList mPlayerList;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                mPlayerList = (PlayerList) extras.getSerializable(EXTRA_PLAYERLIST);
            }
        } else {
            mPlayerList = (PlayerList) savedInstanceState.getSerializable(EXTRA_PLAYERLIST);
        }
        if (mPlayerList != null) {
            nextButton = (Button) findViewById(R.id.btn_next_player);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    endTurn();
                }
            });
            setPlayer(mPlayerList.getCurrent());
        }
    }

    private void endTurn() {
        setPlayer(mPlayerList.getNext());
    }

    private void setPlayer(Player player) {
        int bgColor = player.getColour();
        findViewById(R.id.activity_game).setBackgroundColor(bgColor);

        TextView firefighterView = (TextView) findViewById(R.id.tv_firefighter_name);
        firefighterView.setText(player.getFirefighterTitle());

        TextView playerView = (TextView) findViewById(R.id.tv_player_name);
        playerView.setText(player.getName());

        TextView ap = (TextView) findViewById(R.id.tv_ap);
        int apCount = player.getAp();
        String apLabel = getResources().getString(R.string.ap, apCount);
        ap.setText(apLabel);

        String nextPlayer = mPlayerList.peekNext().getFirefighterTitle();
        String nextLabel = getResources().getString(R.string.next_player, nextPlayer);
        nextButton.setText(nextLabel);
    }
}
