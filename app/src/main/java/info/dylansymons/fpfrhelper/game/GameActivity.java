package info.dylansymons.fpfrhelper.game;

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Stack;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.database.GameContract;
import info.dylansymons.fpfrhelper.database.GameDbHelper;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.player.ActionAdapter;
import info.dylansymons.fpfrhelper.player.Player;

public class GameActivity extends AppCompatActivity
        implements ChangeFirefighterDialogFragment.Callback {
    public static final String EXTRA_GAME_ID = "game_id";
    private static final String EXTRA_SNACK_NAMES = "snack_names";
    private ActionAdapter mAdapter;
    private Button mNextButton;
    private GridView mActionView;
    private Stack<Snackbar> mSnacks;
    private ArrayList<String> mSnackNames;
    private boolean showingCrewChange = false;
    private AdView mAdView;

    private SQLiteDatabase db;
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new GameDbHelper(this).getWritableDatabase();
        setContentView(R.layout.activity_game);
        displayAd();
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                restoreGame(extras.getLong(EXTRA_GAME_ID));
            }
        } else {
            restoreGame(savedInstanceState.getLong(EXTRA_GAME_ID));
        }

        if (mGame != null) {
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
            mGame.getNext();
            setPlayer();
        }

        mSnacks = new Stack<>();
        mSnackNames = new ArrayList<>();
    }

    private void restoreGame(long gameId) {
        mGame = GameContract.restore(db, gameId);
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
        GameContract.save(db, mGame);
        outState.putSerializable(EXTRA_GAME_ID, mGame.getId());
        outState.putStringArrayList(EXTRA_SNACK_NAMES, mSnackNames);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mGame = GameContract.restore(db, savedInstanceState.getInt(EXTRA_GAME_ID));
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
        final String actionName = action.getName();
        if (action.equals(Firefighter.Action.CREW_CHANGE)) {
            showCrewChangeDialog();
        } else {
            mGame.getCurrent().perform(action);
            mSnackNames.add(actionName);
            addSnackBar(actionName);
            mSnacks.peek().show();
            updateActions();
        }
    }

    private void showCrewChangeDialog() {
        showingCrewChange = true;
        DialogFragment fragment = ChangeFirefighterDialogFragment.newInstance(
                mGame.getFirefighterList(), mGame.getCurrent(), this);
        fragment.show(getFragmentManager(), "dialog");
    }

    private void undoAction() {
        mSnacks.pop();
        Player currentPlayer = mGame.getCurrent();
        String name = mSnackNames.remove(mSnackNames.size() - 1);
        if (!name.equals(Firefighter.Action.CREW_CHANGE.getName())) {
            currentPlayer.undoAction();
            setTitleText();
            updateActions();
        } else {
            currentPlayer.undoCrewChange(mGame.getFirefighterList());
            setPlayer();
        }
        if (!mSnacks.isEmpty()) {
            mSnacks.peek().show();
        }

    }

    private void endTurn() {
        if (!mSnacks.isEmpty()) {
            mSnacks.pop().dismiss();
            mSnacks.clear();
        }
        mGame.getNext();
        setPlayer();
    }

    private void setTitleText() {
        Player currentPlayer = mGame.getCurrent();
        TextView firefighterView = (TextView) findViewById(R.id.tv_firefighter_name);
        firefighterView.setText(currentPlayer.getFirefighterTitle());

        TextView playerView = (TextView) findViewById(R.id.tv_player_name);
        playerView.setText(currentPlayer.getName());
    }

    private void setPlayer() {
        Player currentPlayer = mGame.getCurrent();
        int bgColor = currentPlayer.getColour();
        findViewById(R.id.activity_game).setBackgroundColor(bgColor);

        String nextPlayer = mGame.peekNext().getFirefighterTitle();
        String nextLabel = getResources().getString(R.string.next_player, nextPlayer);
        mNextButton.setText(nextLabel);

        setTitleText();
        mAdapter.setPlayer(currentPlayer);
        updateActions();
    }

    private void updateActions() {
        Player currentPlayer = mGame.getCurrent();
        TextView ap = (TextView) findViewById(R.id.tv_ap);
        int apCount = currentPlayer.getAp();
        String apString = getResources().getString(R.string.ap, apCount);
        ap.setText(apString);

        TextView bonusAp = (TextView) findViewById(R.id.tv_ap_bonus);
        TextView bonusApLabel = (TextView) findViewById(R.id.tv_ap_bonus_label);
        String bonusApAction = currentPlayer.getFirefighter().getBonusApLabel();
        if (bonusApAction != null) {
            int bonusApCount = currentPlayer.getBonusAp();
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
        Firefighter originalFirefighter = player.getFirefighter();
        player.crewChange(newFirefighter);
        mGame.getFirefighterList().setChosen(player.getFirefighter(), true);
        mGame.getFirefighterList().setChosen(originalFirefighter, false);
        String actionName = Firefighter.Action.CREW_CHANGE.getName();
        mSnackNames.add(actionName);
        addSnackBar(actionName);
        mSnacks.peek().show();
        setPlayer();
    }

    private void displayAd() {
        mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("77442A7F1A4FD6E2660582FD97CD6707")
                .addTestDevice("48F5874A64E7405B7BC2AB217DC30692")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdView.setVisibility(View.GONE);
            }
        });
        mAdView.loadAd(adRequest);
    }
}
