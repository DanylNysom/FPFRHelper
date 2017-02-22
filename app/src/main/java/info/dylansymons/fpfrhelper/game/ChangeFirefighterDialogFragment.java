package info.dylansymons.fpfrhelper.game;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Random;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.firefighter.FirefighterRandom;
import info.dylansymons.fpfrhelper.player.Player;

public class ChangeFirefighterDialogFragment extends DialogFragment {
    private static final String INSTANCE_PLAYER = "player";
    private static final String INSTANCE_FIREFIGHTER_LIST = "firefighters";
    private static final String INSTANCE_CALLBACK = "callback";

    private Player mPlayer;
    private Callback mCallback;

    static ChangeFirefighterDialogFragment newInstance(FirefighterList firefighterList,
                                                       Player player,
                                                       Callback callback) {
        ChangeFirefighterDialogFragment frag = new ChangeFirefighterDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(INSTANCE_FIREFIGHTER_LIST, firefighterList);
        args.putSerializable(INSTANCE_PLAYER, player);
        frag.mCallback = callback;
        frag.setArguments(args);
        frag.setRetainInstance(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final DialogFragment fragment = this;
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        FirefighterList mFirefighters = (FirefighterList)
                savedInstanceState.getSerializable(INSTANCE_FIREFIGHTER_LIST);
        mPlayer = (Player) savedInstanceState.getSerializable(INSTANCE_PLAYER);

        View view = inflater.inflate(R.layout.dialog_change_firefighter, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lv_firefighters);
        final Firefighter[] firefighterArray;
        if (mFirefighters.size() > 1) {
            firefighterArray = mFirefighters.toArray();
        } else {
            firefighterArray = new Firefighter[1];
            firefighterArray[0] = mFirefighters.getLast();
        }
        final ArrayAdapter<Firefighter> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, firefighterArray);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position < 0) {
                    fragment.dismiss();
                }
                Firefighter newFirefighter = firefighterArray[position];
                if (newFirefighter instanceof FirefighterRandom) {
                    position = new Random().nextInt(firefighterArray.length - 1) + 1;
                    newFirefighter = firefighterArray[position];
                }
                mCallback.changeFirefighter(mPlayer, newFirefighter);
                fragment.dismiss();
            }
        });
        return view;
    }

    public interface Callback {
        void changeFirefighter(Player player, Firefighter newFirefighter);
    }
}
