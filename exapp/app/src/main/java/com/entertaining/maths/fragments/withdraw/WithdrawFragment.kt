package com.entertaining.maths.fragments.withdraw

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.entertaining.maths.R
import com.entertaining.maths.activity.MainActivity
import com.entertaining.maths.databinding.FragmentWithdrawBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawFragment : Fragment(R.layout.fragment_withdraw) {

    private lateinit var binding: FragmentWithdrawBinding

    private val viewModel by viewModels<WithdrawViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWithdrawBinding.bind(view)

        binding.buttonWithdraw.setOnClickListener {
            onWithdraw()
        }
    }

    private fun onWithdraw() {
        val wallet = binding.productEditText.text?.toString()
        if (wallet.isNullOrBlank()) {
            onWithdrawFailure()
            return
        }

        viewModel.withdraw(wallet, (activity as MainActivity).viewModel.encryptedSharedPrefs) {
            requireActivity().runOnUiThread {
                findNavController().popBackStack()
            }
        }
    }

    private fun onWithdrawFailure() {
        viewModel.toast(R.string.wrong_input, Toast.LENGTH_SHORT)
    }
}
