package com.example.gamesumnumbers.domain.usecases

import com.example.gamesumnumbers.data.GameRepositoryImpl
import com.example.gamesumnumbers.domain.entity.GameSettings
import com.example.gamesumnumbers.domain.entity.Level


class GetGameSettingsUseCase(
    private val repository: GameRepositoryImpl
    ){

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
}

}