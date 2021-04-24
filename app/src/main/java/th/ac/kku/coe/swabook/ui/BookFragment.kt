package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_book.view.*
import kotlinx.android.synthetic.main.fragment_book.view.bookName
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.BookItem
import th.ac.kku.coe.swabook.dataclass.UserItem
import th.ac.kku.coe.swabook.recyclerview.UserAdapter
import timber.log.Timber

class BookFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var firebaseUser: FirebaseUser

    private var name: String? = null
    private var image: String? = null
    private var author: String? = null
    private var detail: String? = null
    private var userName: String? = null
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_book, container, false)
        retrieveArguments()
        setHasOptionsMenu(true)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseUser.reload()
        mView.borrow_btn.setOnClickListener {
            /*mView.findNavController().navigate(R.id.action_bookFragment_to_lendFragment) */
            val message = "ฉันต้องการยืมหนังสือ $name"
            sendMessage(firebaseUser.uid, userID!!, message)
            val db = FirebaseFirestore.getInstance().collection("users")

            db.document(firebaseUser.uid).get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(UserItem::class.java)
                val userName = user?.userName
                val userUID = user?.userId
                val userImg = user?.userImage
                saveFireStoreDataToOther( userName!!, userUID!!, userImg!!)

            }

            // You Data to  Other
            db.document(userID!!).get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(UserItem::class.java)
                val userName = user?.userName
                val userUID = user?.userId
                val userImg = user?.userImage
                saveFireStoreDataToMe( userName!!, userUID!!, userImg!!)
                val direction = BookFragmentDirections.actionBookFragmentToChatFragment(
                    argUserID = user.userId,
                    argUserName = user.userName,
                    argUserImg = user.userImage
                )
                mView.findNavController().navigate(direction)
            }


        }

        Timber.i("onCreateView called")
        return mView
    }

    // FireStore
    private fun saveFireStoreDataToMe(
        userName: String,
        userUID: String,
        userImg: String,
    ) {
        val db = FirebaseFirestore.getInstance().collection("users")
        val chatWith: MutableMap<String, Any> = java.util.HashMap()
        chatWith["userId"] = userUID
        chatWith["userName"] = userName
        chatWith["userImage"] = userImg
        db.document(firebaseUser.uid).collection("ChatWith")
            .document(userID!!).set(chatWith)
    }

    private fun saveFireStoreDataToOther(
        userName: String,
        userUID: String,
        userImg: String,
    ) {
        val db = FirebaseFirestore.getInstance().collection("users")
        val chatWith: MutableMap<String, Any> = java.util.HashMap()
        chatWith["userId"] = userUID
        chatWith["userName"] = userName
        chatWith["userImage"] = userImg
        db.document(userID!!).collection("ChatWith")
            .document(firebaseUser.uid).set(chatWith)
    }

    // Send Message
    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val reference: DatabaseReference? = FirebaseDatabase.getInstance().reference

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        reference!!.child("Chat").push().setValue(hashMap)

    }

    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = BookFragmentArgs.fromBundle(arguments)
            name = args.argBookName
            image = args.argBookImg
            author = args.argBookAuthor
            detail = args.argBookDetail
            userName = args.argUserID
            userID = args.argDocPath
            mView.bookName.text = name
            /* Picasso.get().load(image).into(mView.bookImage)*/
            Glide.with(mView.context).load(image).dontAnimate().into(mView.bookImage);
            mView.bookAuthor.text = author
            mView.bookDetail.text = detail
            mView.userID.text = "โพสต์ โดย $userName"
        }
    }



    private fun getShareIntent(): Intent {
        val args = BookFragmentArgs.fromBundle(requireArguments())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(
            Intent.EXTRA_TEXT, "Book Name:  " + args.argBookName + "\nDetail:  "
                    + args.argBookDetail + "\nYou can swap this book and more ,Let's come Swabook!!"
        )
        return shareIntent
    }

    private fun shareInfo() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) {
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareInfo()
        }
        return super.onOptionsItemSelected(item)
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