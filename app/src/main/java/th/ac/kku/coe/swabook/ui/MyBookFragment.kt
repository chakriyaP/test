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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener
import th.ac.kku.coe.swabook.recyclerview.BookAdapter
import th.ac.kku.coe.swabook.dataclass.BookItem
import timber.log.Timber

class MyBookFragment : Fragment(), OnItemClickListener {

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
        mView = inflater.inflate(R.layout.fragment_mybook, container, false)
        // RecyclerView
        recyclerView = mView.findViewById(R.id.recycler_myBook)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 2)
        bookAdapter = BookAdapter(mView.context, this)
        recyclerView.adapter = bookAdapter
        bookAdapter.setBookDataList(bookList)
        readFireStoreData()
        Timber.i("onCreateView called")
        return mView
    }

    // FireStore
    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        val userID = Firebase.auth.currentUser.uid
        db.collection("book").whereEqualTo("uid", userID)
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

    // Click to Edit Detail Book Fragment
    override fun onItemClick(book: BookItem) {
        Timber.i(book.name)
        navigateByDirection(mView, book)
    }

    private fun navigateByDirection(view: View, book: BookItem) {
        val direction = MyBookFragmentDirections.actionMyBookFragmentToMyBookDetailFragment(
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