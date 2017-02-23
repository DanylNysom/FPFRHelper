package info.dylansymons.fpfrhelper.game.management;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.SettingsActivity;
import info.dylansymons.fpfrhelper.database.GameContract;
import info.dylansymons.fpfrhelper.database.GameDbHelper;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.game.Game;
import info.dylansymons.fpfrhelper.game.GameActivity;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerListViewAdapter;
import info.dylansymons.fpfrhelper.player.PlayerListViewAdapterCallback;

/**
 * An Activity used to create a new Game.
 */
public class NewGameActivity extends AppCompatActivity implements NewPlayerDialogFragmentCallback,
        PlayerListViewAdapterCallback {
    private final String INSTANCE_COLOUR_LIST = "colours";
    private final String INSTANCE_GAME_ID = "game_id";
    private SQLiteDatabase db;
    private InterstitialAd mInterstitialAd;
    /**
     * The adapter used to supply the RecyclerView
     */
    private PlayerListViewAdapter mAdapter;
    /**
     * The colors available to be selected for new players
     */
    private ArrayList<Integer> mColourList;
    /**
     * The action button used to add a new player
     */
    private FloatingActionButton fab;
    /**
     * The button that starts the game, launching a new GameActivity with the Game
     */
    private Button startButton;
    private AdView mAdView;
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new GameDbHelper(this).getWritableDatabase();
        setContentView(R.layout.activity_new_game);
        displayAd();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.pref_expansions, false);

        createStartButton();
        createColourList();
        createGame();
        loadInterstitial();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void createGame() {
        mGame = GameContract.create(db, Game.DEFAULT_NAME, this);
        createPlayerList();
    }

    private void loadInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        String unitId = getResources().getString(R.string.interstitial_ad_unit_id);
        mInterstitialAd.setAdUnitId(unitId);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startGame();
            }
        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("77442A7F1A4FD6E2660582FD97CD6707")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void displayAd() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("77442A7F1A4FD6E2660582FD97CD6707")
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

    private void showAddDialog() {
        DialogFragment fragment = NewPlayerDialogFragment.newInstance(
                mGame.getFirefighterList(), mColourList, this);
        fragment.show(getFragmentManager(), "dialog");
    }

    private void showEditDialog(Player player) {
        DialogFragment fragment = NewPlayerDialogFragment.newEditInstance(
                mGame.getFirefighterList(), mColourList, this, player);
        fragment.show(getFragmentManager(), "dialog");
    }

    /**
     * Initializes the list of colours {@link #mColourList} with the default colour choices, which
     * come from the game itself
     */
    private void createColourList() {
        mColourList = new ArrayList<>(6);
        int[] colors = new int[]{
                ContextCompat.getColor(this, android.R.color.holo_red_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.white),
                Color.YELLOW
        };
        for (int color : colors) {
            mColourList.add(color);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGame.checkExpansions(this);
        checkButtonEnableState();
    }

    private void checkButtonEnableState() {
        enableFab(!mGame.getFirefighterList().isEmpty() && !mColourList.isEmpty());
        startButton.setEnabled(mGame.getPlayerList().size() > 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        GameContract.save(db, mGame);
        outState.putLong(INSTANCE_GAME_ID, mGame.getId());
        outState.putIntegerArrayList(INSTANCE_COLOUR_LIST, mColourList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        long gameId = savedInstanceState.getLong(INSTANCE_GAME_ID);
        mGame = GameContract.restore(db, gameId);
        mColourList = savedInstanceState.getIntegerArrayList(INSTANCE_COLOUR_LIST);
        createPlayerList();
    }

    /**
     * Initializes the Start Button to, when clicked, start a GameActivity
     */
    private void createStartButton() {
        startButton = (Button)findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startGame();
                }
            }
        });
    }

    private void startGame() {
        GameContract.save(db, mGame);
        Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_GAME_ID, mGame.getId());
        startActivity(intent);
    }

    /**
     * Sets up the RecyclerView that will display to the user the list of Players currently in the
     * game, including initializing the backing list itself and the Adapter for the View
     */
    private void createPlayerList() {
        ArrayList<Player> players;
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_players);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mGame == null || mGame.getPlayerList() == null) {
            players = new ArrayList<>();
        } else {
            players = mGame.getPlayerList();
        }
        mAdapter = new PlayerListViewAdapter(players, this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                removePlayer(swipedPosition);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void addPlayer(String name, Firefighter firefighter, int color) {
        enableFab(false);
        mGame.getFirefighterList().remove(firefighter);

        mColourList.remove(Integer.valueOf(color));

        mGame.getPlayerList().add(new Player(name, firefighter, color));
        mAdapter.notifyItemInserted(mGame.getPlayerList().size() - 1);
        checkButtonEnableState();
    }

    public void editPlayer(String name, Firefighter firefighter, int color, Player player) {
        enableFab(false);
        int index = mGame.getPlayerList().indexOf(player);
        mGame.getFirefighterList().remove(firefighter);
        player.setFirefighter(firefighter);

        mColourList.remove(Integer.valueOf(color));
        player.setColour(color);

        player.setName(name);

        mAdapter.notifyItemChanged(index);
        checkButtonEnableState();
    }

    /**
     * Toggles the state of the floating action button that is used to add a new player.
     *
     * @param enable whether the FAB should be enabled or not. If this is true, the FAB will be
     *               visible to the user. If it is false, the FAB will become invisible.
     */
    private void enableFab(boolean enable) {
        if (enable) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void removePlayer(int position) {
        Player player = mAdapter.remove(position);
        mGame.getFirefighterList().add(player.getFirefighter());
        int color = player.getColour();
        mColourList.add(color);
        checkButtonEnableState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettings(MenuItem item) {
        if(item.isEnabled()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPlayerSelected(Player player) {
        showEditDialog(player);
    }
}
