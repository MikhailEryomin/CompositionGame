package com.example.compositiongame.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val success: Boolean,
    val countOfRightAnswers: Int,
    val progress: Int,
    val gameSettings: GameSettings
) : Parcelable