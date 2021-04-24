package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_login_page.*
import kotlinx.android.synthetic.main.fragment_login_page.view.*
import th.ac.kku.coe.swabook.model.AuthViewModel
import th.ac.kku.coe.swabook.R
import timber.log.Timber

class LoginPageFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 850
    }

    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login_page, container, false)
        mView.register.setOnClickListener {
            mView.findNavController().navigate(R.id.action_loginPageFragment_to_registerFragment)
        }
        mView.forgot_password.setOnClickListener {
            mView.findNavController()
                .navigate(R.id.action_loginPageFragment_to_resetPasswordFragment)
        }
        Timber.i("onCreateView called")
        return mView
    }

    // Login
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        initGetDataFromTextField()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initGetDataFromTextField() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        val actionToNavHome = LoginPageFragmentDirections.actionLoginPageFragmentToFeedFragment()

        authViewModel.user.observe(requireActivity(), Observer {
            Log.d("Login Result", it.email ?: "no email")
            it?.let {
                navController.navigate(actionToNavHome)
            }
        })
        mView.findViewById<MaterialButton>(R.id.login_btn).setOnClickListener {
            // Set an error
            if (!isPasswordValid(password_edit_text.text)) {
                password_text_input.error = getString(R.string.error_password_8digi)
                if (isPasswordValid(password_edit_text.text) || !isUserValid(user_edit_text.text)) {
                    user_text_input.error = getString(R.string.error_user)
                }
            } else if (!isUserValid(user_edit_text.text)) {
                user_text_input.error = getString(R.string.error_user)
                if (isUserValid(user_edit_text.text) || !isPasswordValid(password_edit_text.text)) {
                    password_text_input.error = getString(R.string.error_password_8digi)
                }
            } else {
                password_text_input.error = null
                user_text_input.error = null
                // Sing In
                signIn()
            }

            //Clear the error
            mView.password_edit_text.setOnKeyListener { _, _, _ ->
                if (isPasswordValid(password_edit_text.text) || (isUserValid(user_edit_text.text))) {
                    password_text_input.error = null
                }
                false
            }

            mView.user_edit_text.setOnKeyListener { _, _, _ ->
                if (isPasswordValid(password_edit_text.text) || (isUserValid(user_edit_text.text))) {
                    user_text_input.error = null
                }
                false
            }
        }
        mView.findViewById<ImageView>(R.id.google).setOnClickListener {
            googleSingIn()
        }
    }

    private fun signIn() {
        authViewModel.onSignInWithEmailAndPassword(
            requireActivity(),
            mView.findViewById<TextInputEditText>(R.id.user_edit_text).text.toString(),
            mView.findViewById<TextInputEditText>(R.id.password_edit_text).text.toString()
        )
    }

    private fun googleSingIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Google login", "firebaseAuthWithGoogle:" + account.id)
                authViewModel.firebaseAuthWithGoogle(requireActivity(), account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google login", "Google sign in failed", e)
            }
        }
    }

    // Valid
    private fun isUserValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
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