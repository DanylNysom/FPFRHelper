package info.dylansymons.fpfrhelper.game.management;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Random;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.firefighter.FirefighterRandom;
import info.dylansymons.fpfrhelper.player.Player;

public class NewPlayerDialogFragment extends DialogFragment {
    private static final String INSTANCE_PLAYER = "player";
    private static String INSTANCE_FIREFIGHTER_LIST = "firefighters";
    private static String INSTANCE_COLOUR_LIST = "colours";
    private static String INSTANCE_CALLBACK = "callback";

    private FirefighterList mFirefighters;
    private ArrayList<Integer> mColourList;
    private NewPlayerDialogFragmentCallback mCallback;
    private Player mPlayer;

    static NewPlayerDialogFragment newInstance(FirefighterList firefighterList,
                                               ArrayList<Integer> colourList,
                                               NewPlayerDialogFragmentCallback callback) {
        NewPlayerDialogFragment frag = new NewPlayerDialogFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(INSTANCE_COLOUR_LIST, colourList);
        args.putSerializable(INSTANCE_FIREFIGHTER_LIST, firefighterList);
        args.putSerializable(INSTANCE_CALLBACK, callback);
        frag.setArguments(args);
        frag.setRetainInstance(true);
        return frag;
    }

    static NewPlayerDialogFragment newEditInstance(FirefighterList firefighterList,
                                                   ArrayList<Integer> colourList,
                                                   NewPlayerDialogFragmentCallback callback,
                                                   Player player) {
        NewPlayerDialogFragment frag = NewPlayerDialogFragment.newInstance(firefighterList,
                colourList, callback);
        frag.getArguments().putSerializable(INSTANCE_PLAYER, player);
        return frag;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        mColourList = savedInstanceState.getIntegerArrayList(INSTANCE_COLOUR_LIST);
        mFirefighters = (FirefighterList)
                savedInstanceState.getSerializable(INSTANCE_FIREFIGHTER_LIST);
        mCallback = (NewPlayerDialogFragmentCallback)
                savedInstanceState.getSerializable(INSTANCE_CALLBACK);
        mPlayer = (Player) savedInstanceState.getSerializable(INSTANCE_PLAYER);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_player,
                null);

        if (mPlayer != null) {
            mFirefighters.add(mPlayer.getFirefighter());
            mColourList.add(mPlayer.getColour());
            ((EditText) view.findViewById(R.id.et_player_name)).setText(mPlayer.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Dialog dialog = (Dialog) dialogInterface;
                addPlayer(dialog);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (mPlayer != null) {
                    mFirefighters.remove(mPlayer.getFirefighter());
                    mColourList.remove(Integer.valueOf(mPlayer.getColour()));
                }
            }
        });

        final ListView ffList = (ListView) view.findViewById(R.id.lst_firefighter);
        Firefighter[] firefighterArray;
        if (mFirefighters.size() > 1) {
            firefighterArray = mFirefighters.toArray();
        } else {
            firefighterArray = new Firefighter[1];
            firefighterArray[0] = mFirefighters.getLast();
        }
        ArrayAdapter<Firefighter> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, firefighterArray);

        if (ffList != null) {
            ffList.setAdapter(adapter);
            ffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ffList.setItemChecked(i, true);
                }
            });
            int position;
            if (mPlayer != null) {
                position = adapter.getPosition(mPlayer.getFirefighter());
            } else {
                position = 0;
            }
            ffList.setItemChecked(position, true);
        }

        RadioGroup colors = (RadioGroup) view.findViewById(R.id.rg_colors);
        if (colors != null && mColourList != null) {
            for (int color : mColourList) {
                AppCompatRadioButton button = new AppCompatRadioButton(getActivity());
                if (color == Color.WHITE) {
                    color = Color.BLACK;
                }
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
            int checkPosition;
            if (mPlayer == null) {
                checkPosition = new Random().nextInt(mColourList.size());
            } else {
                checkPosition = mColourList.indexOf(mPlayer.getColour());
            }
            AppCompatRadioButton checkView = (AppCompatRadioButton) colors.getChildAt(checkPosition);
            colors.check(checkView.getId());
        }
        builder.setView(view);
        return builder.create();
    }

    private void addPlayer(Dialog dialog) {
        EditText nameBox = (EditText) dialog.findViewById(R.id.et_player_name);
        String name = nameBox.getText().toString();

        ListView ffList = (ListView) dialog.findViewById(R.id.lst_firefighter);
        int checkedPosition = ffList.getCheckedItemPosition();
        Firefighter firefighter = (Firefighter) ffList.getItemAtPosition(checkedPosition);
        if (firefighter instanceof FirefighterRandom) {
            checkedPosition = 1 + new Random().nextInt(ffList.getAdapter().getCount() - 1);
            firefighter = (Firefighter) ffList.getItemAtPosition(checkedPosition);
        }

        RadioGroup colors = (RadioGroup) dialog.findViewById(R.id.rg_colors);
        AppCompatRadioButton checkedButton = (AppCompatRadioButton)
                dialog.findViewById(colors.getCheckedRadioButtonId());
        ColorStateList colorStateList = checkedButton.getSupportButtonTintList();
        int color;
        if (colorStateList != null) {
            color = checkedButton.getSupportButtonTintList().getDefaultColor();
        } else {
            color = mColourList.get(0);
        }
        if (color == Color.BLACK) {
            color = Color.WHITE;
        }

        if (mPlayer == null) {
            mCallback.addPlayer(name, firefighter, color);
        } else {
            mCallback.editPlayer(name, firefighter, color, mPlayer);
        }
    }
}
