package com.example.compositiongame.domain.entities

data class GameResult (
    val success: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
)