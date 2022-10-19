package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemArticlesBinding
import agstack.gramophone.ui.home.view.fragments.market.model.FormattedArticlesData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ArticlesAdapter(
    private val articlesList: ArrayList<FormattedArticlesData>,
    private val listener: (String) -> Unit,
) :
    RecyclerView.Adapter<ArticlesAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemArticlesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = articlesList[i]
        holder.binding.llArticlesItem.setOnClickListener {
            listener.invoke(articlesList[i].id.toString())
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    inner class CustomViewHolder(var binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root)
}