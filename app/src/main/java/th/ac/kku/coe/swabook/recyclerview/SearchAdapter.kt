package th.ac.kku.coe.swabook.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener2
import th.ac.kku.coe.swabook.dataclass.SearchItem

class SearchAdapter(val context: Context, private val clickListener: OnItemClickListener2) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var searchList = emptyList<SearchItem>()

    internal fun setSearchDataList(searchList: List<SearchItem>) {
        this.searchList = searchList
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImg: ImageView = itemView.findViewById(R.id.category_img)
        var categoryName: TextView = itemView.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return SearchViewHolder(layoutView)
    }

    override fun getItemCount() = searchList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val search = searchList[position]
        holder.apply {
            holder.categoryName.text = search.categoryName
            Glide.with(holder.itemView).asBitmap().load(searchList[position].categoryImage)
                .into(holder.categoryImg)
            holder.itemView.setOnClickListener {
                clickListener.onItemClick(search)
            }
        }
    }
}