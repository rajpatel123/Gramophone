package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemActivityBinding
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.databinding.ItemLinkedIssuesBinding
import agstack.gramophone.databinding.ItemLinkedProductBinding
import agstack.gramophone.ui.advisory.models.advisory.Activity
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.advisory.models.advisory.LinkedIssue
import agstack.gramophone.ui.advisory.models.advisory.LinkedTechnical
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull


class ActivityLinkedProductsListAdapter(
    private val dataList: List<LinkedTechnical>,
    var onAddToCartClick: ((productId: String) -> Unit),
) :
    RecyclerView.Adapter<ActivityLinkedProductsListAdapter.DeveloperViewHolder>() {
    private var isHeaderVisible: Boolean=false
    var onActivitySelected: ((LinkedIssue) -> Unit)? = null

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemLinkedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var linkedTechnical: LinkedTechnical = dataList[position]!!
        holder.binding.setVariable(BR.model, linkedTechnical)



        if (position>0 && linkedTechnical.product_id<1  && !isHeaderVisible){
            holder.binding.outPurchaseText.visibility=VISIBLE
            isHeaderVisible=true
        }else{
            holder.binding.outPurchaseText.visibility= GONE
        }

        if (dataList.size==1 && linkedTechnical.product_id==0){
            holder.binding.outPurchaseText.visibility=VISIBLE
        }

        if (linkedTechnical.product_id==0){
            holder.binding.productDetails.visibility= GONE
            holder.binding.tvProductTitle.visibility= VISIBLE
        }
        val linkedIssue: LinkedTechnical = dataList[position]
        holder.binding.setVariable(BR.model, linkedIssue)
        holder.binding.tvAddToCarts.setOnClickListener {
            if (onAddToCartClick.isNotNull())
                onAddToCartClick?.invoke(linkedIssue.product_base_name)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLinkedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}