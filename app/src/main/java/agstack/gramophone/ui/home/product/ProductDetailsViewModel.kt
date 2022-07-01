package agstack.gramophone.ui.home.product

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import agstack.gramophone.ui.home.repository.HomeRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    var mSKUList = ArrayList<ProductWeightPriceModel>()

    var mSkuOfferList = ArrayList<ProductSkuOfferModel>()


fun onAddToCartClicked(){

}

    fun getBundleData() {
       val bundle=  getNavigator()?.getBundle()
        if(bundle?.getParcelable<ProductData>("product")!=null){
           Log.d("ProductName",(bundle?.getParcelable<ProductData>("product") as ProductData).product_name.toString())
            val productData = bundle?.getParcelable<ProductData>("product")

            getNavigator()?.setToolbarTitle(productData?.product_name!!)

            mSKUList.add(ProductWeightPriceModel(1,"250","799","Gm",true))
            mSKUList.add(ProductWeightPriceModel(1,"500","999","Gm",false))
            mSKUList.add(ProductWeightPriceModel(1,"1","799","Kg",false))

            getNavigator()?.setProductSKUAdapter(ProductSKUAdapter(mSKUList)){



            }
            mSkuOfferList.add(ProductSkuOfferModel(1,"1","799","Kg",false))
            mSkuOfferList.add(ProductSkuOfferModel(2,"1","799","Kg",false))
            getNavigator()?.setProductSKUOfferAdapter(ProductSKUOfferAdapter(mSkuOfferList)){



            }

        }
    }
}