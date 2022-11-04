package agstack.gramophone.ui.tv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeThumbnailModel implements Serializable {

    private YoutubeDefaultThumbnail defaultThumbnail;


    public YoutubeThumbnailModel(@JsonProperty("default") YoutubeDefaultThumbnail defaultThumbnail) {
    this.defaultThumbnail=defaultThumbnail;
    }

    public YoutubeDefaultThumbnail getDefaultThumbnail() {
        return defaultThumbnail;
    }
}
