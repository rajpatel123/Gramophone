package agstack.gramophone.ui.order.adapter


import agstack.gramophone.databinding.ItemOrderBinding
import agstack.gramophone.ui.order.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrderListAdapter(orderDataList: List<Data>) :
    RecyclerView.Adapter<OrderListAdapter.CustomViewHolder>() {
    var orderList = orderDataList
    var onOrderDetailClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = orderList[position]
        holder.binding.tvDetail.setOnClickListener {
            onOrderDetailClicked?.invoke(orderList[position].order_id)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class CustomViewHolder(var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}