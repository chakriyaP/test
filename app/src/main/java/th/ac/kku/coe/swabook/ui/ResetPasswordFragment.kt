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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import th.ac.kku.coe.swabook.model.AuthViewModel
import th.ac.kku.coe.swabook.R
import timber.log.Timber


class ResetPasswordFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_reset_password, container, false)
        mView.back_btn.setOnClickListener {mView.findNavController().navigate(R.id.action_resetPasswordFragment_to_loginPageFragment)}
        return mView
    }

    // Reset Password
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        initGetDataFromTextField()
    }

    private fun initGetDataFromTextField() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        mView.findViewById<MaterialButton>(R.id.resetpassword_btn).setOnClickListener {
            // Set an error
            if (!isUserValid(userID_edit_text.text)) {
                userID_text_input.error = getString(R.string.error_user)
            } else {
                userID_text_input.error = null
                // Send Email
                resetPassword()
                Toast.makeText(activity,"ส่งลิ้งเปลี่ยนรหัสผ่านไปที่อีเมลของคุณแล้ว", Toast.LENGTH_LONG).show()
                /*navController.navigate(R.id.action_resetPasswordFragment_to_loginPageFragment)*/
            }
        }
    }
    private fun resetPassword() {
        authViewModel.forgotPassword(requireActivity(),
            mView.findViewById<TextInputEditText>(R.id.userID_edit_text).text.toString(),
        )
    }

    // Valid
    private fun isUserValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
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