package th.ac.kku.coe.swabook.dataclass

data class ChatItem(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    var img: String = ""
)