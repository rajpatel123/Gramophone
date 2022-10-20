package agstack.gramophone.ui.createnewpost.model;


import agstack.gramophone.utils.Constants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class PostDetailsModel implements Serializable {

    private String _id;
    private String description;
    private String title;
    private String sharedUrlLink;
    private String urlPreviewDescription;
    private String imagePath;
    private Boolean liked=false;
    private List<AgriTag> tags;
    private PollData pollData;
    private BannerModelDetail banner;
    private UrlPreviewModel urlPreviewMeta;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    private String postType;
    private long createdDate;
    private int watchersCount, whatsAppShare, facebookShare, otherShare, complainCount, likesCount, commentsCount;
    private AuthorModelClass author;
    private AuthorModelClass lastLike;
    private ItemType itemType;

    public PostDetailsModel() {
        itemType = ItemType.ITEM;
    }
    public PostDetailsModel(boolean isLoading) {
        if(isLoading) {
            postType = Constants.PostType.Loader;
        }
    }

    public PostDetailsModel(ItemType itemType) {
        this.itemType = itemType;
        setId(itemType.toString());
    }

    public ItemType getItemType() {
        return itemType;
    }

    public PostDetailsModel(@JsonProperty("_id") String _id,
                            @JsonProperty("description") String description,
                            @JsonProperty("title") String title,
                         //   @JsonProperty("authorId") String authorId,

                            @JsonProperty("watchersCount") int watchersCount,
                            @JsonProperty("whatsAppShare") int whatsAppShare,
                            @JsonProperty("facebookShare") int facebookShare,
                            @JsonProperty("otherShare") int otherShare,
                            @JsonProperty("complainCount") int complainCount,
                            @JsonProperty("likesCount") int likesCount,
                            @JsonProperty("commentsCount") int commentsCount,
                            @JsonProperty("sharedUrlLink") String sharedUrlLink,
                            @JsonProperty("urlPreviewDescription") String urlPreviewDescription,
                            @JsonProperty("imagePath") String imagePath,
                            @JsonProperty("author") AuthorModelClass author,
                            @JsonProperty("lastLike") AuthorModelClass lastLike,
                            @JsonProperty("createdDate") long createdDate,
                            @JsonProperty("postType") String postType,
                            @JsonProperty("liked") Boolean liked,
                            @JsonProperty("quiz_data") PollData pollData,
                            @JsonProperty("banner") BannerModelDetail banner,
                            @JsonProperty("urlPreviewMeta") UrlPreviewModel urlPreviewMeta,
                            @JsonProperty("tags") List<AgriTag> tags) {

                this._id = _id;
                this.description = description;
                this.title = title;
              //  this.authorId = authorId;

                this.watchersCount = watchersCount;
                this.whatsAppShare = whatsAppShare;
                this.facebookShare = facebookShare;
                this.otherShare = otherShare;
                this.complainCount = complainCount;
                this.likesCount = likesCount;
                this.commentsCount = commentsCount;
                this.sharedUrlLink = sharedUrlLink;
                this.urlPreviewDescription = urlPreviewDescription;
                this.imagePath = imagePath;
                this.author = author;
                this.lastLike = lastLike;
                this.liked = liked;
                this.createdDate = createdDate;
                this.postType = postType;
                this.tags=tags;
                this.urlPreviewMeta=urlPreviewMeta;
                this.pollData=pollData;
                this.banner=banner;



    }

    public UrlPreviewModel getUrlPreviewMeta() {
        return urlPreviewMeta;
    }

    public List<AgriTag> getTags() {
        return tags;
    }

    public AuthorModelClass getLastLike() {
        return lastLike;
    }

    public void setLastLike(AuthorModelClass lastLike) {
        this.lastLike = lastLike;
    }

    public Boolean getLiked() {
        return liked;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
/*
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }*/



    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getWhatsAppShare() {
        return whatsAppShare;
    }

    public void setWhatsAppShare(int whatsAppShare) {
        this.whatsAppShare = whatsAppShare;
    }

    public int getFacebookShare() {
        return facebookShare;
    }

    public void setFacebookShare(int facebookShare) {
        this.facebookShare = facebookShare;
    }

    public int getOtherShare() {
        return otherShare;
    }

    public void setOtherShare(int otherShare) {
        this.otherShare = otherShare;
    }

    public int getComplainCount() {
        return complainCount;
    }

    public void setComplainCount(int complainCount) {
        this.complainCount = complainCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }


    public String getSharedUrlLink() {
        return sharedUrlLink;
    }

    public void setSharedUrlLink(String sharedUrlLink) {
        this.sharedUrlLink = sharedUrlLink;
    }

    public String getUrlPreviewDescription() {
        return urlPreviewDescription;
    }

    public void setUrlPreviewDescription(String urlPreviewDescription) {
        this.urlPreviewDescription = urlPreviewDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public AuthorModelClass getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModelClass author) {
        this.author = author;
    }


    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }


    public String get_id() {
        return _id;
    }



    public void setTags(List<AgriTag> tags) {
        this.tags = tags;
    }


    public PollData getPollData() {
        return pollData;
    }

    public void setPollData(PollData pollData) {
        this.pollData = pollData;
    }


    public BannerModelDetail getBanner() {
        return banner;
    }

    public void setBanner(BannerModelDetail banner) {
        this.banner = banner;
    }
}
