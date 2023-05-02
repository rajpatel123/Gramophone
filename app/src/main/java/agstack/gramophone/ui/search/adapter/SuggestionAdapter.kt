package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.ItemGlobalSearchSuggestionBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SuggestionAdapter(
    val list: List<String>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): SuggestionAdapter.SuggestionViewHolder {
        return SuggestionViewHolder(
            ItemGlobalSearchSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuggestionAdapter.SuggestionViewHolder, position: Int) {
        try {
            if (position < list.size) {
                holder.binding.item = list[position]
                holder.binding.txtSuggestionWrapper.setOnClickListener {
                    listener.invoke(list[position])
                }
            }
        } catch (ex: Exception) {
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SuggestionViewHolder(var binding: ItemGlobalSearchSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root)
}