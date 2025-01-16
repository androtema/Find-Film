package com.temalu.findfilm.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.temalu.findfilm.databinding.FragmentLaterWatchBinding
import com.temalu.findfilm.utils.AnimationHelper

class LaterWatchFragment : Fragment() {

    private lateinit var binding: FragmentLaterWatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaterWatchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.laterRoot,
            requireActivity(),
            4
        )
    }
}