package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemArticlesBinding
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.FormattedArticlesData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ArticlesAdapter(private val featuredArticlesList: ArrayList<FormattedArticlesData>) :
    RecyclerView.Adapter<ArticlesAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemArticlesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = featuredArticlesList[i]
    }

    override fun getItemCount(): Int {
        return featuredArticlesList.size
    }

    inner class CustomViewHolder(var binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root)
}