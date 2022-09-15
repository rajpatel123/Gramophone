package agstack.gramophone.ui.address.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemStateListBinding
import agstack.gramophone.ui.address.model.State
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.logging.Handler

class StateListAdapter(private val stateList: List<State>) :
    RecyclerView.Adapter<StateListAdapter.ViewHolder>() {
    var selectedState: ((State) -> Unit)? = null
    var lastSelectPosition: Int = 0
    var mStateList = stateList
    var setImage: SetImage? = null
    lateinit var context: Context


    interface SetImage {
        fun onImageSet(imageUrl: String,iv: ImageView)
    }

    fun setImageListener(setImage: SetImage) {
        this.setImage = setImage
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
       // context = viewGroup.context
        return ViewHolder(
            ItemStateListBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mStateList[position]
        var state: State = mStateList[position]!!
//        holder.binding.setVariable(BR.model, state)
        //val mBinding = holder.binding as ItemStateListBinding
        //onStateSelection?.onStateSelect(state)

        if (state.selected) {
            holder.binding.ivStateImage.borderColor = ContextCompat.getColor(context, R.color.green_dark)
        } else {
            holder.binding.ivStateImage.borderColor = ContextCompat.getColor(context, R.color.white)
        }
        setImage?.onImageSet(state.image!!,holder.binding.ivStateImage)
        holder.binding.llLanguageLinearLayout.setOnClickListener {
            mStateList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            state.selected = true
            selectedState?.invoke(state)
            notifyDataSetChanged()
//            onStateSelection?.onStateSelect(state)
        }


    }

    override fun getItemCount(): Int {
        return mStateList.size ?: 0
    }

    inner class ViewHolder(var binding: ItemStateListBinding) :
        RecyclerView.ViewHolder(binding.root)
}