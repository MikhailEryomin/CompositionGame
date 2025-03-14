package com.example.compositiongame.domain.usecases

import com.example.compositiongame.domain.entities.GameSettings
import com.example.compositiongame.domain.entities.Level
import com.example.compositiongame.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    /*
    Кейс, который будет заниматься
    получением необходимых параметров
    для уровня
     */

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }


}