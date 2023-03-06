package com.example.gamesumnumbers.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamesumnumbers.R
import com.example.gamesumnumbers.data.GameRepositoryImpl
import com.example.gamesumnumbers.domain.entity.GameResult
import com.example.gamesumnumbers.domain.entity.GameSettings
import com.example.gamesumnumbers.domain.entity.Level
import com.example.gamesumnumbers.domain.entity.Question
import com.example.gamesumnumbers.domain.usecases.GenerateQuestionUseCase
import com.example.gamesumnumbers.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application): AndroidViewModel(application) {

    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    private val context = application
    private var timer: CountDownTimer? = null
    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
    get()=_formattedTime

    private val _enoughCountRightOfAnswers = MutableLiveData<Boolean>()
    val enoughCountRightOfAnswers: LiveData<Boolean>
        get()= _enoughCountRightOfAnswers

    private val _enoughPercentRightOfAnswers = MutableLiveData<Boolean>()
    val enoughPercentRightOfAnswers: LiveData<Boolean>
        get()= _enoughPercentRightOfAnswers

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get()= _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get()= _progressAnswers

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
    get()= _question

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get()= _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get()= _gameResult

    fun startGame(level: Level){
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
}

    private fun getGameSettings(level: Level){
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswer
    }



    private fun startTimer(){
         timer = object: CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ){
            override fun onTick(millisUntilFinished: Long) {
               _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    companion object{
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }

    private fun finishGame() {
         _gameResult.value = GameResult(
            enoughCountRightOfAnswers.value == true
                    && enoughPercentRightOfAnswers.value == true,
            countOfRightAnswers,
            countOfQuestion,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun formatTime(millisUntilFinished: Long): String{
        val seconds = millisUntilFinished / MILLIS_IN_SECOND
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds )
    }

    private fun generateQuestion(){
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

     fun chooseAnswer(number: Int){
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

     private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
            countOfQuestion++
    }

    private fun updateProgress() {
    val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCountRightOfAnswers.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentRightOfAnswers.value = percent >= gameSettings.minPercentOfRightAnswer
    }

    private fun calculatePercentOfRightAnswers(): Int{
        if(countOfQuestion == 0) return 0
        return ((countOfRightAnswers / countOfQuestion.toFloat()) * 100).toInt()
    }

}