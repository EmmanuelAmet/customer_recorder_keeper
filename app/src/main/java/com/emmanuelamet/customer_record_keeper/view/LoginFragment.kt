package com.emmanuelamet.customer_record_keeper.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emmanuelamet.customer_record_keeper.R
import com.emmanuelamet.customer_record_keeper.databinding.FragmentLoginBinding
import com.emmanuelamet.customer_record_keeper.model.User
import com.emmanuelamet.customer_record_keeper.repository.Repository
import com.emmanuelamet.customer_record_keeper.viewModel.MainViewModel
import com.emmanuelamet.customer_record_keeper.viewModelFactory.MainViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLoginUser.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString().trim()
            val password = binding.edtPasswordLogin.text.toString().trim()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, "All fields required.", Toast.LENGTH_LONG).show()
            } else {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

                val user = User(email, password)
                viewModel.loginUser(user)

                viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        Navigation.findNavController(it)
                            .navigate(LoginFragmentDirections.actionLoginFragmentToNavHome())
                        Log.d("StatusCodeLogin", response.code().toString())
                        Log.d("StatusCodeLogin", response.message().toString())
                        Log.d("StatusCodeLogin", response.body().toString())
                        Log.d("StatusCodeLogin", response.headers().toString())
                        Log.d("StatusCodeLogin", response.body()?.email.toString())

                    } else if (response.code().toString() == "400") {
                        Toast.makeText(context, "Invalid email or password!", Toast.LENGTH_LONG).show()
                        Log.d("StatusCodeLogin", response.code().toString())
                        Log.d("StatusCodeLogin", response.message().toString())
                        Log.d("StatusCodeLogin", response.errorBody().toString())
                    } else {
                        Toast.makeText(
                            context,
                            "Oops something unexpected happen, please try again!",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("StatusCodeLogin", response.code().toString())
                    }
                })
            }
        }


        binding.btnSignUp.setOnClickListener {
            Navigation.findNavController(it).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }
}