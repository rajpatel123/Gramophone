package agstack.gramophone.ui.createnewpost.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlPreviewModel implements Serializable {


    private String url, title, description,image;

    public UrlPreviewModel(@JsonProperty("url")String url,@JsonProperty("title") String title, @JsonProperty("description")String description, @JsonProperty("image")String image) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }



}
