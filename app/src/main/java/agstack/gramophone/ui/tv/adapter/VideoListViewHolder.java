package agstack.gramophone.ui.tv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import agstack.gramophone.R;
import agstack.gramophone.ui.tv.model.PlayListItemModels;
import agstack.gramophone.utils.Constants;


public class VideoListViewHolder extends RecyclerView.ViewHolder {

    private TextView videoName, playListId, videoDescription;
    private LinearLayout listItemContainer;
    YouTubeThumbnailView youTubeThumbnailView;
    RelativeLayout relativeLayoutOverYouTubeThumbnailView;
    private Context context;
    private Boolean isThumbnailLoaded=false;
    public VideoListViewHolder(@NonNull View itemView, final VideoListAdapter.Callback callback) {
        super(itemView);
        initViews(itemView);
        this.context=itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onListItemClick(v, getAdapterPosition());
                }
            }
        });

    }

    private void initViews(View itemView) {
        videoName = (TextView)itemView.findViewById(R.id.videoTitleTextView);
        videoDescription = (TextView)itemView.findViewById(R.id.videoDescriptionTextView);
        listItemContainer = (LinearLayout) itemView.findViewById(R.id.listItemTitleContainer);
        youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

    }

    public void bindData(final PlayListItemModels youtubeChannelItem, int selectedPosition) {

        videoName.setText(youtubeChannelItem.getSnippet().getTitle());
        videoDescription.setText(youtubeChannelItem.getSnippet().getDescription());
        if(selectedPosition==getAdapterPosition())
        {

            listItemContainer.setBackgroundColor(context.getResources().getColor(R.color.gray));
            videoName.setTextColor(context.getResources().getColor(R.color.gray_stroke));
        }else{
            listItemContainer.setBackgroundColor(context.getResources().getColor(R.color.blakish));

        }


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
              //  relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };
        if(!isThumbnailLoaded) {
            isThumbnailLoaded=true;
            youTubeThumbnailView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(youtubeChannelItem.getContentDetails().getVideoId());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
               isThumbnailLoaded=false;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                    isThumbnailLoaded=false;
                }
            });
//
        }


    }
}