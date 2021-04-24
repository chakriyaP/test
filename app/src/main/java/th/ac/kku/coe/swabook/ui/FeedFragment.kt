package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import th.ac.kku.coe.swabook.*
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener
import th.ac.kku.coe.swabook.recyclerview.BookAdapter
import th.ac.kku.coe.swabook.dataclass.BookItem
import th.ac.kku.coe.swabook.model.AuthViewModel
import timber.log.Timber


class FeedFragment : Fragment(), OnItemClickListener {

    private lateinit var mView: View

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private var bookList = mutableListOf<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_feed, container, false)
        // RecyclerView
        recyclerView = mView.findViewById(R.id.recycler_book)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 2)
        bookAdapter = BookAdapter(mView.context, this)
        recyclerView.adapter = bookAdapter
        bookAdapter.setBookDataList(bookList)
        readFireStoreData()

        setHasOptionsMenu(true)
        Timber.i("onCreateView called")
        return mView
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())||super.onOptionsItemSelected(item)
    }

    // FireStore
    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("book")
            .get()
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    bookList.clear()
                    for (document in it.result!!)
                        bookList.add(document.toObject(BookItem::class.java))
                    }
                    bookAdapter.setBookDataList(bookList)
                    bookAdapter.notifyDataSetChanged()
                }
    }

    // Click to Detail Book Fragment
    override fun onItemClick(book: BookItem) {
        Timber.i(book.name)
        val userUID = Firebase.auth.currentUser?.uid
        Timber.i(book.uid)
        Timber.i(userUID)
        if (userUID == book.uid) {
            // Other Book
            navigateByDirection2(mView, book)
        } else {
            // Your Book
            navigateByDirection(mView, book)
        }
    }

    private fun navigateByDirection(view: View, book: BookItem) {
        val direction = FeedFragmentDirections.actionFeedFragmentToBookFragment(
            argBookName = book.name,
            argBookImg = book.image,
            argBookAuthor = book.author,
            argBookDetail = book.detail,
            argUserID = book.userID,
            argDocPath = book.uid
        )
        view.findNavController().navigate(direction)
    }

    private fun navigateByDirection2(view: View, book: BookItem) {
        val direction = FeedFragmentDirections.actionFeedFragmentToMyBookDetailFragment(
            argBookName = book.name,
            argBookImg = book.image,
            argBookAuthor = book.author,
            argBookDetail = book.detail,
            argDocPath = book.doc_path,
            argCategoryName = book.category
        )
        view.findNavController().navigate(direction)
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