package agstack.gramophone.utils

import agstack.gramophone.R
import android.net.Uri
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_detail.view.*


class BindingAdapter {
    companion object {
        @BindingAdapter("loadUri")
        @JvmStatic
        fun loadImageUri(imageView: ImageView, uri: String?) {

            if (uri != null)
                Glide.with(imageView).load(uri).into(imageView)
        }


        @BindingAdapter("doubleToFloatRating")
        @JvmStatic
        fun doubleToFloatRating(ratingBar: RatingBar, ratingVal: Double) {
            ratingBar.rating= ratingVal.toFloat()

        }

        @BindingAdapter("priceWithSymbol")
        @JvmStatic
        fun priceWithSymbol(view: TextView, price: String) {
            view.setText("\u20B9" + price);

        }

        @BindingAdapter("isUserFavorite")
        @JvmStatic
        fun isUserFavorite(imageView: AppCompatImageView,isUserfav:Boolean){
            if(isUserfav){
                Glide.with(imageView).load(R.drawable.ic_call).into(imageView)
            }else{
          Glide.with(imageView).load(R.drawable.ic_heart_blank).into(imageView)
            }
        }

    }
}