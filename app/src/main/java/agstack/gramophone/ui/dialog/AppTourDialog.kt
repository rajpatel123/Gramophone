package agstack.gramophone.ui.dialog

import agstack.gramophone.R
import agstack.gramophone.databinding.DialogAppTourBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

class AppTourDialog : DialogFragment() {
    var binding: DialogAppTourBinding? = null

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): AppTourDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = AppTourDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogAppTourBinding.inflate(inflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupUi() {
        binding?.tvSkip?.setOnClickListener { dismiss() }
        binding?.tvSkip2?.setOnClickListener { dismiss() }
        binding?.tvSkip3?.setOnClickListener { dismiss() }

        binding?.tvNextMenu?.setOnClickListener {
            binding?.llMenu?.visibility = View.GONE
            binding?.llHome?.visibility = View.VISIBLE
        }

        binding?.tvNextHome?.setOnClickListener {
            binding?.llHome?.visibility = View.GONE
            binding?.llOthers?.visibility = View.VISIBLE
            binding?.llCommunity?.visibility = View.VISIBLE

            binding?.ivThumbCommon?.setImageResource(R.drawable.ic_app_tour_community)
            binding?.tvTitleCommon?.text = getString(R.string.community)
            binding?.tvDescriptionCommon?.text = getString(R.string.app_tour_descrition_community)
        }

        binding?.tvNextCommunity?.setOnClickListener {
            when {
                binding?.llCommunity?.visibility == View.VISIBLE -> {
                    binding?.llCommunity?.visibility = View.INVISIBLE
                    binding?.llProfile?.visibility = View.VISIBLE
                    binding?.llTrade?.visibility = View.INVISIBLE

                    binding?.ivThumbCommon?.setImageResource(R.drawable.ic_app_tour_profile)
                    binding?.tvTitleCommon?.text = getString(R.string.my_profile)
                    binding?.tvDescriptionCommon?.text = getString(R.string.app_tour_descrition_profile)
                }
                binding?.llProfile?.visibility == View.VISIBLE -> {
                    binding?.llCommunity?.visibility = View.INVISIBLE
                    binding?.tvSkip3?.visibility = View.INVISIBLE
                    binding?.llProfile?.visibility = View.INVISIBLE
                    binding?.llTrade?.visibility = View.VISIBLE

                    binding?.ivThumbCommon?.setImageResource(R.drawable.ic_app_tour_trade)
                    binding?.tvNextCommunity?.text = getString(R.string.app_tour_done)
                    binding?.tvTitleCommon?.text = getString(R.string.app_tour_title_trade)
                    binding?.tvDescriptionCommon?.text = getString(R.string.app_tour_descrition_trade)
                }
                binding?.llTrade?.visibility == View.VISIBLE -> {
                    dismiss()
                }
            }

        }
    }
}