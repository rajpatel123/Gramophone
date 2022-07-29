package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.databinding.GenuineRatingDialogBinding
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.product_review_dialog.view.*

class GenuineCustomerRatingAlertFragment : BottomSheetDialogFragment() {
    var binding: GenuineRatingDialogBinding? = null
    companion object {

        const val TAG = "AddEditProductDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): GenuineCustomerRatingAlertFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = GenuineCustomerRatingAlertFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GenuineRatingDialogBinding.inflate(inflater)

       // setupUi()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

    }


    private fun setupView(view: View) {
        view.tv_reviewTitle.text = arguments?.getString(KEY_TITLE)

    }
}