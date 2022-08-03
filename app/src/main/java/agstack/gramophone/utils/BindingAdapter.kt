package agstack.gramophone.utils

import agstack.gramophone.R
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import android.widget.ProgressBar
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


        @BindingAdapter("progressAdapter")
        @JvmStatic
        fun progressAdapter(progressBar: ProgressBar ,progress:Int){

            progressBar.progress = progress*2*10

        }

        @BindingAdapter("percentageOff")
        @JvmStatic
        fun percentageOff(textView: TextView,model: RelatedProductItem){
            /*Formula = (MP-SP/SP *100 )*/
            val numarator = ((model.mrpPrice!!.toFloat()-(model.salesPrice)!!.toFloat())*100)
            val denominator = model.salesPrice.toFloat()
            val percentage = numarator/denominator
            val formatted_percentage = String.format("%.02f", percentage);
            textView.setText(formatted_percentage+" % Off")
        }

        @BindingAdapter("htmlText")
        fun setHtmlTextValue(textView: TextView, htmlText: String?) {
            if (htmlText == null) return
            val result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(htmlText)
            }
            textView.text = result
        }


    }
}