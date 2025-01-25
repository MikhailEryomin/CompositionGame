package com.example.compositiongame.domain.entities

import java.io.Serializable

data class GameResult (
    val success: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
): Serializable