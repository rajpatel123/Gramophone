package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemArticlesBinding
import agstack.gramophone.databinding.ItemFeatureProductBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ArticlesAdapter :
    RecyclerView.Adapter<ArticlesAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemArticlesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/3
    }

    inner class CustomViewHolder(var binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root)
}