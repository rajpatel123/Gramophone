package agstack.gramophone.ui.weather


import agstack.gramophone.databinding.ItemDayWiseForecastBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class DayWiseForecastAdapter() :
    RecyclerView.Adapter<DayWiseForecastAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemDayWiseForecastBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemDayWiseForecastBinding) :
        RecyclerView.ViewHolder(binding.root)
}