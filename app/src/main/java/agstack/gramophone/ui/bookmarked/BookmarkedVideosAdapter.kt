package agstack.gramophone.ui.bookmarked


import agstack.gramophone.databinding.ItemBookmarkedVideosBinding
import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.tv.model.Bookmark
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import javax.inject.Singleton

@Singleton
class BookmarkedVideosAdapter(bookmarkList: List<Bookmark>) :
    RecyclerView.Adapter<BookmarkedVideosAdapter.CustomViewHolder>() {
    var bookmarkVideoList = bookmarkList
    var onVideoClicked: ((videoId: String) -> Unit)? = null
    var onRemoveClick: ((bookmarkedVideo: Bookmark) -> Unit)? = null
    var lastSelectPosition: Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemBookmarkedVideosBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = bookmarkVideoList[position]
        holder.binding.listItemTitleContainer.setOnClickListener {
            onVideoClicked?.invoke(bookmarkVideoList[position].youtube_video_id)
        }

        holder.binding.rlMenu.setOnClickListener {
            bookmarkVideoList[position].isSelected = !bookmarkVideoList[position].isSelected
            notifyDataSetChanged()
        }

        holder.binding.rlMenuAction.setOnClickListener {
            bookmarkVideoList[position].isSelected = !bookmarkVideoList[position].isSelected
            holder.binding.rlMenuAction.visibility = View.GONE
            onRemoveClick?.invoke(bookmarkVideoList[position])
        }
    }

    override fun getItemCount(): Int {
        return bookmarkVideoList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemBookmarkedVideosBinding) :
        RecyclerView.ViewHolder(binding.root)
}