package agstack.gramophone.ui.order.adapter


import agstack.gramophone.R
import agstack.gramophone.databinding.ItemOrderBinding
import agstack.gramophone.ui.order.model.Data
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull

class OrderListAdapter(orderDataList: List<Data>) :
    RecyclerView.Adapter<OrderListAdapter.CustomViewHolder>() {
    var orderList = orderDataList
    var onOrderDetailClicked: ((String, String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = orderList[position]
        if (orderList.size - 1 == position) {
            holder.binding.tabSeparator.visibility = View.GONE
        } else {
            holder.binding.tabSeparator.visibility = View.VISIBLE
        }
        when (if (orderList[position].order_status_name.isNotNull()) orderList[position].order_status_name else "") {
            "Order Placed" -> {
                holder.binding.tvOrderStatus.text = orderList[position].order_status_name
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.order_placed_text))
                holder.binding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(holder.itemView.context,
                        R.drawable.ic_order_placed),
                    null,
                    null,
                    null
                )
            }
            "Order Approved" -> {
                holder.binding.tvOrderStatus.text = orderList[position].order_status_name
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.order_approved_text))
                holder.binding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(holder.itemView.context,
                        R.drawable.ic_order_approved),
                    null,
                    null,
                    null
                )
            }
            "Order Cancelled" -> {
                holder.binding.tvOrderStatus.text = orderList[position].order_status_name
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.order_cancelled_text))
                holder.binding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(holder.itemView.context,
                        R.drawable.ic_order_cancelled),
                    null,
                    null,
                    null
                )
            }
            "Delivered" -> {
                holder.binding.tvOrderStatus.text = orderList[position].order_status_name
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.order_delivered_text))
                holder.binding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(holder.itemView.context,
                        R.drawable.ic_order_delivered),
                    null,
                    null,
                    null
                )
            }
            "Order Dispatched" -> {
                holder.binding.tvOrderStatus.text = orderList[position].order_status_name
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.order_dispatched_text))
                holder.binding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(holder.itemView.context,
                        R.drawable.ic_order_dispatched),
                    null,
                    null,
                    null
                )
            }
        }
        holder.binding.tvDetail.setOnClickListener {
            onOrderDetailClicked?.invoke(orderList[position].order_id.toString(),
                orderList[position].price.toString())
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class CustomViewHolder(var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}