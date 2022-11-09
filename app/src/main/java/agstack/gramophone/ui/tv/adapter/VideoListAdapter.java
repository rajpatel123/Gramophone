package agstack.gramophone.ui.tv.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import agstack.gramophone.R;
import agstack.gramophone.ui.tv.model.PlayListItemModels;
import agstack.gramophone.utils.Constants;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String TAG = VideoListAdapter.class.getSimpleName();

    private Context context;
    private List<PlayListItemModels> data = new ArrayList<>();
    int selectedPosition = 0;
    private Callback callback;
    private boolean isLoadingAdded = false;
    private String currentPlayingPlayListName;


    public VideoListAdapter(Context context, List<PlayListItemModels> data, String currentPlayingPlayListName) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.data = data;
        this.currentPlayingPlayListName = currentPlayingPlayListName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case Constants.ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_all_videos_new, parent, false);
                return new VideoListViewHolder(view, callback);


            case Constants.LOADING:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_bottom_progress, parent, false);
                return new LoadingVH(view);

        }
        return new VideoListViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case Constants.ITEM:
                ((VideoListViewHolder) viewHolder).bindData(getItemByPosition(position), selectedPosition, currentPlayingPlayListName);
                break;

            case Constants.LOADING:
                break;
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        // add(new PostDetailsModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        PlayListItemModels result = getItem(position);

        if (result != null) {
            notifyItemRemoved(position + 1);
        }
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public PlayListItemModels getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size() && isLoadingAdded) ? Constants.LOADING : Constants.ITEM;
    }

    public void addAll(List<PlayListItemModels> list) {
        int position = this.data.size();
        this.data.addAll(list);
        notifyItemInserted(position);
    }


    public void setList(List<PlayListItemModels> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isLoadingAdded) {
            return data.size() + 1;
        } else {
            return data.size();
        }
    }

    public PlayListItemModels getItemByPosition(int position) {
        return data.get(position);
    }

    @Override
    public void onClick(View v) {

    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    private void shareTextUrl(String id) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        String url = String.valueOf(Uri.parse("http://www.youtube.com/watch?v=" + id));
        share.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(share, "Share link!"));
    }

    public interface Callback {
        void onListItemClick(View view, int position);
    }
}