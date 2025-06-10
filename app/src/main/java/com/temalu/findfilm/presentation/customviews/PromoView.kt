package com.temalu.findfilm.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.androtema.remote.data.tmdb.API_TMDB
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.temalu.findfilm.databinding.MergePromoBinding

class PromoView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val watchButton = binding.watchButton

    fun setLinkForPoster(link: String) {
        Log.d("promoView", "setLinkForPoster")
        Glide.with(binding.root)
            .load(API_TMDB.IMAGES_URL + "w500" + link)
            .apply(RequestOptions()
                .transform(FitCenter(), RoundedCorners(55)))
            .into(binding.poster)
    }
}