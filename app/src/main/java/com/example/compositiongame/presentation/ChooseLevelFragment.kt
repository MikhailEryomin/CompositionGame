package com.example.compositiongame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.compositiongame.databinding.FragmentChooseLevelBinding
import com.example.compositiongame.domain.entities.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.buttonLevelTest.setOnClickListener { launchLevel(Level.TEST) }
        binding.buttonLevelEasy.setOnClickListener { launchLevel(Level.EASY) }
        binding.buttonLevelNormal.setOnClickListener { launchLevel(Level.NORMAL) }
        binding.buttonLevelHard.setOnClickListener { launchLevel(Level.HARD) }
    }

    private fun launchLevel(level: Level) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}