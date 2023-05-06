package com.github.deviants.wisewallet.presentation.secret

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.github.deviants.wisewallet.databinding.FragmentSecretBinding

class SecretFragment : Fragment() {

    private var _binding: FragmentSecretBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSecretBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO(Make secret fragment with Harry Potter quotes(faker library))
    }
}