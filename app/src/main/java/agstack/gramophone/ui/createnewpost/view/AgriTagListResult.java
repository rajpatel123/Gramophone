package agstack.gramophone.ui.createnewpost.view;


import java.util.ArrayList;
import java.util.List;



public class AgriTagListResult {
    private static AgriTagListResult instance;

    public static AgriTagListResult getInstance() {
        if (instance == null)
            instance = new AgriTagListResult();
        return instance;
    }

    private AgriTagListResult() {
    }

    public void setSelectedTagCount(int selectedTagCount) {
        this.selectedTagCount = selectedTagCount;
    }

    private int selectedTagCount=0;

    public List<AgriTag> getSelectedTagList() {
        return selectedTagList;
    }

    public void setSelectedTagList(List<AgriTag> selectedTagList) {
        this.selectedTagList = selectedTagList;
    }

    private List<AgriTag> selectedTagList=new ArrayList<>();

    public void setListAgriTag(List<AgriTag> listAgriTag) {
        this.listAgriTag = listAgriTag;
    }

    private List<AgriTag> listAgriTag=new ArrayList<>();

    public void setListTagInPost(List<AgriTag> listTagInPost) {
        this.listTagInPost = listTagInPost;
    }

    private List<AgriTag> listTagInPost=null;


    public void setTagSaved(boolean tagSaved) {
        isTagSaved = tagSaved;
    }

    private boolean isTagSaved=false;


}
