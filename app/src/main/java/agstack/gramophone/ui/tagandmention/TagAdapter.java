package agstack.gramophone.ui.tagandmention;

import android.app.Activity;
import android.content.Context;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;


import agstack.gramophone.R;

public class TagAdapter extends FilteredArrayAdapter<Tag> {

    @LayoutRes
    private int layoutId;

    public TagAdapter(Context context, @LayoutRes int layoutId, Tag[] tags) {
        super(context, layoutId, tags);
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = l.inflate(layoutId, parent, false);
        }

        Tag t = getItem(position);
        TextView handleTextView= ((TextView)convertView.findViewById(R.id.handle));
        View lineView= ((View)convertView.findViewById(R.id.view));
        if(t.getPrefix()=='@'&& t.getHandle()!=null)
        {
            handleTextView.setText(t.getHandle());
            handleTextView.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

        }else{

            handleTextView.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }
        ((TextView)convertView.findViewById(R.id.value)).setText(Html.fromHtml(t.getFormattedValue()));

        return convertView;
    }

    @Override
    protected boolean keepObject(Tag tag, String mask) {
        mask = mask.toLowerCase();
        boolean a=tag.getPrefix() == mask.charAt(0) && tag.getTag().toLowerCase().contains(mask.substring(1, mask.length()));
        return a;
    }
}
