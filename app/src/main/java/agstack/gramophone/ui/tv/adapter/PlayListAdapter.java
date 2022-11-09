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
import agstack.gramophone.ui.tv.model.YoutubeChannelItem;
import agstack.gramophone.utils.Constants;

public class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String TAG = PlayListAdapter.class.getSimpleName();

    private Context context;
    private List<YoutubeChannelItem> data = new ArrayList<>();
    private List<String> watchedVideosId = new ArrayList<>();
    private Callback callback;
    private boolean isLoadingAdded = false;


    public PlayListAdapter(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
       // this.data = data;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*View view = inflater.inflate(R.layout.list_item_video_playlist,parent,false);
        return new PlayListViewHolder(view,callback);*/


        View view = null;

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case Constants.ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_video_playlist, parent, false);
                return new PlayListViewHolder(view, callback);



            case Constants.LOADING:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_bottom_progress, parent, false);
                return new LoadingVH(view);

        }
            return new PlayListViewHolder(view, callback);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
      //  ((PlayListViewHolder)viewHolder).bindData(getItemByPosition(i), data.size());



        switch (getItemViewType(position)) {
            case Constants.ITEM:
                ((PlayListViewHolder)viewHolder).bindData(getItemByPosition(position));
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
        int position = data.size()-1;
        YoutubeChannelItem result = getItem(position);

        if (result != null) {
        //   data.remove(position);
            notifyItemRemoved(position +1);
        }
    }

    public YoutubeChannelItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size() && isLoadingAdded) ? Constants.LOADING:Constants.ITEM;
    }

    public void addAll(List<YoutubeChannelItem> list) {
        int position = this.data.size();
        this.data.addAll(list);
        notifyItemInserted(position);
    }


    public void setList(List<YoutubeChannelItem> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       if(isLoadingAdded)
        {
            return data.size()+1;
        }else {
          return data.size();
       }
    }

    public YoutubeChannelItem getItemByPosition(int position) {
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
        String  url = String.valueOf(Uri.parse("http://www.youtube.com/watch?v=" + id));
        share.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(share, "Share link!"));
    }

    public interface Callback {
        void onListItemClick(View view, int position);
    }
}