package com.example.compositiongame.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minAccuracy: Int,
    val gameTimeInSeconds: Int
) : Parcelable