package agstack.gramophone.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

class RxSearchObservable {

    companion object {

        fun fromView(searchView : EditText) : Flowable<String> {
            return Flowable.create({ emitter ->
                searchView.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged( text: CharSequence?,start: Int,count: Int, after: Int) {
                        //emitter.onNext(text.toString())
                    }

                    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                        emitter.onNext(text.toString())
                    }

                    override fun afterTextChanged(text: Editable?) {
                        //emitter.onNext(text.toString())
                    }
                })
            }, BackpressureStrategy.BUFFER)
        }
    }

}