package info.dylansymons.fpfrhelper.game;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerList;

public class GameActivity extends AppCompatActivity
        implements ChangeFirefighterDialogFragment.Callback {
    public static final String EXTRA_PLAYER_LIST = "players";
    public static final String EXTRA_FIREFIGHTER_LIST = "firefighters";
    private static final String EXTRA_SNACK_NAMES = "snack_names";
    private static PlayerList mPlayerList;
    private static FirefighterList mFirefighterList;
    private static Player mCurrentPlayer;
    private ActionAdapter mAdapter;
    private Button mNextButton;
    private GridView mActionView;
    private Stack<Snackbar> mSnacks;
    private ArrayList<String> mSnackNames;
    private boolean showingCrewChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                mPlayerList = (PlayerList) extras.getSerializable(EXTRA_PLAYER_LIST);
                mFirefighterList = (FirefighterList) extras.getSerializable(EXTRA_FIREFIGHTER_LIST);
                mCurrentPlayer = null;
            }
        } else {
            mPlayerList = (PlayerList) savedInstanceState.getSerializable(EXTRA_PLAYER_LIST);
            mFirefighterList = (FirefighterList)
                    savedInstanceState.getSerializable(EXTRA_FIREFIGHTER_LIST);
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
            mAdapter = new ActionAdapter(this);
            mActionView.setAdapter(mAdapter);
            setPlayer(mPlayerList.getCurrent());
        }

        mSnacks = new Stack<>();
        mSnackNames = new ArrayList<>();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mActionView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_PLAYER_LIST, mPlayerList);
        outState.putSerializable(EXTRA_FIREFIGHTER_LIST, mFirefighterList);
        outState.putStringArrayList(EXTRA_SNACK_NAMES, mSnackNames);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPlayerList = (PlayerList) savedInstanceState.getSerializable(EXTRA_PLAYER_LIST);
        if (mPlayerList != null) {
            setPlayer(mPlayerList.getCurrent());
        }
        mFirefighterList = (FirefighterList)
                savedInstanceState.getSerializable(EXTRA_FIREFIGHTER_LIST);
        mSnackNames = savedInstanceState.getStringArrayList(EXTRA_SNACK_NAMES);
        if (mSnackNames != null) {
            for (String snackName : mSnackNames) {
                addSnackBar(snackName);
            }
        } else {
            mSnackNames = new ArrayList<>();
        }
        if (!mSnacks.isEmpty()) {
            mSnacks.peek().show();
        }
        if (showingCrewChange) {
            showCrewChangeDialog();
        }
    }

    private void addSnackBar(String snackName) {
        mSnacks.push(Snackbar
                .make(findViewById(R.id.gv_actions), snackName,
                        Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undoAction();
                    }
                }));
    }

    private void doAction(Firefighter.Action action) {
        final String actionName = action.getShortDescription();
        if (action.equals(Firefighter.Action.CREW_CHANGE)) {
            showCrewChangeDialog();
        } else {
            mCurrentPlayer.perform(action);
            mSnackNames.add(actionName);
            addSnackBar(actionName);
            mSnacks.peek().show();
            updateActions();
        }
    }

    private void showCrewChangeDialog() {
        showingCrewChange = true;
        DialogFragment fragment = ChangeFirefighterDialogFragment.newInstance(
                mFirefighterList, mCurrentPlayer, this);
        fragment.show(getFragmentManager(), "dialog");
    }

    private void undoAction() {
        mSnacks.pop();
        String name = mSnackNames.remove(mSnackNames.size() - 1);
        if (!name.equals(Firefighter.Action.CREW_CHANGE.getShortDescription())) {
            mCurrentPlayer.undoAction();
            setTitleText();
            updateActions();
        } else {
            mCurrentPlayer.undoCrewChange(mFirefighterList);
            setPlayer(mCurrentPlayer);
        }
        if (!mSnacks.isEmpty()) {
            mSnacks.peek().show();
        }

    }

    private void endTurn() {
        if (!mSnacks.isEmpty()) {
            mSnacks.pop().dismiss();
            mSnacks.empty();
        }
        setPlayer(mPlayerList.getNext());
    }

    private void setTitleText() {
        TextView firefighterView = (TextView) findViewById(R.id.tv_firefighter_name);
        firefighterView.setText(mCurrentPlayer.getFirefighterTitle());

        TextView playerView = (TextView) findViewById(R.id.tv_player_name);
        playerView.setText(mCurrentPlayer.getName());
    }

    private void setPlayer(Player player) {
        mCurrentPlayer = player;
        int bgColor = player.getColour();
        findViewById(R.id.activity_game).setBackgroundColor(bgColor);

        String nextPlayer = mPlayerList.peekNext().getFirefighterTitle();
        String nextLabel = getResources().getString(R.string.next_player, nextPlayer);
        mNextButton.setText(nextLabel);

        setTitleText();
        mAdapter.setPlayer(mCurrentPlayer);
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

        mAdapter.update();
    }

    @Override
    public void changeFirefighter(Player player, Firefighter newFirefighter) {
        showingCrewChange = false;
        Firefighter originalFirefighter = mCurrentPlayer.getFirefighter();
        mCurrentPlayer.crewChange(newFirefighter);
        mFirefighterList.remove(mCurrentPlayer.getFirefighter());
        mFirefighterList.add(originalFirefighter);
        String actionName = Firefighter.Action.CREW_CHANGE.getShortDescription();
        mSnackNames.add(actionName);
        addSnackBar(actionName);
        mSnacks.peek().show();
        setPlayer(player);
    }
}
