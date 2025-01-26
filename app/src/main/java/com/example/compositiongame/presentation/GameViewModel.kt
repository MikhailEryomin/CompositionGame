package com.example.compositiongame.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compositiongame.data.GameRepositoryImpl
import com.example.compositiongame.domain.entities.GameResult
import com.example.compositiongame.domain.entities.GameSettings
import com.example.compositiongame.domain.entities.Level
import com.example.compositiongame.domain.entities.Question
import com.example.compositiongame.domain.usecases.GenerateQuestionUseCase
import com.example.compositiongame.domain.usecases.GetGameSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {

    private val repository = GameRepositoryImpl

    private val _gameSettings = MutableLiveData<GameSettings>()
    val gameSettings: LiveData<GameSettings>
        get() = _gameSettings

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _countOfRightAnswers: MutableLiveData<Int> = MutableLiveData()
    val countOfRightAnswers: LiveData<Int>
        get() = _countOfRightAnswers

    private val _progress: MutableLiveData<Int> = MutableLiveData()
    val progress: LiveData<Int>
        get() = _progress

    private val _amountOfTime = MutableLiveData<Int>()
    val amountOfTime: LiveData<Int>
        get() = _amountOfTime

    private val _gameIsFinished = MutableLiveData<Unit>()
    val gameIsFinished: LiveData<Unit>
        get() = _gameIsFinished

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var timerJob: Job? = null
    private var countOfAnswers = 0

    private fun getGameSettings(level: Level) {
        _gameSettings.value = GetGameSettingsUseCase(repository)(level)
        _amountOfTime.value = _gameSettings.value?.gameTimeInSeconds
    }

    private fun generateQuestion() {
        _question.value =
            _gameSettings.value?.let { GenerateQuestionUseCase(repository)(it.maxSumValue) }
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(1000) // Задержка 1 секунда

                val currentTime = _amountOfTime.value ?: 0
                val updatedTime = currentTime - 1

                //doing in main-thread
                //liveData can be edited only in main thread
                withContext(Dispatchers.Main) {
                    _amountOfTime.value = updatedTime
                    if (updatedTime == 0) {
                        stopGame()
                        finishGame()
                    }
                }
            }
        }
    }

    fun startGame(level: Level) {
        getGameSettings(level)
        generateQuestion()
        startTimer()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            gameSuccessOfFailure(),
            _countOfRightAnswers.value ?: 0,
            _progress.value ?: 0,
            _gameSettings.value!!
        )
        _gameIsFinished.value = Unit //event
        stopGame()
    }

    private fun gameSuccessOfFailure(): Boolean {
        val progress = _progress.value ?: 0
        val minAccuracy = _gameSettings.value?.minAccuracy ?: 0
        val rightAnswers = _countOfRightAnswers.value ?: 0
        val minRightAnswers = _gameSettings.value?.minCountOfRightAnswers ?: 0

        return progress >= minAccuracy && rightAnswers >= minRightAnswers
    }

    fun stopGame() {
        timerJob?.cancel()
    }

    fun checkForRightAnswer(answer: Int) {
        _question.value?.let {
            val sum = it.sum
            val visibleNumber = it.visibleNumber
            val expected = sum - visibleNumber
            if (answer == expected) {
                _countOfRightAnswers.value = (_countOfRightAnswers.value ?: 0) + 1
            }
            countOfAnswers++
            getProgress()
            generateQuestion()
        }
    }

    private fun getProgress() {
        val progress = if (countOfAnswers == 0) 0
        else ((_countOfRightAnswers.value
            ?: 0).toDouble() / countOfAnswers.toDouble() * 100).toInt()

        _progress.value = progress
    }

    companion object {

        fun getFormattedTimeString(s: Int): String {
            val seconds = s % 60
            val minutes = (s / 60) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    }


}