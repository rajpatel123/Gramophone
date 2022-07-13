package agstack.gramophone.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.BulletSpan
import android.widget.Toast

object Utility {

    fun List<String>.toBulletedList(): CharSequence {
        return SpannableString(this.joinToString("\n")).apply {
            this@toBulletedList.foldIndexed(0) { index, acc, span ->
                val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
                this.setSpan(BulletSpan(16), acc, end, 0)
                end
            }
        }
    }

    fun showShortToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}