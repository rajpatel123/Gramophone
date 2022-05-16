package agstack.gramophone.menu

import agstack.gramophone.R
import android.view.Menu
import androidx.annotation.ColorRes

class BottomMenuParams {
    var menu: Menu? = null
    @ColorRes
    var activeColor: Int = R.color.gray_menu_active
    @ColorRes var passiveColor: Int = R.color.gray_menu_passive
    @ColorRes var pressedColor: Int = R.color.bottom_menu_pressed
    var itemPadding: Float = 16f
    var itemTextSize : Float = 40f
    var animationDuration: Int = 300
    var endScale: Float = 0.95f
    var startScale: Float = 1f
}