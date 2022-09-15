package agstack.gramophone.ui.referandearn.transaction.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("gramcash_txn")
	val gramcashTxn: List<GramcashTxnItem?>? = null
) : Parcelable