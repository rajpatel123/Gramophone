package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemFeatureProductBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FeatureProductAdapter :
    RecyclerView.Adapter<FeatureProductAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemFeatureProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/4
    }

    inner class CustomViewHolder(var binding: ItemFeatureProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}