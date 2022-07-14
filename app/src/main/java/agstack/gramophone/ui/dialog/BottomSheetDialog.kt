package agstack.gramophone.ui.dialog

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.BottomSheetDialogHelpBinding
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDialog : BaseBottomSheetDialog<BottomSheetDialogHelpBinding,DialogNavigator,DialogViewModel>(),DialogNavigator {

    private val dialogViewModel: DialogViewModel by viewModels()

    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): BottomSheetDialogHelpBinding = BottomSheetDialogHelpBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun getLayoutID(): Int {
        return R.layout.bottom_sheet_dialog_help
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): DialogViewModel {
        return dialogViewModel
    }

    override fun getBundle(): Bundle? {
        return arguments
    }

    override fun callNow(intent: Intent) {
        startActivity(intent,null)
    }

    override fun closeDialog() {
        dismiss()
    }
}