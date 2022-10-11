package agstack.gramophone.ui.createnewpost.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorModelClass implements Serializable {


    private String _id, phoneNo, photoUrl,username,uuid,badge;

    public AuthorModelClass(@JsonProperty("_id") String _id,@JsonProperty("uuid") String uuid, @JsonProperty("phoneNo") String phoneNo,
                            @JsonProperty("photoUrl") String photoUrl, @JsonProperty("username") String username, @JsonProperty("badge") String badge) {
                    this._id = _id;
                    this.uuid=uuid;
                    this.phoneNo = phoneNo;
                    this.photoUrl = photoUrl;
                    this.username = username;
                    this.badge=badge;

    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getUuid() {
        return uuid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
