package com.example.compositiongame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.compositiongame.R
import com.example.compositiongame.databinding.FragmentGameFinishedBinding
import com.example.compositiongame.domain.entities.GameResult
import com.example.compositiongame.domain.entities.GameSettings

class GameFinishFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding = null")

    private var _gameResult: GameResult? = null
    private val gameResult: GameResult
        get() = _gameResult ?: throw RuntimeException("gameResult = null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        _gameResult = requireArguments().getParcelable(KEY_GAME_RESULT)
    }

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
        setupBackButton()
    }

    private fun setupViews() {
        val success = gameResult.success
        val gameSettings = gameResult.gameSettings
        binding.emojiResult.setImageResource(if (success) R.drawable.ic_smile else R.drawable.ic_sad)
        binding.tvRequiredAnswers.text = getString(R.string.required_score, gameSettings.minCountOfRightAnswers.toString())
        binding.tvScoreAnswers.text = getString(R.string.score_answers, gameResult.countOfRightAnswers.toString())
        binding.tvRequiredPercentage.text = getString(R.string.required_percentage, gameSettings.minAccuracy.toString())
        binding.tvScorePercentage.text = getString(R.string.score_percentage, gameResult.progress.toString())
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        })
    }

    private fun retryGame() {
        //returning to chooseLevelFragment in the stack, skipping all game fragments
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishFragment {
            return GameFinishFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }

}