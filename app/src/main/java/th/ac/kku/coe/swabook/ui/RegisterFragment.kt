package th.ac.kku.coe.swabook.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login_page.password_text_input
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.back_btn
import th.ac.kku.coe.swabook.model.AuthViewModel
import th.ac.kku.coe.swabook.R
import timber.log.Timber

class RegisterFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.back_btn.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginPageFragment)
        }
        // Set an error if the password is less than 8 characters.
        view.register_btn.setOnClickListener {
            if (!isPasswordValid(password_edit_text.text) || !isPasswordValid(
                    password_confirm_edit_text.text
                )
            ) {
                password_text_input.error = getString(R.string.error_password_8digi)
                password_confirm_text_input.error = getString(R.string.error_password_8digi)
                userID_text_input.error = getString(R.string.error_user)
                email_edit_input.error = getString(R.string.error_space)
            } else {
                password_text_input.error = null // Clear the error
                password_confirm_text_input.error = null // Clear the error
                userID_text_input.error = null
                email_edit_input.error = null

                val getName = view.findViewById<TextInputEditText>(R.id.userID_edit_text)
                val getEmail = view.findViewById<TextInputEditText>(R.id.email_edit_text)
                val getPass = view.findViewById<TextInputEditText>(R.id.password_edit_text)
                val getPassConfirm =
                    view.findViewById<TextInputEditText>(R.id.password_confirm_edit_text)

                val nameText = getName.text.toString()
                val emailText = getEmail.text.toString()
                val passText = getPass.text.toString()
                val passConfirmText = getPassConfirm.text.toString()
                if (passText == passConfirmText) {
                    authViewModel.register(requireActivity(), nameText, emailText, passText)
                    view.findNavController()
                        .navigate(R.id.action_registerFragment_to_loginPageFragment)
                } else {
                    Toast.makeText(activity, getString(R.string.rePassword), Toast.LENGTH_LONG)
                        .show()
                }

            }
        }
        Timber.i("onCreateView called")
        return view
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