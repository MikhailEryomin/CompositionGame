package com.example.compositiongame.domain.entities

data class GameSettings (
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minAccuracy: Int,
    val gameTimeInSeconds: Int
)