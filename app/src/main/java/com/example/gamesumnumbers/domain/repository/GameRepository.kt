package com.example.gamesumnumbers.domain.repository

import com.example.gamesumnumbers.domain.entity.GameSettings
import com.example.gamesumnumbers.domain.entity.Level
import com.example.gamesumnumbers.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}