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
import kotlinx.android.synthetic.main.fragment_search_show.view.*
import kotlinx.android.synthetic.main.fragment_search_show.view.back_btn
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener
import th.ac.kku.coe.swabook.recyclerview.BookAdapter
import th.ac.kku.coe.swabook.dataclass.BookItem
import timber.log.Timber

class SearchShowFragment : Fragment(), OnItemClickListener {

    private lateinit var mView: View
    private var category_name: String? = null
    private var search_text: String? = null

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
        mView = inflater.inflate(R.layout.fragment_search_show, container, false)
        retrieveArguments()
        recyclerView = mView.findViewById(R.id.recycler_category_book)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 2)
        bookAdapter = BookAdapter(mView.context, this)
        recyclerView.adapter = bookAdapter
        bookAdapter.setBookDataList(bookList)
        readFireStoreData()

        mView.back_btn.setOnClickListener {
            mView.findNavController().navigate(R.id.action_SearchShowFragment_to_SearchFragment)
        }
        Timber.i("onCreateView called")
        return mView
    }

    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = SearchShowFragmentArgs.fromBundle(arguments)
            category_name = args.argCategoryName
            search_text = args.argSearchText

        }
    }
    // FireStore
    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        val userID = Firebase.auth.currentUser.uid

        if (category_name == "") {
            val search_key = search_text?.toLowerCase()
            db.collection("book").orderBy("search_key").startAt(search_key).endAt("$search_key\uf8ff")
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
            mView.search_text.text = "ผลการค้นหาของ '$search_text'"
        } else {
            db.collection("book").whereEqualTo("category", category_name)
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
            mView.search_text.text = "หมวดหมู่ : $category_name"
        }
    }

    // Click to Edit Detail Book Fragment
    override fun onItemClick(book: BookItem) {
        Timber.i(book.name)
        navigateByDirection(mView, book)
    }

    private fun navigateByDirection(view: View, book: BookItem) {
  /*      val direction = MyBookFragmentDirections.actionMyBookFragmentToMyBookDetailFragment(
            argBookName = book.name,
            argBookImg = book.image,
            argBookAuthor = book.author,
            argBookDetail = book.detail,
            argDocPath = book.doc_path
        )
        view.findNavController().navigate(direction)*/
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