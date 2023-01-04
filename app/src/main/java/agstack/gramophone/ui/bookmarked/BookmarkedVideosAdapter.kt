package agstack.gramophone.ui.bookmarked


import agstack.gramophone.databinding.ItemBookmarkedVideosBinding
import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.tv.model.Bookmark
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import javax.inject.Singleton

@Singleton
class BookmarkedVideosAdapter(bookmarkList: List<Bookmark>) :
    RecyclerView.Adapter<BookmarkedVideosAdapter.CustomViewHolder>() {
    var bookmarkVideoList = bookmarkList

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemBookmarkedVideosBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = bookmarkVideoList[position]
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