package agstack.gramophone.binding

import agstack.gramophone.R
import agstack.gramophone.utils.Utility
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import kotlin.math.roundToInt


@BindingAdapter("product_image")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}


@BindingAdapter("state_image")
fun bindStateImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("profile_image")
fun bindProfileImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .error(R.drawable.dummy_profile)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("login_banner")
fun bindLoginBannerImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .error(R.drawable.connected)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)

    }
}

@BindingAdapter(value = ["product_image", "error"], requireAll = false)
fun loadImage(view: ImageView, profileImage: String, error: Int) {
    Glide.with(view.context)
        .load(profileImage)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>,
                isFirstResource: Boolean,
            ): Boolean {
                view.setImageResource(error)
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                view.setImageDrawable(resource)
                return true
            }

        })
        .into(view)
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: String?) {
    if (imageUri == null) {
        view.setImageURI(null)
    } else {
        view.setImageURI(Uri.parse(imageUri))
    }
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: Uri?) {
    view.setImageURI(imageUri)
}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("userRatingFormat")
fun bindRating(view: TextView, rating: Double?) {
    rating?.let {
        view.text = rating.toString()
    }
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

@BindingAdapter(value = ["selectedTab", "recentSize", "pastSize"], requireAll = true)
fun orderEmptyViewHandling(
    emptyView: LinearLayout, selectedTab: Int, recentSize: Int, pastSize: Int,
) {
    when (selectedTab) {
        0 -> {
            if (recentSize > 0) {
                emptyView.visibility = View.GONE
            } else {
                emptyView.visibility = View.VISIBLE
            }
        }
        1 -> {
            if (pastSize > 0) {
                emptyView.visibility = View.GONE
            } else {
                emptyView.visibility = View.VISIBLE
            }
        }
    }
}

@BindingAdapter(value = ["selectedTab", "recentSize"], requireAll = true)
fun recentOrderRecyclerHandling(
    recyclerView: RecyclerView, selectedTab: Int, recentSize: Int,
) {
    if (selectedTab == 0 && recentSize > 0) {
        recyclerView.visibility = View.VISIBLE
    } else {
        recyclerView.visibility = View.GONE
    }
}

@BindingAdapter(value = ["selectedTab", "pastSize"], requireAll = true)
fun pastOrderRecyclerHandling(
    recyclerView: RecyclerView, selectedTab: Int, pastSize: Int,
) {
    if (selectedTab == 1 && pastSize > 0) {
        recyclerView.visibility = View.VISIBLE
    } else {
        recyclerView.visibility = View.GONE
    }
}

@BindingAdapter(value = ["orderDate", "quantity", "items"], requireAll = true)
fun setDateAndItemCount(
    textView: TextView, orderDate: String, quantity: String, itemsText: String,
) {
    textView.text = Utility.getFormattedDate(orderDate,
        Utility.DATE_MONTH_YEAR_FORMAT,
        Utility.MONTH_DATE_YEAR_FORMAT) + " / " + quantity + itemsText
}

@BindingAdapter(value = ["validity_date"], requireAll = true)
fun setDateAndVisibility(
    textView: TextView, validity_date: String? = null,
) {
    try {
        if (validity_date.isNullOrEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = "Valid till " + Utility.getFormattedDate(validity_date,
                Utility.MONTH_DATE_YEAR_FORMAT,
                Utility.YEAR_MONTH_DATA_TIME_FORMAT)
        }
    } catch (e: Exception) {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("product_quantity")
fun setQuantity(textView: TextView, quantity: String) {
    val productQuantity: Int
    try {
        productQuantity = quantity.toInt()
        if (productQuantity < 10) {
            textView.text = "0".plus(productQuantity)
        } else {
            textView.text = productQuantity.toString()
        }
    } catch (e: Exception) {
        textView.text = quantity
    }
}

@BindingAdapter("reformatPrice")
fun setReformattedPrice(textView: TextView, price: String) {
    if (price.isNullOrEmpty()) {
        textView.text = "₹"
    } else {
        val ac = price.replace(".00", "")
        textView.text = "₹ " + ac
    }
}

@BindingAdapter("reformatFloatPriceToInt")
fun setReformattedIntPrice(textView: TextView, price: Float) {
    try {
        if (price.isNaN() || price.isInfinite()) {
            textView.text = "₹ 0"
        } else {
            if (price.toString().contains(".0") || price.toString().contains(".00"))
                textView.text = "₹ " + price.roundToInt().toString()
            else textView.text = "₹ " + price.toString()
        }
    } catch (e: Exception) {
        textView.text = "₹ 0"
    }
}

@BindingAdapter(value = ["mrp_price", "sales_price"], requireAll = true)
fun calculateDiscount(
    textView: TextView, mrp_price: Float, sales_price: Float,
) {
    try {
        if (mrp_price.isNaN() || sales_price.isNaN()) {
            textView.visibility = View.INVISIBLE
        } else {
            val discount = ((mrp_price - sales_price) / mrp_price) * 100
            if (discount.isNaN() || discount.isInfinite()) {
                textView.visibility = View.INVISIBLE
            } else {
                if (discount.roundToInt() == 0) {
                    textView.visibility = View.GONE
                } else {
                    textView.visibility = View.VISIBLE
                    textView.text = discount.roundToInt()
                        .toString() + textView.context.getString(R.string.percent_off)
                }
            }
        }
    } catch (e: Exception) {
        textView.visibility = View.INVISIBLE
    }
}

@BindingAdapter(value = ["mrp_price_equal", "to_sales_price"], requireAll = true)
fun mrpPriceVisibility(
    textView: TextView, mrp_price: Float, sales_price: Float,
) {
    try {
        if (mrp_price == 0f) {
            textView.visibility = View.GONE
        } else if (mrp_price == sales_price) {
            textView.visibility = View.GONE
        } else if (mrp_price < sales_price) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            if (sales_price.toString().contains(".0") || sales_price.toString().contains(".00"))
                textView.text = "₹ " + sales_price.roundToInt().toString()
            else textView.text = "₹ " + sales_price.toString()
        }
    } catch (e: Exception) {
        textView.visibility = View.GONE
    }
}

@BindingAdapter(value = ["sku_mrp_price", "sku_sales_price"], requireAll = true)
fun setPriceAndVisibility(
    textView: TextView, mrp_price: Double? = null, sales_price: String? = null,
) {
    try {
        if (mrp_price==null && sales_price.isNullOrEmpty()) {
            textView.visibility = View.INVISIBLE
        } else if (mrp_price==null && !sales_price.isNullOrEmpty()) {
            textView.text = "₹ " + sales_price.toString()
            textView.visibility = View.VISIBLE
        } else if (sales_price.isNullOrEmpty() && mrp_price!=null) {
            textView.text = "₹ " + mrp_price.toString()
            textView.visibility = View.VISIBLE
        }
        else if (!sales_price.isNullOrEmpty() && mrp_price!=null) {
            textView.text = "₹ " + mrp_price.toString()
            textView.visibility = View.VISIBLE
        }
    } catch (e: Exception) {
        textView.visibility = View.INVISIBLE
    }
}




