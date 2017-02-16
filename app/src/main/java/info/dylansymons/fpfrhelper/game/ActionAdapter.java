package info.dylansymons.fpfrhelper.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;
import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.player.Player;

/**
 * An Adapter for managing a set of Actions and displaying them in square views ideal for a
 * GridView.
 */
class ActionAdapter extends BaseAdapter {
    private Player mPlayer;
    private Context mContext;

    /**
     * Creates a new ActionAdapter
     *
     * @param context the Context to be used to initialize Views
     * @param player  the player to retreive Actions from
     */
    ActionAdapter(Context context, Player player) {
        mContext = context;
        mPlayer = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return mPlayer.getActions().length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        return mPlayer.getActions()[position];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout view;
        if (convertView == null) {
            view = (RelativeLayout) inflater.inflate(R.layout.griditem_action, parent, false);
        } else {
            view = (RelativeLayout) convertView;
        }
        Firefighter.Action action = (Firefighter.Action) getItem(position);

        String name = action.getShortDescription();

        int cost = action.getCost();
        String costString;
        if (!mPlayer.getFirefighter().hasBonusApFor(action)) {
            costString = String.valueOf(cost);
        } else {
            costString = mContext.getResources().getString(R.string.special_cost, cost);
        }

        TextView nameView = (TextView) view.findViewById(R.id.tv_name);
        TextView costView = (TextView) view.findViewById(R.id.tv_cost);
        nameView.setText(name);
        costView.setText(costString);

        if (mPlayer.hasPointsFor(action)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
