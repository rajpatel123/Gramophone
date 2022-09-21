package agstack.gramophone.ui.dialog

import agstack.gramophone.databinding.PostCommentDialogBinding
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.Data
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CommentBottomSheetDialog : BottomSheetDialogFragment() {
    var binding: PostCommentDialogBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostCommentDialogBinding.inflate(inflater)

        setupUi()
        setCommentList()
        return binding!!.root

    }

    private fun setupUi() {
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setCommentList() {
        val finalList = ArrayList<Data>()

        for (i in 1..40) {
            finalList.add(Data(viewType = CommunityPostAdapter.VIEW_TYPE_TEXT,textItem = "List Item: $i"))
        }

        binding?.rvCommentsDialog?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.rvCommentsDialog?.setHasFixedSize(false)
        binding?.rvCommentsDialog?.adapter = CommentsAdapter(finalList)
    }


}