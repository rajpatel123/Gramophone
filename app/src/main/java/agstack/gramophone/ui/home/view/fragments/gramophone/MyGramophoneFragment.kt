package agstack.gramophone.ui.home.view.fragments.gramophone

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMyGramophoneBinding
import agstack.gramophone.ui.home.view.fragments.gramophone.viewmodel.MyGramophoneFragmentViewModel
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyGramophoneFragment : BaseFragment<FragmentMyGramophoneBinding,MyGramophoneFragmentNavigator, MyGramophoneFragmentViewModel>(),MyGramophoneFragmentNavigator {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val myGramophoneFragmentViewModel: MyGramophoneFragmentViewModel by viewModels()


    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyGramophoneBinding = FragmentMyGramophoneBinding.inflate(inflater, container, false)

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
      myGramophoneFragmentViewModel.initProfile()
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_my_gramophone
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): MyGramophoneFragmentViewModel {
        return myGramophoneFragmentViewModel
    }

    override fun getUserName(): String? = SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USERNAME)

    override fun getUserAddress(): String? = SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ADDRESS)

    override fun setUserImage() {
        Glide.with(this).load(SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USER_IMAGE)).into(
            binding?.ivUserProfile!!
        )
    }
}