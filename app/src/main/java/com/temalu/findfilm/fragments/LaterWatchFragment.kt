package com.temalu.findfilm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.temalu.findfilm.AnimationHelper
import com.temalu.findfilm.R
import com.temalu.findfilm.databinding.FragmentDetailsBinding
import com.temalu.findfilm.databinding.FragmentFavoritesBinding
import com.temalu.findfilm.databinding.FragmentLaterWatchBinding

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