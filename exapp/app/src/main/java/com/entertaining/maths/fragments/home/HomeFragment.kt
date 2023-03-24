package com.entertaining.maths.fragments.home

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.entertaining.maths.R
import com.entertaining.maths.activity.MainActivity
import com.entertaining.maths.databinding.FragmentHomeBinding
import com.entertaining.maths.extensions.openInBrowser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    val viewModel by viewModels<HomeViewModel>()

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initSharedPrefs((activity as MainActivity).viewModel.encryptedSharedPrefs)
        viewModel.initAds {
            (activity as MainActivity).showRewardAd(this)
        }

        binding = FragmentHomeBinding.bind(view)

        binding.apply {
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            imageView.setOnClickListener {
                answerEditText.clearFocus()
                imm.hideSoftInputFromWindow(root.windowToken, 0)
            }

            checkLayout.setOnClickListener {
                if (checkTextView.visibility == View.VISIBLE)
                    onCheck()
                else
                    onNext()
            }

            hintLayout.setOnClickListener {
                onHint()
            }

            supportLayout.setOnClickListener {
                onSupport()
            }

            rewardLayout.setOnClickListener {
                onReward()
            }

            answerEditText.addTextChangedListener {
                answerEditText.setTextColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
            }
        }

        viewModel.problem.observe(viewLifecycleOwner) {
            binding.checkTextView.visibility = View.VISIBLE
            binding.nextTextView.visibility = View.GONE
            binding.answerEditText.setTextColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
            binding.answerEditText.text = SpannableStringBuilder("")
        }
    }

    private fun onNext() {
        viewModel.onResult()
    }

    private fun onCheck() {
        val input = binding.answerEditText.text.toString()
        try {
            input.toInt()
        } catch (e: NumberFormatException) {
            viewModel.toast(R.string.not_a_number, Toast.LENGTH_SHORT)
            return
        }

        val isCorrect: Boolean = viewModel.checkInput(input.toInt())
        if (isCorrect) {
            binding.answerEditText.setTextColor(ResourcesCompat.getColor(resources, android.R.color.holo_green_light, null))
            onResult()
        } else {
            binding.answerEditText.setTextColor(ResourcesCompat.getColor(resources, android.R.color.holo_red_light, null))
        }
    }

    private fun onHint() {
        val correctResult = viewModel.getHint()
        binding.answerEditText.text = SpannableStringBuilder(correctResult)
        onResult()
    }

    private fun onResult() {
        binding.checkTextView.visibility = View.GONE
        binding.nextTextView.visibility = View.VISIBLE
    }

    private fun onSupport() {
        requireActivity().openInBrowser("https://t.me/+a9e2pJMgc8UyNDZi")
    }

    private fun onReward() {
        if (viewModel.isRewardAvailable()) {
            findNavController().navigate(R.id.action_homeFragment_to_withdrawFragment)
        } else {
            viewModel.toast(R.string.reward_unavailable, Toast.LENGTH_SHORT)
        }
    }
}
