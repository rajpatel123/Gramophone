package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.utils.LocaleManagerClass
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseActivityWrapper<B : ViewDataBinding, N : BaseNavigator, V : BaseViewModel<N>> :
    AppCompatActivity(), BaseNavigator {


    protected var mViewModel :V?=null
    protected lateinit var viewDataBinding: B

    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(this@BaseActivityWrapper),
            getLayoutID(),
            null,
            false
        )
        this.mViewModel = getViewModel()
        viewDataBinding.setVariable(getBindingVariable(),mViewModel)
        setContentView(viewDataBinding.root)
        mViewModel?.setNavigator(this as? N)
    }


    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        Intent(this, cls).apply {

            if (extras != null)
                putExtras(extras)
                startActivity(this)

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
        return true
    }

    override fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    open fun replaceFragment(fragment: Fragment, TAG: String?) {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment, TAG)
            fragmentTransaction.addToBackStack(TAG)
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun setUpToolBar(enableBackButton: Boolean, title: String, @DrawableRes drawable: Int? = null) {
        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(enableBackButton)
            supportActionBar?.setTitle(title)
            drawable?.let {
                toolbar.setNavigationIcon(drawable)
            }

            toolbar.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onBackPressed()
                }
            })
        }
    }

    override fun getLanguage(): String {
        return LocaleManagerClass.getLangCodeFromPreferences(this)
    }

    override fun getMessage(stringResourceId: Int): String {
       return getString(stringResourceId)
    }

}