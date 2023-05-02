package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemProductBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class ProductsAdapter (
    val list: List<Item>,
    private val listener: (Int) -> Unit,
): RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val product = list[position].product_sku_list?.get(list[position].product_sku_list?.size!! -1)
            holder.binding.item = product

            holder.binding.productDetailsContainer.setOnClickListener {
                listener.invoke(product?.product_id!!)
            }
        }catch (ex:Exception){
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}