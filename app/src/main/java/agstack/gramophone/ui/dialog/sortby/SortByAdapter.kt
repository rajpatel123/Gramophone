package agstack.gramophone.ui.dialog.sortby


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemSortByBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class SortByAdapter(
    val context: Context, private val sortDataList: ArrayList<SortByData>?,
    private val listener: (String) -> Unit,
) :
    RecyclerView.Adapter<SortByAdapter.CustomViewHolder>() {
    private val typefaceMedium = ResourcesCompat.getFont(context, R.font.manrope_medium)
    private val typefaceBold = ResourcesCompat.getFont(context, R.font.manrope_bold)
    var lastSelectPosition: Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSortByBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val sortData: SortByData = sortDataList!![position]
        holder.binding.setVariable(BR.model, sortData)
        val mBinding = holder.binding as ItemSortByBinding
        if (sortDataList[position].isSelected) {
            lastSelectPosition = position
            holder.binding.tvSortingType.typeface = typefaceBold
        } else {
            holder.binding.tvSortingType.typeface = typefaceMedium
        }
        mBinding.item.setOnClickListener {
            sortDataList[lastSelectPosition].isSelected = false
            lastSelectPosition = position
            sortData.isSelected = true
            listener.invoke(sortData.sortByCode)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return sortDataList!!.size
    }

    inner class CustomViewHolder(var binding: ItemSortByBinding) :
        RecyclerView.ViewHolder(binding.root)
}