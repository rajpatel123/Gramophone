package agstack.gramophone.widget

import agstack.gramophone.R
import agstack.gramophone.databinding.FilePickerBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FilePicker() : BottomSheetDialogFragment() {

    private lateinit var mBinding: FilePickerBinding
    var onCamera: (() -> Unit)? = null
    var onGallery: (() -> Unit)? = null
    var onDocument: (() -> Unit)? = null
    var showDoc: Boolean = false

    constructor(showDoc: Boolean?) : this() {
        this.showDoc = showDoc!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.file_picker, container, false)
        val myView: View = mBinding.root

        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (showDoc)
            mBinding.documentLl.visibility = View.VISIBLE
        else
            mBinding.documentLl.visibility = View.GONE


        mBinding.cancel.setOnClickListener {

            dismiss()
        }

        mBinding.cameraLl.setOnClickListener {

            onCamera?.invoke()
        }
        mBinding.galleryLl.setOnClickListener {

            onGallery?.invoke()
        }

        mBinding.documentLl.setOnClickListener {

            onDocument?.invoke()
        }
    }


    fun setOnOptionSelectedListener(onCamera: () -> Unit, onGallery: () -> Unit) {
        this.onCamera = onCamera
        this.onGallery = onGallery

    }
}
