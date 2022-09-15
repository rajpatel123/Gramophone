package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemTransactionBinding
import agstack.gramophone.ui.referandearn.transaction.model.GramcashTxnItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class GramCashTransactionListAdapter(transactionList: ArrayList<GramcashTxnItem>) :
    RecyclerView.Adapter<GramCashTransactionListAdapter.CustomViewHolder>() {
    var mTransactionList = transactionList
    lateinit var mContext: Context


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemTransactionBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        val model: GramcashTxnItem = mTransactionList[position]
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

        }

    }

    override fun getItemCount(): Int {
        return mTransactionList.size
    }

    inner class CustomViewHolder(var binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
