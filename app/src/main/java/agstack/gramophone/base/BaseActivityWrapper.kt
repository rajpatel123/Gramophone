package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.utils.*
import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.internal.utils.showToast
import java.util.*

abstract class BaseActivityWrapper<B : ViewDataBinding, N : BaseNavigator, V : BaseViewModel<N>> :
    AppCompatActivity(), BaseNavigator {

    protected var mViewModel: V? = null
    protected lateinit var viewDataBinding: B

    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(this@BaseActivityWrapper),
            getLayoutID(),
            null,
            false
        )





        this.mViewModel = getViewModel()
        viewDataBinding.setVariable(getBindingVariable(), mViewModel)
        setContentView(viewDataBinding.root)
        viewDataBinding.lifecycleOwner = this
        mViewModel?.setNavigator(this as? N)
    }


    override fun <T> openActivity(cls: Class<T>, extras: Bundle?) {
        if (hasInternetConnection(this)) {
            Intent(this, cls).apply {

                if (extras != null)
                    putExtras(extras)
                startActivity(this)

            }

        } else {
            Intent(this, LostConnectionActivity::class.java).apply {
                if (extras != null)
                    putExtras(extras)
                startActivity(this)
            }
        }
    }

    override fun <T : Activity> openActivityWithBottomToTopAnimation(
        cls: Class<T>,
        extras: Bundle?
    ) {

        if (hasInternetConnection(this)) {
            Intent(this, cls).apply {

                if (extras != null)
                    putExtras(extras)
                startActivity(this)
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }

        } else {
            Intent(this, LostConnectionActivity::class.java).apply {
                if (extras != null)
                    putExtras(extras)
                startActivity(this)
            }
        }

    }


    override fun <T> openAndFinishActivity(cls: Class<T>, extras: Bundle?) {

        if (hasInternetConnection(this)) {
            Intent(this, cls).apply {

                if (extras != null)
                    putExtras(extras)
                finish()
                startActivity(this)


            }

        } else {
            Intent(this, LostConnectionActivity::class.java).apply {
                if (extras != null)
                    putExtras(extras)
                startActivity(this)
            }
        }
    }

    override fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(
        cls: Class<T>,
        extras: Bundle?
    ) {

        if (hasInternetConnection(this)) {
            Intent(this, cls).apply {

                if (extras != null)
                    putExtras(extras)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(this)

            }

        } else {
            Intent(this, LostConnectionActivity::class.java).apply {
                if (extras != null)
                    putExtras(extras)
                startActivity(this)
            }
        }
    }
    /* fun <T:Activity> openActivityforResultWithBottomToTopAnimation(cls: Class<T>, extras: Bundle?,requestCode:Int) {
        var intent= Intent(this, cls).apply {
             if (extras != null)
                 putExtras(extras)

         }
         registerForActivityResult(
             ActivityResultContracts.StartActivityForResult()
         ) {
             if (it.resultCode == Activity.RESULT_OK) {
                 val value = it.data?.getStringExtra("input")
             }
         }.launch(intent)
     }*/


    override fun isNetworkAvailable(): Boolean {
        return hasInternetConnection(this)
    }

    /**
     * Changing signature for now will have to update it later
     */
    override fun requestPermission(permission: String): Boolean {
        return checkPermission(permission)
    }


    /* override fun checkSelfPermissions(permission: String, response: (Boolean) -> Unit): Boolean {
         return checkPermissionNew(permission,response)
     }*/
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

    fun setUpToolBar(
        enableBackButton: Boolean,
        title: String,
        @DrawableRes drawable: Int? = null,
        elevation: Boolean = false
    ) {
        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(enableBackButton)
            supportActionBar?.title = title
            drawable?.let {
                toolbar.setNavigationIcon(drawable)
            }
            toolbar.setNavigationOnClickListener { onBackPressed() }
            if (elevation) {
                toolbar.elevation = 24f
            } else {
                toolbar.elevation = 0f
            }
        }
    }

    override fun getLanguage(): String {
        return LocaleManagerClass.getLangCodeFromPreferences(this)
    }

    override fun getMessage(stringResourceId: Int): String {
        return "" + getString(stringResourceId)
    }

    override fun showToast(stringResourceId: Int) {
        Toast.makeText(this, "" + getString(stringResourceId), Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show()
    }

    override fun onError(message: String?) {
    }

    override fun onSuccess(message: String?) {
        showToast(message)
    }

    override fun onLoading() {
    }


    override fun hideProgressBar() {
    }

    override fun checkPermission(permission: String): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
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

    override fun proceedCall(helpLineNo: String, screenName: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber = helpLineNo
        bottomSheet.sourceScreen = screenName
        bottomSheet.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun proceedOnLocationSetting() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.unbind()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mViewModel?.onHelpClick("Login")
            }
        }
    }


    override fun getGeoCoder(): Geocoder {
        return Geocoder(this, Locale.getDefault())
    }

    override fun finishActivity() {
        finish()
    }

    override fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showSoftKeyboard(view: View) {
        val focusedView = view.findFocus() ?: view.apply { requestFocus() }
        val imm = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        val isShowSucceeded = imm.showSoftInput(focusedView, InputMethodManager.SHOW_IMPLICIT)
        if (!isShowSucceeded) {
            imm.toggleSoftInputFromWindow(
                view.windowToken, 0, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    override fun sendMoEngageEvent(event: String, properties: Properties) {
        MoEAnalyticsHelper.trackEvent(this, event, properties)

    }


}