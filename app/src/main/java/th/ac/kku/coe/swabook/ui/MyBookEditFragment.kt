package th.ac.kku.coe.swabook.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_book.view.bookImage
import kotlinx.android.synthetic.main.fragment_mybook_edit.*
import kotlinx.android.synthetic.main.fragment_mybook_edit.view.*
import kotlinx.android.synthetic.main.fragment_mybook_edit.view.category_auto
import kotlinx.android.synthetic.main.fragment_post.bookAuthor_edit_text
import kotlinx.android.synthetic.main.fragment_post.bookAuthor_text_input
import kotlinx.android.synthetic.main.fragment_post.bookDetail_edit_text
import kotlinx.android.synthetic.main.fragment_post.bookDetail_text_input
import kotlinx.android.synthetic.main.fragment_post.bookName_edit_text
import kotlinx.android.synthetic.main.fragment_post.bookName_text_input
import kotlinx.android.synthetic.main.fragment_post.view.bookAuthor_edit_text
import kotlinx.android.synthetic.main.fragment_post.view.bookDetail_edit_text
import kotlinx.android.synthetic.main.fragment_post.view.bookName_edit_text
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.model.PostViewModel
import timber.log.Timber
import java.util.HashMap


class MyBookEditFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var postViewModel: PostViewModel
    private var imgURI: Uri? = null

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private val imgFileName = "image/bookImg${System.currentTimeMillis()}.jpg"

    // arg
    private var name: String? = null
    private var image: String? = null
    private var author: String? = null
    private var detail: String? = null
    private var doc_path: String? = null
    private var category: String? = null


    companion object {
        private const val REQUEST_FROM_CAMERA = 1001;
        private const val REQUEST_FROM_GALLERY = 1002;
        private const val TAG = "FirebaseStorageManager"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_mybook_edit, container, false)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        retrieveArguments()
        // Set Category
        val category_list = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category_list)
        mView.category_auto.setAdapter(arrayAdapter)

        mView.bookImage.setOnClickListener {
            pickImgFromGallery()
        }

        mView.change_book_btn.setOnClickListener {
            if (!isBookValid(bookName_edit_text.text)) {
                Toast.makeText(activity, "กรุณาเใส่ชื่อหนังสือ", Toast.LENGTH_SHORT).show()
            } else if (!isBookValid(category_auto.text)) {
                Toast.makeText(activity, "กรุณาเลือกเลือกหมวดหมู่", Toast.LENGTH_SHORT).show()
            } else if (!isBookValid(bookAuthor_edit_text.text)) {
                Toast.makeText(activity, "กรุณาใส่ชื่อผู้แต่ง", Toast.LENGTH_SHORT).show()
            } else if (!isBookDetailValid(bookDetail_edit_text.text)) {
                Toast.makeText(activity, "กรุณาใส่รายละเอียดหนังสือ", Toast.LENGTH_SHORT).show()
            } else {
                bookName_text_input.error = null
                bookAuthor_text_input.error = null
                bookDetail_text_input.error = null
                if (imgURI == null) {
                    val bookImg = image!!
                    val bookName = mView.bookName_edit_text.text.toString()
                    val bookAuthor = mView.bookAuthor_edit_text.text.toString()
                    val bookDetail = mView.bookDetail_edit_text.text.toString()
                    val bookCategory = mView.category_auto.text.toString()
                    updateFireStoreData(bookName, bookAuthor, bookDetail, bookImg, bookCategory)
                } else {
                    val uploadImg = mStorageRef.child(imgFileName).putFile(imgURI!!)
                    uploadImg.addOnSuccessListener {
                        Timber.e("Image upload successfully")
                        val downloadImgUrl = mStorageRef.child(imgFileName).downloadUrl
                        downloadImgUrl.addOnSuccessListener {
                            Timber.e("Image Path : $it")
                            val bookImg = it.toString()
                            val bookName = mView.bookName_edit_text.text.toString()
                            val bookAuthor = mView.bookAuthor_edit_text.text.toString()
                            val bookDetail = mView.bookDetail_edit_text.text.toString()
                            val bookCategory = mView.category_auto.text.toString()
                            updateFireStoreData(bookName, bookAuthor, bookDetail, bookImg, bookCategory)
                        }.addOnFailureListener {
                            Timber.e("Image Path : Fail")
                        }
                    }.addOnFailureListener {
                        Timber.e("Image upload failed ${it.printStackTrace()}")
                    }
                }

                mView.findNavController()
                    .navigate(R.id.action_myBookEditFragment_to_ProfileFragment)
            }

            //Clear the error
            mView.bookName_edit_text.setOnKeyListener { _, _, _ ->
                if (isBookValid(bookName_edit_text.text)) {
                    bookName_text_input.error = null
                }
                false
            }
            mView.bookAuthor_edit_text.setOnKeyListener { _, _, _ ->
                if (isBookValid(bookAuthor_edit_text.text)) {
                    bookAuthor_text_input.error = null
                }
                false
            }
            mView.bookDetail_edit_text.setOnKeyListener { _, _, _ ->
                if (isBookValid(bookDetail_edit_text.text)) {
                    bookDetail_text_input.error = null
                }
                false
            }
        }

        mView.remove_book_btn.setOnClickListener {
            deleteFireStoreData()
            mView.findNavController().navigate(R.id.action_myBookEditFragment_to_ProfileFragment)
        }

        Timber.i("onCreateView called")
        return mView
    }

    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = MyBookEditFragmentArgs.fromBundle(arguments)
            name = args.argBookName
            image = args.argBookImg
            author = args.argBookAuthor
            detail = args.argBookDetail
            doc_path = args.argDocPath
            category = args.argCategoryName
            /*Picasso.get().load(image).into(mView.bookImage)*/
            Glide.with(mView.context).load(image).dontAnimate().into(mView.bookImage);
            mView.bookName_edit_text.setText(name)
            mView.bookAuthor_edit_text.setText(author)
            mView.bookDetail_edit_text.setText(detail)
            mView.category_auto.setText(category)
        }
    }

    // FireStore
    private fun updateFireStoreData(
        bookName: String,
        bookAuthor: String,
        bookDetail: String,
        bookImg: String,
        bookCategory: String,
    ) {
        val db = FirebaseFirestore.getInstance()
        val book: MutableMap<String, Any> = HashMap()
        book["name"] = bookName
        book["author"] = bookAuthor
        book["detail"] = bookDetail
        book["image"] = bookImg
        book["category"] = bookCategory
        db.collection("book").document(doc_path!!).update(book)
    }

    private fun deleteFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("book").document(doc_path!!)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    // Pick Image
    private fun pickImgFromGallery() {
        ImagePicker.with(this).galleryOnly().crop().start(REQUEST_FROM_GALLERY)
    }

    private fun pickImgUsingCamera() {
        ImagePicker.with(this).cameraOnly().crop().start(REQUEST_FROM_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FROM_GALLERY -> {
                    imgURI = data?.data
                    postViewModel.imgURI = imgURI
                    bookImage.setImageURI(postViewModel.imgURI)
                }
                REQUEST_FROM_CAMERA -> {
                    imgURI = data?.data
                    postViewModel.imgURI = imgURI
                    bookImage.setImageURI(postViewModel.imgURI)
                }
            }
        }
    }

    // Valid
    private fun isBookValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }

    private fun isBookDetailValid(text: Editable?): Boolean {
        return text != null && text.length >= 10
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