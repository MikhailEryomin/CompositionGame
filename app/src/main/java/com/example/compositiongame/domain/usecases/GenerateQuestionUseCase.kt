package com.example.compositiongame.domain.usecases

import com.example.compositiongame.domain.entities.Question
import com.example.compositiongame.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {
    /*
    Кейс, который будет обращаться к репозиторию
    для генерации примеров для уровня
     */

    operator fun invoke(maxSum: Int): Question {
        return repository.generateQuestion(maxSum, COUNT_OF_OPTIONS)
    }

    private companion object {

        const val COUNT_OF_OPTIONS = 6

    }

}