package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.utils.LocaleManagerClass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, N : BaseNavigator, V : BaseViewModel<N>> : Fragment(),
    BaseNavigator {
    protected var binding: B? = null

    protected var mViewModel :V?=null
    private var mContext :Context?=null
    private var mActivity :Activity ?=null


    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.mActivity = activity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateBinding(inflater, container, savedInstanceState)
        this.binding = binding

        this.mViewModel = getViewModel()
        mViewModel?.setNavigator(this as N?)



        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    abstract fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    override fun isNetworkAvailable(): Boolean {
        //  val info = ConnectivityManager?.OnNetworkActiveListener {  }
        return true
    }

    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            startActivity(this)
        }
    }

    override fun <T> openAndFinishActivity(cls :Class<T>,extras:Bundle?){
        Intent(context,cls).apply {

            if(extras!=null)
                putExtras(extras)
            startActivity(this)

        }
    }

    override fun <T> openActivityWithBottomToTopAnimation(cls: Class<T>, extras: Bundle?) {
      /*  Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)
            context.o( R.anim.slide_in_up, R.anim.slide_out_up );

        }*/
    }


    override fun requestPermission(permission: String) :Boolean{
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        return LocaleManagerClass.getLangCodeFromPreferences(activity)
    }

    override fun getMessage(stringResourceId: Int): String {
        return getString(stringResourceId)
    }

    override fun showToast(stringResourceId: Int) {
        TODO("Not yet implemented")
    }

    override fun onError(message: String?) {
    }

    override fun onSuccess(message: String?) {
    }

    override fun onLoading() {
    }




}