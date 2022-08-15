package agstack.gramophone.base

import agstack.gramophone.R
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
        Intent(this, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)

        }
    }
    override fun <T:Activity> openActivityWithBottomToTopAnimation(cls: Class<T>, extras: Bundle?) {
        Intent(this, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        }
    }


    override fun <T> openAndFinishActivity(cls: Class<T>, extras: Bundle?) {
        Intent(this, cls).apply {

            if (extras != null)
                putExtras(extras)
            startActivity(this)
            finish()

        }
    }

    override fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(cls: Class<T>, extras: Bundle?) {
        Intent(this, cls).apply {

            if (extras != null)
                putExtras(extras)
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)

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
        return true
    }

    /**
     * Changing signature for now will have to update it later
     */
    override fun requestPermission(permission: String): Boolean {
        return checkPermission(permission)
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

    override fun showToast(stringResourceId: Int) {
        Toast.makeText(this, getString(stringResourceId), Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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

    fun checkPermission(permission: String): Boolean {
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

    override fun proceedCall(helpLineNo: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber = helpLineNo
        bottomSheet.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.unbind()
    }

    override fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }
}