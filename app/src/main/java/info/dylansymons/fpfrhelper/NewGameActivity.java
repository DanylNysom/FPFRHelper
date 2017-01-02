package info.dylansymons.fpfrhelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Random;

import info.dylansymons.fpfrhelper.player.Player;


public class NewGameActivity extends AppCompatActivity {
    private ArrayAdapter<Player> playerArrayAdapter;
    private ArrayList<String> firefighterList;
    private FloatingActionButton fab;
    private Random rng = new Random();
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton = (Button)findViewById(R.id.btn_start);

        firefighterList = new ArrayList<>(
                Arrays.asList(getResources().getStringArray(R.array.firefighter_names)));
        firefighterList.add(0, getResources().getString(R.string.random));

        ListView characterListView = (ListView)findViewById(R.id.lv_characters);
        playerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        characterListView.setAdapter(playerArrayAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = newPlayerDialog();
                dialog.show();
            }
        });
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
                android.R.id.text1, firefighterList);
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
        int randomIndex = 0;
        if (checkedPosition == randomIndex) {
            checkedPosition = 1 + rng.nextInt(firefighterList.size() - 1);
        }
        String firefighter = ffList.getItemAtPosition(checkedPosition).toString();
        firefighterList.remove(firefighter);
        if (firefighterList.isEmpty()) {
            fab.setVisibility(View.INVISIBLE);
        }

        playerArrayAdapter.add(new Player(name, firefighter));
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
}
