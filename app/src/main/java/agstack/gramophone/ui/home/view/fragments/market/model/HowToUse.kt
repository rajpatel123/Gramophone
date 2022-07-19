package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HowToUse(

	@field:SerializedName("youtube_video_id")
	val youtubeVideoId: String? = null
) : Parcelable