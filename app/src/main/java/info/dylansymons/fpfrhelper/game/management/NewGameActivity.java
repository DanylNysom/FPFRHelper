package info.dylansymons.fpfrhelper.game.management;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.SettingsActivity;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.firefighter.FirefighterRandom;
import info.dylansymons.fpfrhelper.game.GameActivity;
import info.dylansymons.fpfrhelper.player.Player;
import info.dylansymons.fpfrhelper.player.PlayerList;
import info.dylansymons.fpfrhelper.player.PlayerListViewAdapter;


public class NewGameActivity extends AppCompatActivity {
    private static final int DEFAULT_PLAYER_COUNT = 6;
    private PlayerList mPlayerList;
    private RecyclerView mRecyclerView;
    private PlayerListViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Integer> mColourList;

    private HashSet<Firefighter> chosenFirefighters;
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

        chosenFirefighters = new HashSet<>(DEFAULT_PLAYER_COUNT);

        createStartButton();
        createPlayerList();
        createColourList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = newPlayerDialog();
                dialog.show();
            }
        });
    }

    private void createColourList() {
        mColourList = new ArrayList<>(6);
        mColourList.add(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        mColourList.add(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        mColourList.add(Color.BLUE);
        mColourList.add(Color.GREEN);
        mColourList.add(Color.BLACK);
        mColourList.add(Color.YELLOW);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getFirefighterList().length > 0) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
        startButton.setEnabled(!chosenFirefighters.isEmpty());
    }

    private void createStartButton() {
        startButton = (Button)findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayerList.trim();
                Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.EXTRA_PLAYERLIST, mPlayerList);
                startActivity(intent);
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
                removePlayer(swipedPosition);
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
        ArrayAdapter<Firefighter> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, getFirefighterList());
        if(ffList != null) {
            ffList.setAdapter(adapter);
            ffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ffList.setItemChecked(i, true);
                }
            });
            ffList.setItemChecked(0, true);
        }

        RadioGroup colors = (RadioGroup) dialog.findViewById(R.id.rg_colors);
        if (colors != null) {
            for (int color : mColourList) {
                AppCompatRadioButton button = new AppCompatRadioButton(this);
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_checked},
                                new int[]{android.R.attr.state_checked}
                        },
                        new int[]{
                                color,
                                color
                        }
                );
                button.setSupportButtonTintList(colorStateList);
                colors.addView(button);
            }
            int checkPosition = new Random().nextInt(mColourList.size());
            AppCompatRadioButton checkView = (AppCompatRadioButton) colors.getChildAt(checkPosition);
            colors.check(checkView.getId());
        }

        return dialog;
    }

    private void addPlayer(Dialog dialog) {
        EditText nameBox = (EditText)dialog.findViewById(R.id.et_player_name);
        String name = nameBox.getText().toString();

        ListView ffList = (ListView)dialog.findViewById(R.id.lst_firefighter);
        int checkedPosition = ffList.getCheckedItemPosition();
        Firefighter firefighter = (Firefighter) ffList.getItemAtPosition(checkedPosition);
        if(firefighter instanceof FirefighterRandom) {
            checkedPosition = 1 + rng.nextInt(ffList.getAdapter().getCount() - 1);
            firefighter = (Firefighter) ffList.getItemAtPosition(checkedPosition);
        }
        chosenFirefighters.add(firefighter);
        if (ffList.getAdapter().getCount() == 1) {
            enableFab(false);
        }

        RadioGroup colors = (RadioGroup) dialog.findViewById(R.id.rg_colors);
        AppCompatRadioButton checkedButton = (AppCompatRadioButton)
                dialog.findViewById(colors.getCheckedRadioButtonId());
        int color = checkedButton.getSupportButtonTintList().getDefaultColor();
        mColourList.remove(Integer.valueOf(color));
        if (mColourList.isEmpty()) {
            enableFab(false);
        }
        if (color == Color.BLACK) {
            color = Color.WHITE;
        }
        System.err.println("colour is: " + color);

        mPlayerList.add(new Player(name, firefighter, color));
        mAdapter.notifyItemInserted(mPlayerList.size() - 1);
        startButton.setEnabled(true);
    }

    private void enableFab(boolean enable) {
        if (enable) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void removePlayer(int position) {
        Player player = mAdapter.remove(position);
        chosenFirefighters.remove(player.getFirefighter());
        int color = player.getColour();
        if (color == Color.WHITE) {
            color = Color.BLACK;
        }
        mColourList.add(color);
        if (chosenFirefighters.isEmpty()) {
            startButton.setEnabled(false);
        } else {
            enableFab(true);
        }
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

    private Firefighter[] getFirefighterList() {
        ArrayList<Firefighter> firefighterList = new ArrayList<>(
                Arrays.asList(FirefighterList.getList(this)));
        for(Firefighter toRemove : chosenFirefighters) {
            firefighterList.remove(toRemove);
        }
//        firefighterList.removeAll(chosenFirefighters);
        if (firefighterList.size() > 1) {
            firefighterList.add(0, new FirefighterRandom());
        }
        return firefighterList.toArray(new Firefighter[0]);
    }
}
