package agstack.gramophone.ui.offerslist.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import agstack.gramophone.databinding.OffersListItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OffersListAdapter(offersList: List<PromotionListItem>) :
    RecyclerView.Adapter<OffersListAdapter.CustomViewHolder>() {
    var mOffersList = offersList
    var selectedOffer: ((PromotionListItem) -> Unit)? = null



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            OffersListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val model: PromotionListItem = mOffersList[position]
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as OffersListItemBinding


    }

    override fun getItemCount(): Int {
      return mOffersList.size

    }

    inner class CustomViewHolder(var binding: OffersListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}