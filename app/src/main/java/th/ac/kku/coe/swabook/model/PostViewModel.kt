package th.ac.kku.coe.swabook.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class PostViewModel : ViewModel() {
    var imgURI: Uri? = null

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private val imgFileName = "image/bookImg${System.currentTimeMillis()}.jpg"

    companion object {
        private const val TAG = "FirebaseStorageManager"
    }
    fun uploadImgToStorage(imgURI: Uri) {
        val uploadImg = mStorageRef.child(imgFileName).putFile(imgURI)
        uploadImg.addOnSuccessListener {
            Log.e(TAG, "Image upload successfully")
            val downloadImgUrl = mStorageRef.child(imgFileName).downloadUrl
            downloadImgUrl.addOnSuccessListener {
                Log.e(TAG, "Image Path : $it")
            }.addOnFailureListener{
                Log.e(TAG, "Image Path : Fail")
            }
        }.addOnFailureListener {
            Log.e(TAG, "Image upload failed ${it.printStackTrace()}")
        }

    }
    fun downloadImg(imgURI: Uri): String {
        val downloadImgUrl = mStorageRef.child(imgFileName).downloadUrl
        downloadImgUrl.addOnSuccessListener {
            Log.e(TAG, "Image Path : $it")
        }.addOnFailureListener{
            Log.e(TAG, "Image Path : Fail")
        }
        return imgURI.toString()
    }
}