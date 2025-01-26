package com.example.compositiongame.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.compositiongame.R
import com.example.compositiongame.databinding.FragmentGameBinding
import com.example.compositiongame.domain.entities.GameResult
import com.example.compositiongame.domain.entities.GameSettings
import com.example.compositiongame.domain.entities.Level
import com.example.compositiongame.domain.entities.Question

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var level: Level
    private lateinit var viewModel: GameViewModel

    private lateinit var gameSettings: GameSettings
    private lateinit var question: Question
    private lateinit var gameResult: GameResult
    private var countOfRightAnswers = 0
    private var progress = 0
    private var amountOfTimeInSeconds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        startGame()
        observeViewModel()
    }

    private fun startGame() {
        viewModel.startGame(level)
    }

    private fun setupQuestionViews() {

        with(binding) {
            tvSum.text = question.sum.toString()
            tvLeftNumber.text = question.visibleNumber.toString()
            tvOption1.text = question.options[0].toString()
            tvOption2.text = question.options[1].toString()
            tvOption3.text = question.options[2].toString()
            tvOption4.text = question.options[3].toString()
            tvOption5.text = question.options[4].toString()
            tvOption6.text = question.options[5].toString()
        }

        setupOptionsButtons()
    }

    private fun setupOptionsButtons() {
        with(binding) {
            tvOption1.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption1.text.toString().toInt())
            }
            tvOption2.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption2.text.toString().toInt())
            }
            tvOption3.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption3.text.toString().toInt())
            }
            tvOption4.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption4.text.toString().toInt())
            }
            tvOption5.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption5.text.toString().toInt())
            }
            tvOption6.setOnClickListener {
                viewModel.checkForRightAnswer(tvOption6.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.gameSettings.observe(viewLifecycleOwner) {
            gameSettings = it
        }
        viewModel.question.observe(viewLifecycleOwner) {
            question = it
            setupQuestionViews()
        }
        viewModel.amountOfTime.observe(viewLifecycleOwner) {
            amountOfTimeInSeconds = it
            binding.tvTimer.text = GameViewModel.getFormattedTimeString(amountOfTimeInSeconds)
        }
        viewModel.countOfRightAnswers.observe(viewLifecycleOwner) {
            countOfRightAnswers = it
            binding.tvAnswersProgress.text =
                getString(
                    R.string.progress_answers, countOfRightAnswers.toString(),
                    gameSettings.minCountOfRightAnswers.toString()
                )
        }
        viewModel.progress.observe(viewLifecycleOwner) {
            progress = it
            binding.progressBar.progress = progress
        }
        viewModel.gameIsFinished.observe(viewLifecycleOwner) {
            launchGameResultFragment()
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            gameResult = it
        }

    }


    private fun launchGameResultFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_container,
                GameFinishFragment.newInstance(gameResult)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopGame()
        _binding = null
    }

    private fun parseArguments() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "key_level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }

    }

}