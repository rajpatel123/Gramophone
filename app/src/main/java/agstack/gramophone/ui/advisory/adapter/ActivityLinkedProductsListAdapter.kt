package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemLinkedProductBinding
import agstack.gramophone.ui.advisory.models.advisory.LinkedTechnical
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull


class ActivityLinkedProductsListAdapter(
    private val dataList: List<LinkedTechnical>,
    var onAddToCartClick: ((productId: Int) -> Unit),
    var onDetailClick: ((productId: Int) -> Unit)
) :
    RecyclerView.Adapter<ActivityLinkedProductsListAdapter.DeveloperViewHolder>() {
    private var isHeaderVisible: Boolean = false

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemLinkedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        val linkedTechnical: LinkedTechnical = dataList[position]
        holder.binding.setVariable(BR.model, linkedTechnical)

        if (position == 0) {
            if (!isProductWithIdAvailable()){
                val param = holder.binding.outPurchaseText.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(55,0,55,39)
                holder.binding.outPurchaseText.layoutParams = param
            }

        }

        if (linkedTechnical.product_id < 1 && !isHeaderVisible) {
            holder.binding.outPurchaseText.visibility = VISIBLE
            isHeaderVisible = true
        } else {
            holder.binding.outPurchaseText.visibility = GONE
        }

        if (dataList.size == 1 && linkedTechnical.product_id == 0) {
            holder.binding.outPurchaseText.visibility = VISIBLE
        }

        if (linkedTechnical.product_id == 0) {
            holder.binding.productDetails.visibility = GONE
            holder.binding.tvProductTitle.visibility = VISIBLE
        }

        holder.binding.tvAddToCarts.setOnClickListener {
            if (onAddToCartClick.isNotNull() && linkedTechnical.product_id.isNotNull())
                onAddToCartClick.invoke(linkedTechnical.product_id)
        }

        holder.binding.root.setOnClickListener {
            if (onDetailClick.isNotNull() && linkedTechnical.product_id.isNotNull())
                onDetailClick.invoke(linkedTechnical.product_id)
        }


    }

    private fun isProductWithIdAvailable(): Boolean {
        var isExist = false
        run breaking@{
            dataList.forEach {
                if (it.product_id > 0) {
                    isExist = true
                    return@breaking
                } else {
                    isExist = false

                }
            }
        }
        return isExist
    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLinkedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}