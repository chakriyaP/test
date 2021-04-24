package th.ac.kku.coe.swabook.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_book.view.*
import kotlinx.android.synthetic.main.fragment_book.view.bookImage
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_mybook_edit.view.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_post.view.bookAuthor_edit_text
import kotlinx.android.synthetic.main.fragment_post.view.bookDetail_edit_text
import kotlinx.android.synthetic.main.fragment_post.view.bookName_edit_text
import kotlinx.android.synthetic.main.fragment_post.view.category_auto
import kotlinx.android.synthetic.main.fragment_profile.*
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.BookItem
import th.ac.kku.coe.swabook.dataclass.UserItem
import timber.log.Timber
import java.util.HashMap

class EditProfileFragment : Fragment() {

    private lateinit var mView: View
    private var image: Uri? = null
    private lateinit var username: String
    private lateinit var imageUrl: String
    private var databaseReference: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var storageReference: StorageReference? = null
    val userId = Firebase.auth.currentUser?.uid!!
    val db = FirebaseFirestore.getInstance()

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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        retrieveArguments()

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = FirebaseStorage.getInstance().reference

        mView.back_btn.setOnClickListener {
            mView.findNavController().navigate(R.id.action_editProfileFragment_to_ProfileFragment)
        }

        mView.update_btn.setOnClickListener {
            username = mView.userName_edit_text.text.toString().trim()
            if (username.isEmpty()) {
                mView.userName_edit_text.error = "กรุณากรอกชื่อ"
            } else {
                if (image == null) {
                    val userData: MutableMap<String, Any> = HashMap()
                    userData["userName"] = username
                    userData["userImage"] = imageUrl
                    db.collection("users").document(userId).update(userData)
                    updateFireStoreBookData(username)
                } else {
                    storageReference!!.child("profileImage/profileImg${System.currentTimeMillis()}.jpg")
                        .putFile(image!!)
                        .addOnSuccessListener {
                            val task = it.storage.downloadUrl
                            task.addOnCompleteListener { uri ->
                                imageUrl = uri.result.toString()
                                val userData: MutableMap<String, Any> = HashMap()
                                userData["userName"] = username
                                userData["userImage"] = imageUrl
                                db.collection("users").document(userId).update(userData)
                                updateFireStoreBookData(username)
                            }
                        }
                }


                mView.findNavController().navigate(R.id.action_editProfileFragment_to_ProfileFragment)
            }
        }

        mView.imgPickImage.setOnClickListener {
            pickImgFromGallery()
        }

        return mView
    }

    // Real Time Database
   /* private fun uploadData(name: String, image: Uri) = kotlin.run {
        storageReference!!.child("profileImage/profileImg${System.currentTimeMillis()}.jpg")
            .putFile(image)
            .addOnSuccessListener {
                val task = it.storage.downloadUrl
                task.addOnCompleteListener { uri ->
                    imageUrl = uri.result.toString()
                    val map = mapOf(
                        "userName" to name,
                        "userImage" to imageUrl
                    )
                    databaseReference!!.child(firebaseAuth!!.uid!!).updateChildren(map)
                }
            }
    }
*/
    // FireStore
    private fun updateFireStoreBookData(userName: String) = kotlin.run {

        db.collection("book").whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { documents ->
                val bookData: MutableMap<String, Any> = HashMap()
                bookData["userID"] = userName
                for (document in documents)
                    db.collection("book").document(document.id).update(bookData)
            }
    }


    private fun retrieveArguments() {
        arguments?.let { arguments ->
            val args = EditProfileFragmentArgs.fromBundle(arguments)
            username = args.argUserName
            imageUrl = args.argUserImg
            if (imageUrl != "") {
                Glide.with(mView.context).load(imageUrl).dontAnimate().into(mView.imgUser)
            }
            mView.userName_edit_text.setText(username)

        }
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
                    image = data?.data
                    imgUser.setImageURI(image)
                }
                REQUEST_FROM_CAMERA -> {
                    image = data?.data
                    imgUser.setImageURI(image)
                }
            }
        }
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