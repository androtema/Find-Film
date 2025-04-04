package com.temalu.findfilm.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.temalu.findfilm.databinding.FragmentDifferentFilmsBinding
import com.temalu.findfilm.presentation.utils.AnimationHelper


class DifferentFilmsFragment : Fragment() {

    private lateinit var binding: FragmentDifferentFilmsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDifferentFilmsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.differentRoot,
            requireActivity(),
            3
        )
    }
}