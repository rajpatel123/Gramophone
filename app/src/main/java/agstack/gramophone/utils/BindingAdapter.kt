package agstack.gramophone.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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


        @BindingAdapter("stringToFloatRating")
        @JvmStatic
        fun priceWithSymbol(ratingBar: RatingBar, ratingVal: String) {
            ratingBar.rating= ratingVal.toFloat()

        }

        @BindingAdapter("priceWithSymbol")
        @JvmStatic
        fun priceWithSymbol(view: TextView, price: String) {
            view.setText("\u20B9" + price);

        }

    }
}