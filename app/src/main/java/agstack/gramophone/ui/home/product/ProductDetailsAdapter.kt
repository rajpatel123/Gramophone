package agstack.gramophone.ui.home.product

import agstack.gramophone.BR
import agstack.gramophone.databinding.ProductDetailsRowItemBinding
import agstack.gramophone.ui.home.product.adapter.KeyValueListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.KeyPointsItem
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.keyAtIndex

class ProductDetailsAdapter(detailTypeKeyValueList: HashMap<String, ArrayList<KeyPointsItem>>) :
    RecyclerView.Adapter<ProductDetailsAdapter.CustomViewHolder>() {

    val mdetailTypeKeyValueList = detailTypeKeyValueList
    lateinit var mContext: Context
    public var isShowMoreSelected: Boolean = false

    //var detailTypeKeyValueList = HashMap<String, ArrayList<KeyPointsItem>>()



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProductDetailsAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ProductDetailsRowItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ProductDetailsAdapter.CustomViewHolder, position: Int) {


        val keyAtIndex: String? = mdetailTypeKeyValueList.keyAtIndex(position)
        val listatKeyvalue: ArrayList<KeyPointsItem> = mdetailTypeKeyValueList.getValue(keyAtIndex as String)

        var model = keyAtIndex
        val mBinding = holder.binding as ProductDetailsRowItemBinding
        mBinding.tvDetailType.text = model
   /*     if (position % 2 == 0) {
            mBinding.tvKey.setBackgroundResource(R.drawable.greensolid_with_grey_borders)
            mBinding.tvValue.setBackgroundResource(R.drawable.greensolid_with_grey_borders)
        }
*/


     mBinding.rvKeyvalueList.adapter= KeyValueListAdapter(listatKeyvalue)


    }

    override fun getItemCount(): Int {
        if (isShowMoreSelected) {
            return mdetailTypeKeyValueList.size
        } else {
            return 1
        }
    }

    inner class CustomViewHolder(var binding: ProductDetailsRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
