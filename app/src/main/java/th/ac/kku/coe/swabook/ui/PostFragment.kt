package th.ac.kku.coe.swabook.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.UserItem
import th.ac.kku.coe.swabook.model.PostViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class PostFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var postViewModel: PostViewModel
    private var imgURI: Uri? = null
    private lateinit var firebaseUser: FirebaseUser

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private val imgFileName = "image/bookImg${System.currentTimeMillis()}.jpg"

    companion object {
        private const val REQUEST_FROM_CAMERA = 1001;
        private const val REQUEST_FROM_GALLERY = 1002;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_post, container, false)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        mView.choseImg.setOnClickListener {
            pickImgFromGallery()
        }

        // Set Category
        val category_list = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category_list)
        mView.category_auto.setAdapter(arrayAdapter)

        mView.post_btn.setOnClickListener {
            if (imgURI == null) {
                Toast.makeText(activity, "กรุณาเลือกรูปหนังสือของคุณ", Toast.LENGTH_SHORT).show()
                if (!isBookValid(bookName_edit_text.text)) {
                    bookName_text_input.error = getString(R.string.error_book)
                    Toast.makeText(context, "กรุณากรอกชื่อหนังสือ", Toast.LENGTH_SHORT).show()
                } else if (!isBookValid(bookAuthor_edit_text.text)) {
                    bookAuthor_text_input.error = getString(R.string.error_book)
                    Toast.makeText(context, "กรุณากรอกชื่อผู้แต่ง", Toast.LENGTH_SHORT).show()
                } else if (!isBookDetailValid(bookDetail_edit_text.text)) {
                    bookDetail_text_input.error = getString(R.string.error_bookDetail)
                    Toast.makeText(context, "กรุณาเกรอกข้อมูลอย่างน้อย 10 ตัวอักษร", Toast.LENGTH_SHORT).show()
                }
            } else {
                bookName_text_input.error = null
                bookAuthor_text_input.error = null
                bookDetail_text_input.error = null

                val uploadImg = mStorageRef.child(imgFileName).putFile(imgURI!!)
                uploadImg.addOnSuccessListener {
                    Timber.e("Image upload successfully")
                    val downloadImgUrl = mStorageRef.child(imgFileName).downloadUrl
                    downloadImgUrl.addOnSuccessListener {
                        Timber.e("Image Path : $it")

                        firebaseUser = FirebaseAuth.getInstance().currentUser!!
                        firebaseUser.reload()
                        val db = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
                        db.get().addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject(UserItem::class.java)
                            val userID = user?.userName
                            val userUID = user?.userId
                            val bookImg = it.toString()
                            val bookName = mView.bookName_edit_text.text.toString()
                            val bookAuthor = mView.bookAuthor_edit_text.text.toString()
                            val bookDetail = mView.bookDetail_edit_text.text.toString()
                            val bookCategory = mView.category_auto.text.toString()
                            saveFireStoreData(bookName, bookAuthor, bookDetail, bookImg, userID!!, userUID!!, bookCategory)
                        }
                    }.addOnFailureListener {
                        Timber.e("Image Path : Fail")
                    }
                }.addOnFailureListener {
                    Timber.e( "Image upload failed ${it.printStackTrace()}")
                }

                mView.findNavController().navigate(R.id.action_PostFragment_to_SuccessPostFragment)
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
        Timber.i("onCreateView called")
        return mView
    }

    // FireStore
    private fun saveFireStoreData(
        bookName: String,
        bookAuthor: String,
        bookDetail: String,
        bookImg: String,
        userID: String,
        userUID: String,
        bookCategory: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val book: MutableMap<String, Any> = HashMap()
        book["name"] = bookName
        book["author"] = bookAuthor
        book["detail"] = bookDetail
        book["image"] = bookImg
        book["userID"] = userID
        book["uid"] = userUID
        book["category"] = bookCategory
        book["search_key"] = bookName.toLowerCase()
        val doc_path = "book${System.currentTimeMillis()}"
        book["doc_path"] = doc_path
        db.collection("book").document(doc_path).set(book)
        /*db.collection("book").add(book)*/
    }

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
                    choseImg.setImageURI(postViewModel.imgURI)
                }
                REQUEST_FROM_CAMERA -> {
                    imgURI = data?.data
                    postViewModel.imgURI = imgURI
                    choseImg.setImageURI(postViewModel.imgURI)
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