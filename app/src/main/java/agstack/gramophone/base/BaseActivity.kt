package agstack.gramophone.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

open class BaseActivity<B : ViewBinding, N : BaseNavigator, V : BaseViewModel<N>>  : AppCompatActivity(),BaseNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


   override fun <T> openActivity(cls :Class<T>,extras:Bundle?){
        Intent(this,cls).apply {

            if(extras!=null)
                putExtras(extras)
                startActivity(this)

        }
    }

    override fun isNetworkAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }


}