/*
 *
 * Copyright 2017 Rozdoum
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package agstack.gramophone.widget

import agstack.gramophone.R
import agstack.gramophone.databinding.DialogMultipleImageDetailBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil


class MultipleImageDetailDialog : androidx.fragment.app.DialogFragment() {
    lateinit var mBinding: DialogMultipleImageDetailBinding

    private var list= java.util.ArrayList<String?> ()

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_multiple_image_detail,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        list = arguments?.getStringArrayList(IMAGES_LIST)!!
        mBinding.closeButton.setOnClickListener {
            if (dialog != null) {
                dismiss()
            }
        }

        val vpZoomedImages = mBinding.vpZoomedImages
        if (list != null && list!!.size > 0) {
            val imageListingAdapter =
                FullImageAdapter(childFragmentManager, list as ArrayList<String>)


            vpZoomedImages.adapter = imageListingAdapter
            mBinding.zoomedDotsIndicator.attachTo(vpZoomedImages)


        }


    }


    companion object {
        const val IMAGES_LIST = "IMAGES_LIST"
        fun newInstance(list: java.util.ArrayList<String?>): MultipleImageDetailDialog? {
            val frag = MultipleImageDetailDialog()
            val args = Bundle()

            frag.list = list
            args.putStringArrayList(IMAGES_LIST, list)
            frag?.setArguments(args)
            return frag
        }


    }
}