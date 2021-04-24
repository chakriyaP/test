package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_book.view.*
import th.ac.kku.coe.swabook.R
import timber.log.Timber


class MyBookDetailFragment : Fragment() {

    private lateinit var mView: View

    // arg
    private var name: String? = null
    private var image: String? = null
    private var author: String? = null
    private var detail: String? = null
    private var doc_path: String? = null
    private var category: String? = null


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
        /*setHasOptionsMenu(true)*/
        mView.borrow_btn.text = getString(R.string.mybook_edit_btn)
        retrieveArguments()
        mView.borrow_btn.setOnClickListener {
            val direction =
                MyBookDetailFragmentDirections.actionMyBookDetailFragmentToMyBookEditFragment(
                    argBookName = name!!,
                    argBookImg = image!!,
                    argBookAuthor = author!!,
                    argBookDetail = detail!!,
                    argDocPath = doc_path!!,
                    argCategoryName = category!!
                )
            mView.findNavController().navigate(direction)
        }
        Timber.i("onCreateView called")
        return mView
    }

    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = MyBookFragmentArgs.fromBundle(arguments)
            name = args.argBookName
            image = args.argBookImg
            author = args.argBookAuthor
            detail = args.argBookDetail
            doc_path = args.argDocPath
            category = args.argCategoryName
            mView.bookName.text = name
            /*Picasso.get().load(image).into(mView.bookImage)*/
            Glide.with(mView.context).load(image).dontAnimate().into(mView.bookImage);
            mView.bookAuthor.text = author
            mView.bookDetail.text = detail
            mView.userID.text = "โพสต์ โดย คุณ"
        }
    }

    /* private fun getShareIntent(): Intent {
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
     }*/

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