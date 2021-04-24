package th.ac.kku.coe.swabook.model

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
class AuthViewModel : ViewModel() {

    val user = MutableLiveData<FirebaseUser>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser

    fun register(activity: Activity, userName: String, email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        Log.d("Test Register Success", it.user?.email ?: "")
                        // Get Data to Firestore
                        val userId = Firebase.auth.currentUser?.uid!!
                        val db = FirebaseFirestore.getInstance()
                        val userData: MutableMap<String, Any> = HashMap()
                        userData["userId"] = userId
                        userData["userName"] = userName
                        userData["userEmail"] = email
                        userData["userImage"] = ""
                        db.collection("users").document(userId).set(userData)

                        // Get Data to Real Time Database
                      /*  val user: FirebaseUser? = Firebase.auth.currentUser
                        val userId: String = user!!.uid
                        databaseReference =
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)

                        val userData: HashMap<String, String> = HashMap()
                        userData["userId"] = userId
                        userData["userName"] = userName
                        userData["userEmail"] = email
                        userData["userImage"] = ""
                        databaseReference.setValue(userData)*/
                    }
                } else {
                    Log.w("Task Register Error", "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun onSignInWithEmailAndPassword(activity: Activity, email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    user.value = task.result?.user
                } else {
                    Toast.makeText(activity, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user.value = task.result?.user

                    // Get Data to Firestore
                    val user = Firebase.auth.currentUser!!
                    val db = FirebaseFirestore.getInstance()
                    val userData: MutableMap<String, Any> = HashMap()
                    userData["userId"] = user.uid
                    userData["userName"] = user.displayName!!
                    userData["userEmail"] = user.email!!
                    userData["userImage"] = user.photoUrl!!.toString()
                    db.collection("account").document(user.uid).set(userData)

                    // Get Data to Real Time Database
                   /* firebaseUser = FirebaseAuth.getInstance().currentUser!!
                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
                    val userId: String = firebaseUser.uid
                    val userName: String = firebaseUser.displayName!!
                    val userEmail : String = firebaseUser.email!!
                    val photoUrl : Uri? = firebaseUser.photoUrl
                    val userData: HashMap<String, String> = HashMap()
                    userData["userId"] = userId
                    userData["userName"] = userName
                    userData["userEmail"] = userEmail
                    userData["userImage"] = photoUrl.toString()
                    databaseReference.setValue(userData)*/
                }
            }
    }


    fun firebaseSignOut(googleSignInClient: GoogleSignInClient) {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut()
    }

    fun forgotPassword(activity: Activity, email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        Log.w("Email Sent.", "Email Sent.")
                    }
                }
            }
    }

}