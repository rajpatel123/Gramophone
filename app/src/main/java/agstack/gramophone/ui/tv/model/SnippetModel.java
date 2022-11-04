package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SnippetModel implements Serializable {

    private String title;
    private String description;
    private YoutubeThumbnailModel youtubeThumbnailModel;

    public SnippetModel(@JsonProperty("title") String title,
                        @JsonProperty("thumbnails") YoutubeThumbnailModel youtubeThumbnailModel,
                        @JsonProperty("description") String description) {
        this.title = title;
        this.description = description;
        this.youtubeThumbnailModel=youtubeThumbnailModel;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public YoutubeThumbnailModel getYoutubeThumbnailModel() {
        return youtubeThumbnailModel;
    }

    public void setYoutubeThumbnailModel(YoutubeThumbnailModel youtubeThumbnailModel) {
        this.youtubeThumbnailModel = youtubeThumbnailModel;
    }
}