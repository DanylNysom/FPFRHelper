package info.dylansymons.fpfrhelper.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;

/**
 * @author dylan
 */
public class PlayerListViewAdapter extends RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder> {
    private final PlayerList mPlayerList;
    private final PlayerListViewAdapterCallback mCallback;

    public PlayerListViewAdapter(PlayerList playerList, PlayerListViewAdapterCallback callback) {
        mPlayerList = playerList;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_player, parent, false);
                new TextView(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Player player = mPlayerList.get(position);
        holder.mFirefighterView.setText(player.getFirefighterTitle());
        holder.mPlayerView.setText(player.getName());
        View view = holder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onPlayerSelected(player);
            }
        });
        view.setBackgroundColor(player.getColour());
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public void move(int source, int destination) {
        Player player = mPlayerList.remove(source);
        mPlayerList.add(destination, player);
        notifyItemMoved(source, destination);
    }

    public Player remove(int position) {
        Player player = mPlayerList.get(position);
        mPlayerList.remove(position);
        notifyItemRemoved(position);
        return player;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mFirefighterView;
        final TextView mPlayerView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mFirefighterView = (TextView) mView.findViewById(R.id.tv_firefighter_name);
            mPlayerView = (TextView) mView.findViewById(R.id.tv_player_name);
        }
    }
}
