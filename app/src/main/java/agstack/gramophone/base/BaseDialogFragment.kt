package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.hasInternetConnection
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import java.util.*

abstract class BaseDialogFragment<B : ViewDataBinding, N : BaseNavigator, V : BaseViewModel<N>> :
    DialogFragment(),BaseNavigator
{
    protected lateinit var binding: B
    private var mActivity :Activity ?=null
    protected var mViewModel: V? = null
    private var mContext: Context? = null


    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.mActivity = activity
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setWidthPercent(95)
    }

    private fun setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        val percentHeight = rect.height() * percent / 2
        dialog?.window?.setLayout(percentWidth.toInt(), percentHeight.toInt())


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
        this.binding = DataBindingUtil.inflate<B>(
           inflater,
            getLayoutID(),
            container,
            false
        )


        this.mViewModel = getViewModel()
        this.binding.setVariable(getBindingVariable(), mViewModel)
        mViewModel?.setNavigator(this as N?)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);

        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }



    override fun isNetworkAvailable(): Boolean {
        return hasInternetConnection(requireContext())
    }

    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            startActivity(this)
        }
    }

    override fun <T> openAndFinishActivity(cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)

        }
    }

    override fun <T : Activity> openActivityWithBottomToTopAnimation(
        cls: Class<T>,
        extras: Bundle?
    ) {
        Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

        }
    }


    override fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(
        cls: Class<T>,
        extras: Bundle?
    ) {
        Intent(context, cls).apply {

            if (extras != null)
                putExtras(extras)
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)

        }
    }

    override fun requestPermission(permission: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        return LocaleManagerClass.getLangCodeFromPreferences(activity)
    }

    override fun getMessage(stringResourceId: Int): String {
        return getString(stringResourceId)
    }

    override fun showToast(stringResourceId: Int) {
        Toast.makeText(mContext, stringResourceId, Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()


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


    override fun getGeoCoder(): Geocoder {
        return Geocoder(activity, Locale.getDefault())
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

}