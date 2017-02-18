package info.dylansymons.fpfrhelper.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerList;

public class GameActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYER_LIST = "PLAYER_LIST";
    private PlayerList mPlayerList;
    private Button mNextButton;
    private GridView mActionView;
    private Player mCurrentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                mPlayerList = (PlayerList) extras.getSerializable(EXTRA_PLAYER_LIST);
            }
        } else {
            mPlayerList = (PlayerList) savedInstanceState.getSerializable(EXTRA_PLAYER_LIST);
        }

        if (mPlayerList != null) {
            mNextButton = (Button) findViewById(R.id.btn_next_player);
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    endTurn();
                }
            });
            mActionView = (GridView) findViewById(R.id.gv_actions);
            mActionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Firefighter.Action action =
                            (Firefighter.Action) adapterView.getItemAtPosition(i);
                    doAction(action);
                }
            });
            setPlayer(mPlayerList.getCurrent());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_PLAYER_LIST, mPlayerList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPlayerList = (PlayerList) savedInstanceState.getSerializable(EXTRA_PLAYER_LIST);
        if (mPlayerList != null) {
            setPlayer(mPlayerList.getCurrent());
        }
    }

    private void doAction(Firefighter.Action action) {
        mCurrentPlayer.perform(action);
        updateActions();
    }

    private void endTurn() {
        setPlayer(mPlayerList.getNext());
    }

    private void setPlayer(Player player) {
        mCurrentPlayer = player;
        int bgColor = player.getColour();
        findViewById(R.id.activity_game).setBackgroundColor(bgColor);

        TextView firefighterView = (TextView) findViewById(R.id.tv_firefighter_name);
        firefighterView.setText(player.getFirefighterTitle());

        TextView playerView = (TextView) findViewById(R.id.tv_player_name);
        playerView.setText(player.getName());

        String nextPlayer = mPlayerList.peekNext().getFirefighterTitle();
        String nextLabel = getResources().getString(R.string.next_player, nextPlayer);
        mNextButton.setText(nextLabel);

        updateActions();
    }

    private void updateActions() {
        TextView ap = (TextView) findViewById(R.id.tv_ap);
        int apCount = mCurrentPlayer.getAp();
        String apString = getResources().getString(R.string.ap, apCount);
        ap.setText(apString);

        TextView bonusAp = (TextView) findViewById(R.id.tv_ap_bonus);
        TextView bonusApLabel = (TextView) findViewById(R.id.tv_ap_bonus_label);
        String bonusApAction = mCurrentPlayer.getFirefighter().getBonusApLabel();
        if (bonusApAction != null) {
            int bonusApCount = mCurrentPlayer.getBonusAp();
            String bonusApString = getResources().getString(R.string.ap, bonusApCount);
            bonusAp.setText(bonusApString);
            bonusApLabel.setText(bonusApAction);
        } else {
            bonusAp.setText("");
            bonusApLabel.setText("");
        }

        ActionAdapter adapter = new ActionAdapter(this, mCurrentPlayer);
        mActionView.setAdapter(adapter);
    }
}
