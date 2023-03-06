package com.example.gamesumnumbers.domain.usecases

import com.example.gamesumnumbers.data.GameRepositoryImpl
import com.example.gamesumnumbers.domain.entity.Question

class GenerateQuestionUseCase(
    private val repository: GameRepositoryImpl
){

    operator fun invoke(maxSumValue: Int): Question{
    return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}