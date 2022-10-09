package agstack.gramophone.ui.tagandmention;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import agstack.gramophone.utils.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag implements Parcelable {
    private Character prefix;
    private String tag;
    private String url;
    private String _id;
    private String uuid;
    private String handle;


    public Tag(@JsonProperty("_id") String _id, @JsonProperty("prefix") Character prefix, @JsonProperty("tag") @NonNull String tag, @JsonProperty("url") String url, @JsonProperty("uuid") String uuid, @JsonProperty("handle") String handle) {
        this.url = url;
        this.prefix=prefix;
        if(uuid!=null) {
            if(prefix==null) {
                this.prefix = '@';
            }
            if(url==null) {
                    this.url = Constants.SearchUrl + Constants.ProfileUrlPARAMETER + uuid;
            }
        }else {
            if(prefix==null) {
                this.prefix = '#';
            }
            if(tag!=null&&url==null&&prefix!=null&&prefix == '#') {
                this. url=Constants.SearchUrl +Constants.SearchUrlPARAMETER+ tag;
            }
        }
        this.tag = tag;
        this.uuid=uuid;
        this._id=_id;
        this.handle=handle;
    }

    public static Tag[] sampleTags() {
        return new Tag[]{
                //   new Tag('@', "TokenAutoComplete", "app://www.gramophone.in"),
        };
    }

    public char getPrefix() {
        return prefix;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @NonNull
    public String getFormattedValue() {
        if(url==null&&prefix=='#')
        {
            url=Constants.SearchUrl +Constants.SearchUrlPARAMETER+ tag;
        } else if(url==null&&prefix=='@'&&uuid!=null)
        {
            url=Constants.SearchUrl +Constants.ProfileUrlPARAMETER+ uuid;
        }
        if(url!=null) {
            String lastword = "<a href=\"" + url + "\">" + String.format(Locale.US, "%c%s", prefix, tag) + "</a>";
            return lastword;
        }else{
            return String.format(Locale.US, "%c%s", prefix, tag);
        }
    }
    @NonNull
    public String getFormattedValue1() {
//
//        String lastword = "<a href=\"" + Constants.SearchUrl +Constants.SearchUrlPARAMETER+ tag + "\">" + String.format(Locale.US, "%c%s", prefix, tag) + "</a>";
//        // return String.format(Locale.US, "%c%s", prefix, tag);
//        return lastword;
////        }else{
        return String.format(Locale.US, "%c%s", prefix, tag);
//
//        }
    }

    @NotNull
    @Override
    public String toString() {
        return getFormattedValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeCharArray(new char[]{prefix});
        dest.writeString(this.tag);
    }

    private Tag(Parcel in) {
        char[] temp = new char[1];
        in.readCharArray(temp);
        this.prefix = temp[0];
        this.tag = in.readString();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
