package th.ac.kku.coe.swabook.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import th.ac.kku.coe.swabook.model.AuthViewModel
import th.ac.kku.coe.swabook.R
import th.ac.kku.coe.swabook.dataclass.UserItem
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mView: View

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var userName: String? = null
    private var userImage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        /*firebaseUser.reload()*/
        val db = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
        db.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(UserItem::class.java)
            mView.user_name.text = user?.userName
            mView.user_ID.text = user?.userEmail

            // Get data to Edit page
            userName  = user?.userName
            userImage = user?.userImage

            if (user?.userImage == "") {
                user_image.setImageResource(R.drawable.user_img)
            } else {
                Glide.with(this).load(user?.userImage).into(mView.user_image)
            }
        }

        // Real Time Database
        /*databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserItem::class.java)
                mView.user_name.text = user?.userName
                mView.user_ID.text = user?.userEmail

                if (user?.userImage == "") {
                    user_image.setImageResource(R.drawable.user_img)
                } else {
                    Glide.with(this@ProfileFragment).load(user?.userImage).into(mView.user_image)
                }
            }
        })*/


        mView.book_btn.setOnClickListener {
            mView.findNavController().navigate(R.id.action_ProfileFragment_to_myBookFragment)
        }

        mView.edit_profile_btn.setOnClickListener {
            /*mView.findNavController().navigate(R.id.action_ProfileFragment_to_editProfileFragment)*/
            val direction =
               ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                   argUserName = userName!!,
                   argUserImg = userImage!!
                )
            mView.findNavController().navigate(direction)
        }

        mView.setting_btn.setOnClickListener {
            mView.findNavController().navigate(R.id.action_ProfileFragment_to_settingsFragment)
        }
        setHasOptionsMenu(true)
        Timber.i("onCreateView called")
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        authViewModel.user.observe(requireActivity(), Observer {
            it?.let { firebaseUser ->
                Log.d("Test Login resilt", firebaseUser.email ?: "There are no email")
            }
        })

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        mView.findViewById<MaterialCardView>(R.id.logout_btn).setOnClickListener {
            authViewModel.firebaseSignOut(googleSignInClient)
            navController.navigate(R.id.action_ProfileFragment_to_loginPageFragment)
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