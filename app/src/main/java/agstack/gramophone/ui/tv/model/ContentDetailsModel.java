package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDetailsModel implements Serializable {

    public String getVideoId() {
        return videoId;
    }

    private String videoId;
    private int itemCount;

    public ContentDetailsModel(@JsonProperty("videoId") String videoId,@JsonProperty("itemCount") int itemCount
    ) {
        this.videoId =videoId;
        this.itemCount=itemCount;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}