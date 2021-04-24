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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.`interface`.OnItemClickListener2
import th.ac.kku.coe.swabook.dataclass.SearchItem
import th.ac.kku.coe.swabook.recyclerview.*
import timber.log.Timber

class SearchFragment : Fragment(), OnItemClickListener2 {

    private lateinit var mView: View

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private var searchList = mutableListOf<SearchItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false)
        // RecyclerView
        recyclerView = mView.findViewById(R.id.recycler_category)
        recyclerView.layoutManager = GridLayoutManager(mView.context, 2)
        searchAdapter = SearchAdapter(mView.context, this)
        recyclerView.adapter = searchAdapter
        searchAdapter.setSearchDataList(searchList)
        readFireStoreData()

        mView.search_btn.setOnClickListener {
            val direction = SearchFragmentDirections.actionSearchFragmentToSearchShowFragment(
                argCategoryName = "",
                argSearchText = mView.search_text_input.text.toString()
            )
            mView.findNavController().navigate(direction)
        }

        Timber.i("onCreateView called")
        return mView
    }

    override fun onItemClick(category: SearchItem) {
        Timber.i(category.categoryName)
        navigateByDirection(mView, category)
    }

    private fun navigateByDirection(view: View, category: SearchItem) {
        val direction = SearchFragmentDirections.actionSearchFragmentToSearchShowFragment(
            argCategoryName = category.categoryName,
            argSearchText = ""
        )
        view.findNavController().navigate(direction)
    }

    // FireStore
    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("category")
            .get()
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    searchList.clear()
                    for (document in it.result!!)
                        searchList.add(document.toObject(SearchItem::class.java))
                }
                searchAdapter.setSearchDataList(searchList)
                searchAdapter.notifyDataSetChanged()
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