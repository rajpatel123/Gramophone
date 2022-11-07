package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class YoutubeModel implements Serializable {

    private String nextPageToken;

    private List<YoutubeChannelItem> items;

    public YoutubeModel(@JsonProperty("items") List<YoutubeChannelItem> items,
                        @JsonProperty("nextPageToken") String nextPageToken) {
        this.items = items;
        this.nextPageToken = nextPageToken;
    }

    public List<YoutubeChannelItem> getItems() {
        return items;
    }

    public void setItems(List<YoutubeChannelItem> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}