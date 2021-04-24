package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener3
import th.ac.kku.coe.swabook.dataclass.UserItem
import th.ac.kku.coe.swabook.recyclerview.UserAdapter
import timber.log.Timber

class AllChatFragment : Fragment(), OnItemClickListener3 {

    private lateinit var mView: View

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList = mutableListOf<UserItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_all_chat, container, false)
        recyclerView = mView.findViewById(R.id.recycler_alluser)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 1)
        userAdapter = UserAdapter(mView.context, this)
        recyclerView.adapter = userAdapter
        userAdapter.setUserDataList(userList)
        getUsersList()
        Timber.i("onCreateView called")
        return mView
    }

    override fun onItemClick(user: UserItem) {
        Timber.i(user.userName)
        navigateByDirection(mView, user)
    }

    private fun navigateByDirection(view: View, user: UserItem) {
        val direction = AllChatFragmentDirections.actionAllChatFragmentToChatFragment(
            argUserID = user.userId,
            argUserName = user.userName,
            argUserImg = user.userImage
        )
        view.findNavController().navigate(direction)
    }

    private fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(firebase.uid).collection("ChatWith")
            .get()
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    userList.clear()
                    for (document in it.result!!)
                        userList.add(document.toObject(UserItem::class.java))
                }
                userAdapter.setUserDataList(userList)
                userAdapter.notifyDataSetChanged()
            }

        /*// Real time Database
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(UserItem::class.java)

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(UserItem::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {
                        userList.add(user)
                    }
                }
                userAdapter.setUserDataList(userList)
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })*/
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