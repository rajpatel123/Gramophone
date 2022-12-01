package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception


class RecommendedLinkedProductsListAdapter(private val dataList: List<GpApiResponseData>) :
    RecyclerView.Adapter<RecommendedLinkedProductsListAdapter.DeveloperViewHolder>() {
    var onAddToCartClick: ((productId: Int) -> Unit)? = null
    var onProductDetailClick: ((productId: Int) -> Unit)? = null
    var onProductDetailClicked: ((productId: Int) -> Unit)? = null

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemRecommendedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        val productSku: GpApiResponseData = dataList[position]
        holder.binding.setVariable(BR.model, productSku)

        holder.binding.tvAddToCarts.setOnClickListener {
            try {
                onAddToCartClick?.invoke(dataList[position].product_sku_list[0].id.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        holder.binding.ivProducts.setOnClickListener {
            try {
                onProductDetailClick?.invoke(dataList[position].product_sku_list[0].id.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        holder.binding.tvProductNames.setOnClickListener {
            try {
                onProductDetailClick?.invoke(dataList[position].product_sku_list[0].id.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.binding.rlProoduct.setOnClickListener {
            try {
                onProductDetailClicked?.invoke(dataList[position].product_sku_list[0].id.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemRecommendedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}