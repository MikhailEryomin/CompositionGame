package com.example.compositiongame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.compositiongame.R
import com.example.compositiongame.databinding.FragmentGameFinishedBinding

class GameFinishFragment : Fragment() {

    private val args by navArgs<GameFinishFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.buttonRetry.setOnClickListener {
            launchChooseLevelFragment()
        }

        val emojiImgResId = if (args.gameResult.success) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
        binding.emojiResult.setImageResource(emojiImgResId)

        binding.tvRequiredAnswers.text = getString(
            R.string.required_score,
            args.gameResult.gameSettings.minCountOfRightAnswers.toString()
        )
        binding.tvScoreAnswers.text =
            getString(R.string.score_answers, args.gameResult.countOfRightAnswers.toString())
        binding.tvRequiredPercentage.text = getString(
            R.string.required_percentage,
            args.gameResult.gameSettings.minAccuracy.toString()
        )

        val percent = with(args.gameResult) {
            if (countOfRightAnswers == 0) 0
            else {
                ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
            }
        }
        binding.tvScorePercentage.text = getString(R.string.score_percentage, percent.toString())
    }

    private fun launchChooseLevelFragment() {
        findNavController().popBackStack()
    }

    private fun retryGame() {
        //returning to chooseLevelFragment in the stack, skipping all game fragments
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}