package agstack.gramophone.utils

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

fun RecyclerView.addOnScrollHiddenView(
    hiddenView: View,
    translationX: Float = 0F,
    translationY: Float = 0F,
    duration: Long = 200L
) {
    var isViewShown = true
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            when{
                dy> 0 && isViewShown -> {
                    isViewShown = false
                    hiddenView.animate()
                        .translationX(translationX)
                        .translationY(-translationY)
                        .duration = duration
                }
                dy < 0 && !isViewShown ->{
                    isViewShown = true
                    hiddenView.animate()
                        .translationX(0f)
                        .translationY(0f)
                        .duration = duration
                }
            }
        }
    })
}