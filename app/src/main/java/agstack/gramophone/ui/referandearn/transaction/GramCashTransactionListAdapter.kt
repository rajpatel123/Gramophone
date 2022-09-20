package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemProgressBinding
import agstack.gramophone.databinding.ItemTransactionBinding
import agstack.gramophone.databinding.RatingReviewItemBinding
import agstack.gramophone.ui.home.product.activity.RatingAndReviewsAdapter
import agstack.gramophone.ui.referandearn.transaction.model.GramcashTxnItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class GramCashTransactionListAdapter(transactionList: ArrayList<GramcashTxnItem?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mTransactionList = transactionList
    lateinit var mContext: Context
    val LOADING_VIEW = 1
    val ITEM_VIEW = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = viewGroup.context

       if (viewType == ITEM_VIEW) {
            return CustomViewHolder(
            ItemTransactionBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
        } else {
            return LoadingViewHolder(
                ItemProgressBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
            )
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GramCashTransactionListAdapter.CustomViewHolder) {

        val model: GramcashTxnItem = mTransactionList?.get(position)!!
        val mBinding = holder.binding as ItemTransactionBinding
      mBinding.setVariable(BR.model, model)

        //set date Sep 11 2022
        val arr = model.transactionDate?.split(" ")
        val month = arr?.get(0).toString()
        val date = arr?.get(1).toString()
        val year = arr?.get(2).toString()

        mBinding.tvMonth.text = month
        mBinding.tvDate.text = date
        mBinding.tvYear.text = year

        when (model.transactionType) {

            "Redeemed" -> {
                mBinding.dateText.visibility = View.GONE
            }

            "Expired", "Lapsed" -> {
                mBinding.dateText.text = ""
                mBinding.dateText.visibility = View.VISIBLE
                model.triggerEvent.let {
                    mBinding.dateText.text = model.triggerEvent
                }
            }
            else -> {
                mBinding.dateText.visibility = View.VISIBLE

                if (model.isExpired!! && model.transactionDate != null) {
                    mBinding.dateText.text = mContext.getString(R.string.was_earned_on)+ " " + model.transactionDate
                } else if (!model.isExpired && model.expiryDate != null) {
                    mBinding.dateText.text =
                        mContext.getString(R.string.gram_cash_expire_on) + " " + model.expiryDate
                } else {
                    mBinding.dateText.text = ""
                }

            }
        }

        //Set Total Amount Value
        if (model.amount != null && model.ledgerType != null) {

            if (model.ledgerType == "credit") {
                if (model.isExpired!! && model.expirySoon!!) {
                    mBinding.tvAmountTotal.text = model.amount
                } else {
                    mBinding.tvAmountTotal.text = "+" + model.amount
                }
                mBinding.tvAmountTotal.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.green_dark
                    )
                )
            } else {
                if (model.isExpired!! && model.expirySoon!!) {
                    mBinding.tvAmountTotal.text= model.amount
                } else {
                    mBinding.tvAmountTotal.text= "-"+ model.amount
                }
                mBinding.tvAmountTotal.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.orange
                    )
                )
            }

        }}

    }

    override fun getItemCount(): Int {
        return mTransactionList?.size!!
    }

    fun showLoadingItem() {
        val lastItem = itemCount - 1;
        if (mTransactionList?.get(lastItem) != null) {
            mTransactionList?.add(null)
            notifyItemInserted(lastItem + 1)
        }

    }

    fun hideLoadingItem() {
        val lastItem = itemCount - 1;
        mTransactionList?.remove(null)
        notifyItemRemoved(lastItem)


    }

    override fun getItemViewType(position: Int): Int {
        if (mTransactionList?.get(position) == null) return LOADING_VIEW else return ITEM_VIEW
    }

    inner class CustomViewHolder(var binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)



        inner class LoadingViewHolder(val binding: ItemProgressBinding) :
            RecyclerView.ViewHolder(binding.root)


    }
