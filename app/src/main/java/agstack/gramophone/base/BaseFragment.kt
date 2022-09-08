package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.util.*

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

    override fun <T:Activity> openActivityWithBottomToTopAnimation(cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)
            requireActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        }
    }


    override fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)

        }
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

    override fun showToast(message: String?) {

    }

    override fun onError(message: String?) {
    }

    override fun onSuccess(message: String?) {
    }

    override fun onLoading() {
    }

    override fun hideProgressBar() {

    }



    override fun proceedCall(helpLineNo: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber = helpLineNo
        bottomSheet.show(
            requireActivity().supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun proceedOnLocationSetting() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    /*override fun checkSelfPermissions( permission: String): Boolean {
        TODO("Not yet implemented")
    }*/

    override fun getGeoCoder(): Geocoder {
        return Geocoder(activity, Locale.getDefault())
    }
}