package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_book.view.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.RetrofitInstance
import th.ac.kku.coe.swabook.dataclass.ChatItem
import th.ac.kku.coe.swabook.dataclass.Notification
import th.ac.kku.coe.swabook.dataclass.PushNotification
import th.ac.kku.coe.swabook.dataclass.UserItem
import th.ac.kku.coe.swabook.recyclerview.ChatAdapter
import timber.log.Timber
import java.lang.Exception

class ChatFragment : Fragment() {

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var topic = ""
    private lateinit var mView: View

    private var userName: String? = null
    private var userID: String? = null
    private var userImage: String? = null

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var chatList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false)
        retrieveArguments()
        recyclerView = mView.findViewById(R.id.recycler_chat)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 1)
        chatAdapter = ChatAdapter(mView.context)
        recyclerView.adapter = chatAdapter
        chatAdapter.setUserDataList(chatList)

        mView.imgBack.setOnClickListener {
            mView.findNavController().navigate(R.id.action_ChatFragment_to_AllChatFragment)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser

        mView.userName.text = userName
       /* if (userImage == "") {
            imgProfile.setImageResource(R.drawable.user_img)
        } else {
            Glide.with(this@ChatFragment).load(userImage).into(imgProfile)
        }*/


        // Real Time Database
        /*reference = FirebaseDatabase.getInstance().getReference("Users").child(userID!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(UserItem::class.java)
                mView.userName.text = user!!.userName
                if (user.userImage == "") {
                    imgProfile.setImageResource(R.drawable.user_img)
                } else {
                    Glide.with(this@ChatFragment).load(user.userImage).into(imgProfile)
                }
            }
        })*/

        mView.send_message_btn.setOnClickListener {
            var message = message_edit_text.text.toString()

            if (message.isEmpty()) {
                /*Toast.makeText(context, "message is empty", Toast.LENGTH_SHORT).show()*/
                message_edit_text.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userID!!, message)
                message_edit_text.setText("")
                /*  topic = "/topics/$userID"
                  PushNotification(
                      Notification(userName!!, message),
                      topic
                  ).also {
                      sendNotification(it)
                  }*/
            }
        }

        readMessage(firebaseUser!!.uid, userID!!)
        Timber.i("onCreateView called")
        return mView
    }

    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = ChatFragmentArgs.fromBundle(arguments)
            userName = args.argUserName
            userID = args.argUserID
            userImage = args.argUserImg
        }
    }


    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val reference: DatabaseReference? = FirebaseDatabase.getInstance().reference

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message

        reference!!.child("Chat").push().setValue(hashMap)

    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(ChatItem::class.java)

                    if (chat!!.senderId == senderId && chat.receiverId == receiverId ||
                        chat.senderId == receiverId && chat.receiverId == senderId
                    ) {
                        chat.img = userImage.toString()
                        chatList.add(chat)
                    }
                }
                chatAdapter.setUserDataList(chatList)
                chatAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(chatList.size - 1)
            }
        })
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }

    // Timber
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}