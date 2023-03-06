package com.example.gamesumnumbers.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.gamesumnumbers.R
import com.example.gamesumnumbers.databinding.FragmentGameFinishedBinding
import com.example.gamesumnumbers.domain.entity.GameResult


class GameFinishedFragment : Fragment() {
private lateinit var gameResult: GameResult
    private var _binding: FragmentGameFinishedBinding?=null
    private val binding : FragmentGameFinishedBinding
        get()= _binding ?: throw RuntimeException("binding not found")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun parseArgs() {
    requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
        gameResult = it
    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        returnClickListener()
        bindViews()

    }

    private fun bindViews() = with(binding) {

            emojiResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(getString(R.string.required_score),
                gameResult.gameSettings.minCountOfRightAnswers )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                gameResult.countOfRightAnswers
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswer
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswers()
            )
        }


    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun getSmileResId(): Int {
        return if(gameResult.winner){
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    private fun returnClickListener() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    companion object{
        fun newInstance(gameResult: GameResult): GameFinishedFragment{
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
        const val KEY_GAME_RESULT = "game_result"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager
            .popBackStack(GameFragment.NAME,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}