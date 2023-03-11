package com.example.gamesumnumbers.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamesumnumbers.R
import com.example.gamesumnumbers.databinding.FragmentGameBinding
import com.example.gamesumnumbers.domain.entity.GameResult
import com.example.gamesumnumbers.domain.entity.GameSettings
import com.example.gamesumnumbers.domain.entity.Level


class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val  viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

     private val viewModelFactory by lazy{
         GameViewModelFactory(args.level, requireActivity().application)
     }


    private var _binding: FragmentGameBinding?=null
    private val binding : FragmentGameBinding
        get()= _binding ?: throw RuntimeException("binding not found")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeViewModel()


    }



    private fun observeViewModel() {


        viewModel.gameResult.observe(viewLifecycleOwner){
            launchGameFinishedFragment(it)
        }

    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

companion object {
    fun newInstance(level: Level): GameFragment {
        return GameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_LEVEL, level)
            }
        }
    }
     const val ARG_LEVEL = "level"
     const val NAME ="GameFragment"
}

    private fun launchGameFinishedFragment(gameResult: GameResult){

        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult))
    }

}