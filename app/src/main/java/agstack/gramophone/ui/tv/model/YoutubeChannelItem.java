package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class YoutubeChannelItem implements Serializable {

    private String id;

    private SnippetModel snippet;
    private ContentDetailsModel contentDetailsModel;

    public YoutubeChannelItem(@JsonProperty("id") String id,
                              @JsonProperty("snippet") SnippetModel snippet,
                              @JsonProperty("contentDetails") ContentDetailsModel contentDetailsModel) {
        this.id = id;
        this.snippet = snippet;
        this.contentDetailsModel=contentDetailsModel;
    }

    public ContentDetailsModel getContentDetailsModel() {
        return contentDetailsModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SnippetModel getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetModel snippet) {
        this.snippet = snippet;
    }

}