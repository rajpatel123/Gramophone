package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemSubFilterBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SubFilterAdapter :
    RecyclerView.Adapter<SubFilterAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class CustomViewHolder(var binding: ItemSubFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}