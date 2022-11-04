package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayListItemModels implements Serializable {


    private ContentDetailsModel contentDetailsModels;
    private SnippetModel snippet;

    public PlayListItemModels(@JsonProperty("contentDetails") ContentDetailsModel contentDetailsModels,
                              @JsonProperty("snippet") SnippetModel snippet ) {
        this.contentDetailsModels = contentDetailsModels;
        this.snippet = snippet;
    }


    public ContentDetailsModel getContentDetailsModels() {
        return contentDetailsModels;
    }

    public SnippetModel getSnippet() {
        return snippet;
    }
}