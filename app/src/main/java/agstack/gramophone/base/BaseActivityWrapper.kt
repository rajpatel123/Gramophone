package agstack.gramophone.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivityWrapper<B : ViewDataBinding, N : BaseNavigator, V : BaseViewModel<N>> :
    AppCompatActivity(), BaseNavigator {


    protected var mViewModel :V?=null
    protected var viewDataBinding: B? = null

    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ViewDataBindingObj = DataBindingUtil.inflate<B>(
            LayoutInflater.from(this@BaseActivityWrapper),
            getLayoutID(),
            null,
            false
        )
        this.mViewModel = getViewModel()
        ViewDataBindingObj.setVariable(getBindingVariable(),mViewModel)
        setContentView(ViewDataBindingObj.root)
        mViewModel?.setNavigator(this as N?)
    }


    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        Intent(this, cls).apply {

            if (extras != null) {
                putExtras(extras)
                startActivity(this)
            }
        }
    }

    override fun <T> openAndFinishActivity(cls :Class<T>,extras:Bundle?){
        Intent(this,cls).apply {

            if(extras!=null)
                putExtras(extras)
            startActivity(this)
            finish()

        }
    }


    override fun isNetworkAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }


}