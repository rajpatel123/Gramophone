package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseDataRelatedProduct(


    @field:SerializedName("related_product_list")
    val relatedProductList: List<RelatedProductItem?>? = null
) : Parcelable
