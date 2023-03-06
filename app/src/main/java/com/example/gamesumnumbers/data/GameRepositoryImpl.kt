package com.example.gamesumnumbers.data

import com.example.gamesumnumbers.domain.entity.GameSettings
import com.example.gamesumnumbers.domain.entity.Level
import com.example.gamesumnumbers.domain.entity.Question
import com.example.gamesumnumbers.domain.repository.GameRepository
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {

    private const  val MIN_SUM_VALUE = 2
    private const  val  MIN_VALUE = 1


    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
      val sum = Random.nextInt(MIN_SUM_VALUE,maxSumValue+1)
        val visibleNumber = Random.nextInt(MIN_VALUE,sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max (rightAnswer - countOfOptions, MIN_VALUE)
        val to = min(rightAnswer + countOfOptions, maxSumValue-1)
            while (options.size < countOfOptions){
                options.add(Random.nextInt(from,to))
            }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST->{
                GameSettings(
                    15,
                    4,
                    50,
                    8
                )
            }
            Level.EASY->{
                GameSettings(
                    10,
                    5,
                    60,
                    60
                )
            }
            Level.NORMAl -> {
                GameSettings(
                    50,
                    10,
                    60,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    100,
                    15,
                    70,
                    40
                )

            }
        }
    }





    
}