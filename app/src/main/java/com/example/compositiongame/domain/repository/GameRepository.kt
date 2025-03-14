package com.example.compositiongame.domain.repository

import com.example.compositiongame.domain.entities.GameSettings
import com.example.compositiongame.domain.entities.Level
import com.example.compositiongame.domain.entities.Question

interface GameRepository {

    fun getGameSettings(level: Level): GameSettings

    fun generateQuestion(
        maxSum: Int,
        countOfOptions: Int
    ): Question

}