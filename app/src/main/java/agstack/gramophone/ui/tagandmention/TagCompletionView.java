package agstack.gramophone.ui.tagandmention;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tokenautocomplete.TokenCompleteTextView;

import agstack.gramophone.R;
import agstack.gramophone.utils.Constants;

public class TagCompletionView extends TokenCompleteTextView<Tag> {

    public TagCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Tag object) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = null;
        if (object.getPrefix()=='@'){
            token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
            token.setText(Html.fromHtml(object.getFormattedValue()));
        }else if(object.getPrefix()=='#'){
            token = (TokenTextView) l.inflate(R.layout.tag_token, (ViewGroup) getParent(), false);
            token.setText(Html.fromHtml(object.getFormattedValue()));
        }else{
            token = (TokenTextView) l.inflate(R.layout.problem_token, (ViewGroup) getParent(), false);
            token.setText(Html.fromHtml(object.getFormattedValue()));
        }
        return token;


    }

    @Override
    protected Tag defaultObject(String completionText) {
        if (completionText.length() == 1) {
            return null;
        } else {
            if (completionText.charAt(0) == '@') {
                String value = completionText.substring(1, completionText.length());
                return new Tag(null, completionText.charAt(0), value, null, null, null);

            } else if(completionText.charAt(0) == '$'){
                String value = completionText.substring(1, completionText.length());
                return new Tag(null, completionText.charAt(0), value, null, null, null);

            }else {
                String value = completionText.substring(1, completionText.length());
                return new Tag(null, completionText.charAt(0), value, Constants.SearchUrl + Constants.SearchUrlPARAMETER + value, null, null);
            }
        }
    }

}
