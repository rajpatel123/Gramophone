package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByCropsBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCropsAdapter(private val cropList: List<CropData>?,
                         private val listener: (String) -> Unit) :
    RecyclerView.Adapter<ShopByCropsAdapter.CustomViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByCropsBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = cropList?.get(i)
        holder.binding.itemView.setOnClickListener {
            /*onItemClicked?.invoke(cropList?.get(i)?.cropId.toString())*/
            listener.invoke(cropList?.get(i)?.cropId.toString())
        }
    }

    override fun getItemCount(): Int {
        return cropList?.size!!
    }

    inner class CustomViewHolder(var binding: ItemShopByCropsBinding) :
        RecyclerView.ViewHolder(binding.root)
}