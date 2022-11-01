package agstack.gramophone.ui.articles

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class JavaScriptInterface internal constructor(c: Context) {
    var mContext: Context = c

    @JavascriptInterface
    fun getProductId(productId: Int?) {
        Toast.makeText(mContext, productId.toString(), Toast.LENGTH_SHORT).show()
    }

}