package agstack.gramophone.ui.address.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemOtherStateBinding
import agstack.gramophone.databinding.ItemStateListBinding
import agstack.gramophone.ui.address.model.State
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OtherStateListAdapter (private val stateList :List<State>) :
    RecyclerView.Adapter<OtherStateListAdapter.ViewHolder>() {
    var selectedState: ((State) -> Unit)? = null
    var lastSelectPosition: Int = 0
    var mStateList = stateList
    var onStateSelection: OnStateSelection? = null

    interface OnStateSelection {
        fun onStateSelect(state: State)
    }

    fun setLanguageSelectedListener(onLanguageSelection: OnStateSelection) {
        this.onStateSelection=onLanguageSelection
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemOtherStateBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var state: State = mStateList[position]!!
        holder.binding.setVariable(BR.model, state)
        val mBinding = holder.binding as ItemOtherStateBinding
        mBinding.llLanguageLinearLayout.setOnClickListener {
            mStateList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            state.selected = true
            notifyDataSetChanged()
            selectedState?.invoke(state)
            onStateSelection?.onStateSelect(state)
        }

    }

    override fun getItemCount(): Int {
        return mStateList.size ?: 0
    }

    inner class ViewHolder(var binding: ItemOtherStateBinding) :
        RecyclerView.ViewHolder(binding.root)
}