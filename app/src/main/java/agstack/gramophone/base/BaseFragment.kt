package agstack.gramophone.base

import android.app.AppComponentFactory
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, N : BaseNavigator, V : BaseViewModel<N>> : Fragment(),
    BaseNavigator {
    protected var binding: B? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateBinding(inflater, container, savedInstanceState)
        this.binding = binding
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

    override fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

}