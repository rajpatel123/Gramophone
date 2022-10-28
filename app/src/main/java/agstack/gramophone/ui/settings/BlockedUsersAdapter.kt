package agstack.gramophone.ui.settings


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemBlockedUserBinding
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUser
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * binding will be replaced later
 */
class BlockedUsersAdapter(private val blockedUsers: List<BlockedUser>) :
    RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder>() {
    var selectedUser: ((BlockedUser) -> Unit)? = null
    var lastSelectPosition: Int = 0
    var mBlockedUsers = blockedUsers


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemBlockedUserBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var blockedUser: BlockedUser = mBlockedUsers[position]!!
        holder.binding.setVariable(BR.model, blockedUser)
        val mBinding = holder.binding

        Glide.with(holder.binding.root.context).load(blockedUser.photoUrl).into(holder.binding.ivProfileImage)
        mBinding.tvBlockUser.setOnClickListener {
            lastSelectPosition = position
            selectedUser?.invoke(blockedUser)
        }

    }

    override fun getItemCount(): Int {
        return mBlockedUsers.size ?: 0
    }

    inner class ViewHolder(var binding: ItemBlockedUserBinding) :
        RecyclerView.ViewHolder(binding.root)
}