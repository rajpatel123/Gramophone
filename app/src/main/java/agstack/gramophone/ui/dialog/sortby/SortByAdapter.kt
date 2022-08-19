package agstack.gramophone.ui.dialog.sortby


import agstack.gramophone.R
import agstack.gramophone.databinding.ItemSortByBinding
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class SortByAdapter(val context: Context) :
    RecyclerView.Adapter<SortByAdapter.CustomViewHolder>() {
    val typefaceMedium = ResourcesCompat.getFont(context, R.font.manrope_medium)
    val typefaceBold = ResourcesCompat.getFont(context, R.font.manrope_bold)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSortByBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        if (i == 1) {
            holder.binding.ivSelected.visibility = View.VISIBLE
            holder.binding.tvSortingType.typeface = typefaceBold
            holder.binding.tvSortingType.setTextColor(ContextCompat.getColor(
                context,
                R.color.brand_color))
        } else {
            holder.binding.ivSelected.visibility = View.GONE
            holder.binding.tvSortingType.typeface = typefaceMedium
            holder.binding.tvSortingType.setTextColor(ContextCompat.getColor(
                context,
                R.color.blakish))
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class CustomViewHolder(var binding: ItemSortByBinding) :
        RecyclerView.ViewHolder(binding.root)
}