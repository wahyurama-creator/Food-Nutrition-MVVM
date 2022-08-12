package com.way.foodnutrition.presentation.ui.detail.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.way.foodnutrition.R
import com.way.foodnutrition.data.remote.model.Result
import com.way.foodnutrition.databinding.FragmentInstructionBinding
import com.way.foodnutrition.presentation.ui.detail.DetailActivity

class InstructionFragment : Fragment() {

    private lateinit var binding: FragmentInstructionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argsBundle = arguments?.getParcelable<Result>(DetailActivity.RECIPES_BUNDLE) as Result
        setupWebView(argsBundle.sourceUrl)
    }

    private fun setupWebView(url: String) {
        binding.webViewInstruction.apply {
            webViewClient = object : WebViewClient() {}
            loadUrl(url)
            settings.javaScriptEnabled = true
        }
    }
}