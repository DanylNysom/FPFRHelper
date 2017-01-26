package info.dylansymons.fpfrhelper.game.management;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.SettingsActivity;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerList;
import info.dylansymons.fpfrhelper.player.PlayerListViewAdapter;


public class NewGameActivity extends AppCompatActivity {
    private static final int DEFAULT_PLAYER_COUNT = 6;
    private PlayerListViewAdapter mListViewAdapter;
    private PlayerList mPlayerList;
    private RecyclerView mRecyclerView;
    private PlayerListViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private ArrayList<String> firefighterList;
    private HashSet<String> chosenFirefighters;
    private FloatingActionButton fab;
    private Random rng = new Random();
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.pref_expansions, false);

        startButton = (Button)findViewById(R.id.btn_start);

        chosenFirefighters = new HashSet<>(DEFAULT_PLAYER_COUNT);

        createPlayerList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = newPlayerDialog();
                dialog.show();
            }
        });
    }

    private void createPlayerList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_players);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPlayerList = new PlayerList(DEFAULT_PLAYER_COUNT);
        mAdapter = new PlayerListViewAdapter(mPlayerList);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                String firefighter = mAdapter.remove(swipedPosition);
                chosenFirefighters.remove(firefighter);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private AlertDialog newPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_new_player, null));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog)dialogInterface;
                        addPlayer(dialog);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        final ListView ffList = (ListView)dialog.findViewById(R.id.lst_firefighter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, getFirefighterList());
        ffList.setAdapter(adapter);
        ffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ffList.setItemChecked(i, true);
            }
        });
        ffList.setItemChecked(0, true);
        return dialog;
    }

    private void addPlayer(Dialog dialog) {
        EditText nameBox = (EditText)dialog.findViewById(R.id.et_player_name);
        String name = nameBox.getText().toString();

        ListView ffList = (ListView)dialog.findViewById(R.id.lst_firefighter);
        int checkedPosition = ffList.getCheckedItemPosition();
        String random = getResources().getString(R.string.random);
        String firefighter = ffList.getItemAtPosition(checkedPosition).toString();
        if(firefighter.equals(random)) {
            checkedPosition = 1 + rng.nextInt(ffList.getAdapter().getCount() - 1);
            firefighter = ffList.getItemAtPosition(checkedPosition).toString();
        }
        chosenFirefighters.add(firefighter);
        if (ffList.getAdapter().getCount() == 1) {
            fab.setVisibility(View.INVISIBLE);
        }

        mPlayerList.add(new Player(name, firefighter));
        mAdapter.notifyItemInserted(mPlayerList.size() - 1);
        startButton.setEnabled(true);
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
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private String[] getFirefighterList() {
        ArrayList<String> firefighterList = new ArrayList<>(
                Arrays.asList(FirefighterList.getList(this)));
        firefighterList.removeAll(chosenFirefighters);
        if (firefighterList.size() > 1) {
            firefighterList.add(0, getResources().getString(R.string.random));
        }
        return firefighterList.toArray(new String[0]);
    }
}
