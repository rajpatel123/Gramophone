package agstack.gramophone.ui.tv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import agstack.gramophone.R;
import agstack.gramophone.ui.tv.model.YoutubeChannelItem;
import agstack.gramophone.utils.Constants;


public class PlayListViewHolder extends RecyclerView.ViewHolder {

    private TextView playListName, playListId, videoCountTextView;
    LinearLayout playListImg;
    YouTubeThumbnailView youTubeThumbnailView;
    private Boolean isThumbnailLoaded = false;
    private Context context;

    public PlayListViewHolder(@NonNull View itemView, final PlayListAdapter.Callback callback) {
        super(itemView);
        initViews(itemView);
        this.context = itemView.getContext();

        playListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPlayListClick(v, getAdapterPosition());
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onListItemClick(v, getAdapterPosition());
                }
            }
        });

    }

    private void initViews(View itemView) {
        playListImg = itemView.findViewById(R.id.playListImg);
        playListName =  itemView.findViewById(R.id.playListNameView);
        videoCountTextView =  itemView.findViewById(R.id.videoCountTextView);
        youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

    }

    public void bindData(final YoutubeChannelItem youtubeChannelItem) {

        playListName.setText(youtubeChannelItem.getSnippet().getTitle());

        videoCountTextView.setText(String.format(context.getString(R.string.video_count), youtubeChannelItem.getContentDetails().getItemCount()));

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);

                //  relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };
        if (!isThumbnailLoaded) {
            isThumbnailLoaded = true;
            youTubeThumbnailView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setPlaylist(youtubeChannelItem.getId());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    isThumbnailLoaded = false;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                    isThumbnailLoaded = false;
                }
            });

        }
    }
}