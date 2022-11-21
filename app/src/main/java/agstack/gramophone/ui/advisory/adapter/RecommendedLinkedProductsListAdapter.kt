package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class RecommendedLinkedProductsListAdapter(private val dataList: List<GpApiResponseData>) :
    RecyclerView.Adapter<RecommendedLinkedProductsListAdapter.DeveloperViewHolder>() {
    var onAddToCartClicked: ((GpApiResponseData) -> Unit)? = null

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemRecommendedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var productSku: GpApiResponseData = dataList[position]!!
        holder.binding.setVariable(BR.model, productSku)

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemRecommendedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}