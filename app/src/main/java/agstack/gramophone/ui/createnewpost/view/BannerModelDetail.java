package agstack.gramophone.ui.createnewpost.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BannerModelDetail implements Serializable {



    private int banner_id;
    private String banner_image;
    private String language;
    private int banner_featured_flag;
    private int banner_sequence;
    private String banner_type;
    private String banner_link;
    private String banner_description;

    public BannerModelDetail(@JsonProperty("banner_id") int banner_id,
                             @JsonProperty("banner_image") String banner_image,
                             @JsonProperty("language") String language,
                             @JsonProperty("banner_featured_flag") int banner_featured_flag,
                             @JsonProperty("banner_sequence") int banner_sequence,
                             @JsonProperty("banner_type") String banner_type,
                             @JsonProperty("banner_link") String banner_link,
                             @JsonProperty("banner_description") String banner_description) {
            this.banner_id = banner_id;
            this.banner_image = banner_image;
            this.language = language;
            this.banner_description = banner_description;
            this.banner_featured_flag = banner_featured_flag;
            this.banner_sequence = banner_sequence;
            this.banner_type = banner_type;
            this.banner_link = banner_link;

    }

    public int getBanner_id() {
        return banner_id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public String getLanguage() {
        return language;
    }

    public int getBanner_featured_flag() {
        return banner_featured_flag;
    }

    public int getBanner_sequence() {
        return banner_sequence;
    }

    public String getBanner_type() {
        return banner_type;
    }

    public String getBanner_link() {
        return banner_link;
    }

    public String getBanner_description() {
        return banner_description;
    }

    public void setBanner_link(String banner_link) {
        this.banner_link = banner_link;
    }
}
