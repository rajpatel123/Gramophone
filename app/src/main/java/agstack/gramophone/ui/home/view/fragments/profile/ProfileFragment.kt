package agstack.gramophone.ui.home.view.fragments.profile

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentProfileBinding
import agstack.gramophone.ui.home.view.fragments.profile.viewmodel.ProfileFragmentViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileFragmentNavigator, ProfileFragmentViewModel>(),ProfileFragmentNavigator {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()


    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

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

    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_profile
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ProfileFragmentViewModel {
        return profileFragmentViewModel
    }
}