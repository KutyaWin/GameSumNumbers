package com.example.gamesumnumbers.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamesumnumbers.R
import com.example.gamesumnumbers.databinding.FragmentGameFinishedBinding
import com.example.gamesumnumbers.domain.entity.GameResult


class GameFinishedFragment : Fragment() {
    private val args by navArgs<GameFinishedFragmentArgs>()



    private var _binding: FragmentGameFinishedBinding?=null
    private val binding : FragmentGameFinishedBinding
        get()= _binding ?: throw RuntimeException("binding not found")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        returnClickListener()
        bindViews()

    }

    private fun bindViews() = with(binding) {

            emojiResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(getString(R.string.required_score),
                args.gameResult.gameSettings.minCountOfRightAnswers )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                args.gameResult.countOfRightAnswers
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                args.gameResult.gameSettings.minPercentOfRightAnswer
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswers()
            )
        }


    private fun getPercentOfRightAnswers() = with(args.gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun getSmileResId(): Int {
        return if(args.gameResult.winner){
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    private fun returnClickListener() {

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
       findNavController().popBackStack()
    }

}