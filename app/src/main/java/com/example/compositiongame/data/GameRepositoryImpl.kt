package com.example.compositiongame.data

import com.example.compositiongame.domain.entities.GameSettings
import com.example.compositiongame.domain.entities.Level
import com.example.compositiongame.domain.entities.Question
import com.example.compositiongame.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min

object GameRepositoryImpl: GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun getGameSettings(level: Level): GameSettings {
        return when(level) {
            Level.TEST -> GameSettings(
                10,
                3,
                50,
                8
            )
            Level.EASY -> GameSettings(
                10,
                10,
                70,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                80,
                40
            )
            Level.HARD -> GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }

    override fun generateQuestion(maxSum: Int, countOfOptions: Int): Question {
        if (maxSum <= 0) throw RuntimeException("maxSum cannot be <=0")

        val sum = (MIN_SUM_VALUE..maxSum).random()
        val visibleNumber = (MIN_ANSWER_VALUE..sum).random()
        val options = HashSet<Int>()

        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)

        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(maxSum, rightAnswer + countOfOptions)

        while (options.size < countOfOptions) {
            val randomNumber = (from until to).random()
            options.add(randomNumber)
        }


        return Question(
            sum,
            visibleNumber,
            options.toList()
        )
    }


}