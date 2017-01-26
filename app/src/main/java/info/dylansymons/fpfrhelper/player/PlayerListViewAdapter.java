package info.dylansymons.fpfrhelper.player;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.dylansymons.fpfrhelper.R;

/**
 * Created by dylan on 1/2/17.
 */

public class PlayerListViewAdapter extends RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder> {
    private PlayerList mPlayerList;

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

    public boolean move(int source, int destination) {
        Player player = mPlayerList.remove(source);
        mPlayerList.add(destination, player);
        notifyItemMoved(source, destination);
        return true;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }

    public String remove(int position) {
        String firefighter = mPlayerList.get(position).getFirefighter();
        mPlayerList.remove(position);
        notifyItemRemoved(position);
        return firefighter;
    }
}
