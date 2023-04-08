package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.hasInternetConnection
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import java.util.*

abstract class BaseFragment<B : ViewBinding, N : BaseNavigator, V : BaseViewModel<N>> : Fragment(),
    BaseNavigator {
    protected var binding: B? = null

    protected var mViewModel: V? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null


    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.mActivity = activity
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
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
        return hasInternetConnection(requireContext())
    }

    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        if (isNetworkAvailable()) {
            Intent(context, cls).apply {
                if (extras != null)
                    putExtras(extras)
                startActivity(this)
            }

        } else {
            val intent = Intent(activity, LostConnectionActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun <T> openAndFinishActivity(cls: Class<T>, extras: Bundle?) {
        if (isNetworkAvailable()) {
            Intent(context, cls).apply {

                if (extras != null)
                    putExtras(extras)
                startActivity(this)

            }

        } else {
            val intent = Intent(activity, LostConnectionActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun <T : Activity> openActivityWithBottomToTopAnimation(
        cls: Class<T>,
        extras: Bundle?
    ) {
        if (isNetworkAvailable()) {
            Intent(context, cls).apply {

                if (extras != null)
                    putExtras(extras)
                startActivity(this)
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_up,
                    R.anim.slide_out_up
                );

            }

        } else {
            val intent = Intent(activity, LostConnectionActivity::class.java)
            activity?.startActivity(intent)
        }
    }


    override fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(
        cls: Class<T>,
        extras: Bundle?
    ) {
        if (isNetworkAvailable()) {
            Intent(context, cls).apply {

                if (extras != null)
                    putExtras(extras)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(this)

            }

        } else {
            val intent = Intent(activity, LostConnectionActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun requestPermission(permission: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        if (activity != null)
            return LocaleManagerClass.getLangCodeFromPreferences(activity)
        else
            return "hi"
    }

    override fun getMessage(stringResourceId: Int): String {
        return getString(stringResourceId)
    }

    override fun showToast(stringResourceId: Int) {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String?) {
        if (activity != null)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onError(message: String?) {
    }

    override fun onSuccess(message: String?) {
    }

    override fun onLoading() {
    }

    override fun hideProgressBar() {

    }


    override fun proceedCall(helpLineNo: String, screenName: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber = helpLineNo
        bottomSheet.sourceScreen = screenName
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

    override fun getGeoCoder(): Geocoder? {
        return activity?.let { Geocoder(it, Locale.getDefault()) }
    }

    override fun checkPermission(permission: String): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            ) -> {
                requestPermissionLauncher.launch(
                    permission
                )
                return false
            }

            else -> {
                requestPermissionLauncher.launch(
                    permission
                )
                return false
            }
        }
    }

    override fun finishActivity() {
        activity?.finish()
    }

    override fun sendMoEngageEvent(event: String, properties: Properties) {
        activity?.let { MoEAnalyticsHelper.trackEvent(it, event, properties) }

    }

}