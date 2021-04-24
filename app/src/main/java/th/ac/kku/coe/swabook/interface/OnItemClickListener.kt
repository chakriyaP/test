package th.ac.kku.coe.swabook.`interface`

import th.ac.kku.coe.swabook.dataclass.BookItem
import th.ac.kku.coe.swabook.dataclass.ChatItem
import th.ac.kku.coe.swabook.dataclass.SearchItem
import th.ac.kku.coe.swabook.dataclass.UserItem

interface OnItemClickListener {

    fun onItemClick(book: BookItem)

}

interface OnItemClickListener2 {

    fun onItemClick(category: SearchItem)
}

interface OnItemClickListener3 {

    fun onItemClick(user: UserItem)

}

interface OnItemClickListener4 {

    fun onItemClick(chat: ChatItem)

}

