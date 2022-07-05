package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMarketBinding
import agstack.gramophone.databinding.FragmentRelatedProductBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RelatedProductFragment :BaseFragment<FragmentRelatedProductBinding, RelatedProductNavigator, RelatedProductViewModel>(),
    RelatedProductNavigator{


    private val relatedProductViewModel: RelatedProductViewModel by viewModels()
    override fun getLayoutID(): Int {
        return R.layout.fragment_related_product
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): RelatedProductViewModel {
        return relatedProductViewModel
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRelatedProductBinding = FragmentRelatedProductBinding.inflate(inflater,container,false)


}