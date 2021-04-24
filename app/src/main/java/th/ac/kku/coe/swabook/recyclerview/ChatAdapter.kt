package th.ac.kku.coe.swabook.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.ChatItem

class ChatAdapter(val context: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object {
        private const val MESSAGE_TYPE_LEFT = 0
        private const val MESSAGE_TYPE_RIGHT = 1
    }

    var firebaseUser: FirebaseUser? = null
    var chatList = emptyList<ChatItem>()

    internal fun setUserDataList(chatList: List<ChatItem>) {
        this.chatList = chatList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ChatViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ChatViewHolder(view)
        }
    }

    override fun getItemCount() = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.message.text = chat.message
        Glide.with(context).load(chat.img).placeholder(R.drawable.user_img).into(holder.imgUser)

        holder.apply {
            holder.message.text = chat.message
            Glide.with(holder.itemView).asBitmap().load(chatList[position].img)
                .placeholder(R.drawable.user_img)
                .into(holder.imgUser)
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tvMessage)
        val imgUser: CircleImageView = itemView.findViewById(R.id.userImage)

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }
}