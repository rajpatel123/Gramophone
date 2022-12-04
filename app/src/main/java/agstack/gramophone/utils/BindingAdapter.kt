package agstack.gramophone.utils

import agstack.gramophone.R
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.product_detail.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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
            ratingBar.rating = ratingVal.toFloat()

        }

        @BindingAdapter("priceWithSymbol")
        @JvmStatic
        fun priceWithSymbol(view: TextView, price: String?) {
            if (price != null)
                view.setText("\u20B9" + price) else
                view.visibility = View.GONE

        }


        @BindingAdapter("amountSaved")
        @JvmStatic
        fun amountSaved(view: TextView, amountSaved: Double?) {
            if (amountSaved != null) {
                view.visibility = View.VISIBLE
                view.setText("Save ₹" + amountSaved)
            } else {
                view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("isUserFavorite")
        @JvmStatic
        fun isUserFavorite(imageView: AppCompatImageView, isUserfav: Boolean) {
            if (isUserfav) {
                Glide.with(imageView).load(R.drawable.ic_heart_selected).into(imageView)
            } else {
                Glide.with(imageView).load(R.drawable.ic_heart_blank).into(imageView)
            }
        }


        @BindingAdapter("progressAdapter")
        @JvmStatic
        fun progressAdapter(progressBar: ProgressBar, progress: Int) {

            progressBar.progress = progress * 2 * 10

        }

        @BindingAdapter("percentageOff")
        @JvmStatic
        fun percentageOff(textView: TextView, model: RelatedProductItem) {
            /*Formula = (MP-SP/SP *100 )*/

            if (model.mrpPrice != null && model.salesPrice != null) {
                val numarator =
                    ((model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat()) * 100)
                val denominator = model.mrpPrice.toFloat()
                val percentage = numarator / denominator
                val formatted_percentage = String.format("%.02f", percentage);
                textView.setText(formatted_percentage + " % Off")
            }else{
                textView.visibility=View.INVISIBLE
            }
        }

        //arrowImage
        @BindingAdapter("arrowImage")
        @JvmStatic
        fun arrowImage(imageView: ImageView, model: GramcashFaqItem) {

            if (model.isExpanded) {
                Glide.with(imageView).load(R.drawable.ic_arrow_up).into(imageView)
            } else {
                Glide.with(imageView).load(R.drawable.ic_arrow_down).into(imageView)
            }
        }

        @BindingAdapter("htmlText")
        @JvmStatic
        fun setHtmlTextValue(textView: TextView, htmlText: String?) {
            if (htmlText == null) return
            val result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(htmlText)
            }
            textView.text = result
        }


        @BindingAdapter("isMRPVisible")
        @JvmStatic
        fun isMRPVisible(textView: TextView, model: ProductSkuListItem) {
            /*Formula = (MP-SP/SP *100 )*/
            val isGreater: Boolean = (model.mrpPrice!!.toFloat() > (model.salesPrice)!!.toFloat())
            if (isGreater) {
                textView.visibility = View.VISIBLE

            } else {
                textView.visibility = View.INVISIBLE
            }


        }

        @BindingAdapter("percentageOffSKUItem")
        @JvmStatic
        fun percentageOffSKUItem(textView: TextView, model: ProductSkuListItem) {
            val numarator = ((model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat()) * 100)
            val denominator = model.salesPrice.toFloat()
            val percentage = numarator / denominator
            val formatted_percentage = String.format("%.02f", percentage);
            textView.setText(formatted_percentage + " % Off")
        }

        @BindingAdapter("queryListener")
        @JvmStatic
        fun setOnQueryListener(
            materialSearchView: MaterialSearchView,
            listener: MaterialSearchView.OnQueryTextListener
        ) {
            materialSearchView.setOnQueryTextListener(listener)
        }

        @BindingAdapter("materialHideListener")
        @JvmStatic
        fun setOnMaterialBack(
            materialSearchView: MaterialSearchView,
            listener: MaterialSearchView.SearchViewListener
        ) {
            materialSearchView.setOnSearchViewListener(listener)
        }

        @BindingAdapter("format2Digit")
        @JvmStatic
        fun format2Digit(view: TextView, qty: Int) {
            view.setText(String.format("%02d", qty));

        }


        @BindingAdapter("formatDate")
        @JvmStatic
        fun formatDate(view: TextView, date: String) {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            val output = SimpleDateFormat("MMM dd,yyyy", Locale.US)

            var d: Date? = null
            try {

                d = input.parse(date)
                val formatted = output.format(d)
                view.setText("Registered on " + formatted)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


        }


        @BindingAdapter("formatDateValidTil")
        @JvmStatic
        fun formatDateValidTil(view: TextView, date: String) {
            if (date.isNotNullOrEmpty()) {
                val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val output = SimpleDateFormat("MMM dd,yyyy", Locale.US)

                var d: Date? = null
                try {

                    d = input.parse(date)
                    val formatted = output.format(d)
                    view.setText("Valid Till " + formatted)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }
        }


    }
}