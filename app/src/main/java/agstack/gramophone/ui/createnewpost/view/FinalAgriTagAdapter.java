/*
 *
 * Copyright 2017 Rozdoum
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package agstack.gramophone.ui.createnewpost.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agstack.gramophone.R;

import java.util.ArrayList;
import java.util.List;


public class FinalAgriTagAdapter extends RecyclerView.Adapter<FinalAgriTagViewHolder> {
    private List<AgriTag> list = new ArrayList<>();
    private Callback callback;
    private boolean isCallFromPostViewHolder=false;
    private int Comment_List_Size_For_MainPage=4;

    @Override
    public FinalAgriTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isCallFromPostViewHolder)
        {  final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_tag_list_item, parent, false);
            return new FinalAgriTagViewHolder(view, callback);}
        else {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.final_tag_list_item, parent, false);
            return new FinalAgriTagViewHolder(view, callback);
        }
    }

    @Override
    public void onBindViewHolder(FinalAgriTagViewHolder holder, int position) {

      //  holder.itemView.setLongClickable(true);
        holder.bindData(getItemByPosition(position));
    }

    public AgriTag getItemByPosition(int position) {
        return list.get(position);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setList(List<AgriTag> list) {

        this.list = list;

        notifyDataSetChanged();
    }
    public void setCallFromPostViewHolder(){
        this.isCallFromPostViewHolder=true;

    }

    @Override
    public int getItemCount() {

            return list.size();


    }




    public interface Callback {
        void onRemoveTagFromList(View v, int position);
    }
}
