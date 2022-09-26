package agstack.gramophone.ui.offerslist.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemProgressBinding
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import agstack.gramophone.databinding.ItemTransactionBinding
import agstack.gramophone.databinding.OffersListItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.ui.referandearn.transaction.GramCashTransactionListAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OffersListAdapter(offersList: ArrayList<DataItem?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var mOffersList = offersList
    lateinit var mContext: Context
    var selectedOffer: ((DataItem) -> Unit)? = null
    val LOADING_VIEW = 1
    val ITEM_VIEW = 0



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        mContext = viewGroup.context

        if (viewType == ITEM_VIEW) {
            return CustomViewHolder(
                OffersListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            )
        } else {
            return LoadingViewHolder(
                ItemProgressBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
            )
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OffersListAdapter.CustomViewHolder) {

        val model: DataItem? = mOffersList?.get(position)
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as OffersListItemBinding

        mBinding.icOfferDetails.setOnClickListener {
            selectedOffer?.invoke(model!!)

        }}


    }

    override fun getItemCount(): Int {
      return mOffersList?.size!!

    }


    fun showLoadingItem() {
        val lastItem = itemCount - 1;
        if (mOffersList?.get(lastItem) != null) {
            mOffersList?.add(null)
            notifyItemInserted(lastItem + 1)
        }

    }

    fun hideLoadingItem() {
        val lastItem = itemCount - 1;
        mOffersList?.remove(null)
        notifyItemRemoved(lastItem)


    }

    override fun getItemViewType(position: Int): Int {
        if (mOffersList?.get(position) == null) return LOADING_VIEW else return ITEM_VIEW
    }

    inner class CustomViewHolder(var binding: OffersListItemBinding) :
        RecyclerView.ViewHolder(binding.root)




    inner class LoadingViewHolder(val binding: ItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root)
}