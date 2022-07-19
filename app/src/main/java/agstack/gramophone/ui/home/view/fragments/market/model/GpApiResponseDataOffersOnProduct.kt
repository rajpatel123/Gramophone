package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseDataOffersOnProduct(


    @field:SerializedName("promotion_list")
    val offersProductList: List<PromotionListItem?>? = null
) : Parcelable
