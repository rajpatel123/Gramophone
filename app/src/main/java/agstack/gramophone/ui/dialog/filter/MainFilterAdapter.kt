package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemMainFilterBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainFilterAdapter :
    RecyclerView.Adapter<MainFilterAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemMainFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        if (i==0) {
            holder.binding.view.visibility = View.VISIBLE
        } else {
            holder.binding.view.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class CustomViewHolder(var binding: ItemMainFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}