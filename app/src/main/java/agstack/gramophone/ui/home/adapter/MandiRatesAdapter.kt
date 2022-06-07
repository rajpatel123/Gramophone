package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemMandiRatesBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MandiRatesAdapter :
    RecyclerView.Adapter<MandiRatesAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemMandiRatesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/10
    }

    inner class CustomViewHolder(var binding: ItemMandiRatesBinding) :
        RecyclerView.ViewHolder(binding.root)
}