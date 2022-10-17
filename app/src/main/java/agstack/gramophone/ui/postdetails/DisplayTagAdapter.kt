package agstack.gramophone.ui.postdetails

import agstack.gramophone.databinding.FinalTagListItemBinding
import agstack.gramophone.ui.postdetails.model.Tag
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DisplayTagAdapter(
    val tagList: List<Tag>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectCropItemViewHolder(
            FinalTagListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as SelectCropItemViewHolder
        h.binding.finalTagText.text= tagList?.get(position)?.tag
        h.binding.removeTag.visibility=GONE
    }


    override fun getItemViewType(position: Int): Int {
        return viewTypeItem
    }

    override fun getItemCount(): Int {
        return tagList?.size!!
    }

    inner class SelectCropItemViewHolder(var binding: FinalTagListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}