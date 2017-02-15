package info.dylansymons.fpfrhelper.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;

/**
 * @author dylan
 */
public class PlayerListViewAdapter extends RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder> {
    private final PlayerList mPlayerList;

    public PlayerListViewAdapter(PlayerList playerList) {
        mPlayerList = playerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_player, parent, false);
                new TextView(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mPlayerList.get(position).toString());
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;

        ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }

    public Player remove(int position) {
        Player player = mPlayerList.get(position);
        mPlayerList.remove(position);
        notifyItemRemoved(position);
        return player;
    }
}
