package agstack.gramophone.ui.home.product.fragment


import agstack.gramophone.databinding.FragmentRelatedProductBinding
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RelatedProductFragmentAdapter :
    RecyclerView.Adapter<RelatedProductFragmentAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
         ItemRelatedProductFragmentBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class CustomViewHolder(var binding: ItemRelatedProductFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

