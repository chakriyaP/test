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
import th.ac.kku.coe.swabook.dataclass.BookItem

class BookAdapter(val context: Context, private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    var bookList = emptyList<BookItem>()

    internal fun setBookDataList(bookList: List<BookItem>) {
        this.bookList = bookList
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookImg: ImageView = itemView.findViewById(R.id.bookImg)
        var bookName: TextView = itemView.findViewById(R.id.bookName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(layoutView)
    }

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.apply {
            holder.bookName.text = book.name
            Glide.with(holder.itemView).asBitmap().load(bookList[position].image)
                .into(holder.bookImg)
            holder.itemView.setOnClickListener {
                clickListener.onItemClick(book)
            }
        }
    }
}


