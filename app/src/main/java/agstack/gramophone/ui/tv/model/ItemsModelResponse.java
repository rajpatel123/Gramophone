package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown =true)
public class ItemsModelResponse
{

    private String nextPageToken;

    public List<PlayListItemModels> getPlayListItemModelsList() {
        return playListItemModelsList;
    }

    private List<PlayListItemModels> playListItemModelsList;

    public ItemsModelResponse(@JsonProperty("items") List<PlayListItemModels> playListItemModels,
                              @JsonProperty("nextPageToken") String nextPageToken) {
        this.playListItemModelsList = playListItemModels;
        this.nextPageToken = nextPageToken;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}