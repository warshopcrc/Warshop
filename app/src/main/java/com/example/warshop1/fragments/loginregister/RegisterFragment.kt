package com.example.warshop1.fragments.loginregister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.warshop1.R
import com.example.warshop1.data.User
import com.example.warshop1.databinding.FragmentRegisterBinding
import com.example.warshop1.util.RegisterFieldState
import com.example.warshop1.util.RegisterValidation
import com.example.warshop1.util.Resource
import com.example.warshop1.util.validateEmail
import com.example.warshop1.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment:  Fragment(R.layout.fragment_register){
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            btnDaftar.setOnClickListener {
                val user = User(
                    usernameRegister.text.toString().trim(),
                    lastnameregister.text.toString().trim(),
                    Email.text.toString().trim(),
                    Password.text.toString()
                )
                val password = Password.text.toString()
                val confpassword =KonfirmasiPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password,confpassword)
            }
        }
        lifecycleScope.launch {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.btnDaftar.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.btnDaftar.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        binding.btnDaftar.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect {validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.Email.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.Password.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
                if (validation.confpassword is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.KonfirmasiPassword.apply {
                            requestFocus()
                            error = validation.confpassword.message
                        }
                    }
                }
            }
        }
    }
}