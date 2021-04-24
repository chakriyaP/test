package th.ac.kku.coe.swabook.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.UserItem
import th.ac.kku.coe.swabook.ui.AllChatFragment

class UserAdapter(val context: Context, private val clickListener: AllChatFragment) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var userList = emptyList<UserItem>()

    internal fun setUserDataList(userList: List<UserItem>) {
        this.userList = userList
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val temp: TextView = itemView.findViewById(R.id.temp)
        val userImage: CircleImageView = itemView.findViewById(R.id.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(layoutView)
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.apply {
            holder.userName.text = user.userName
            Glide.with(holder.itemView).asBitmap().load(userList[position].userImage)
                .placeholder(R.drawable.user_img)
                .into(holder.userImage)
            holder.itemView.setOnClickListener {
                clickListener.onItemClick(user)
            }
        }
    }
}