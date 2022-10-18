package agstack.gramophone.ui.createnewpost.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import agstack.gramophone.R;
import agstack.gramophone.ui.createnewpost.model.AgriTag;

/**
 * Created by Ashish on 13-06-2018.
 */

public class FinalAgriTagViewHolder extends RecyclerView.ViewHolder{

    protected FinalAgriTagAdapter.Callback callback;
    private TextView textView;

    public FinalAgriTagViewHolder(View itemView, final FinalAgriTagAdapter.Callback callback) {
        super(itemView);
        this.callback = callback;
        Context context = itemView.getContext();
        textView= itemView.findViewById(R.id.finalTagText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callback.onRemoveTagFromList(v,getAdapterPosition());
            }
        });
    }
    public void bindData(AgriTag agriTag)
    {

        if(agriTag.getTag()!=null) {

            textView.setText("# " + agriTag.getTag());
        }

    }


}
