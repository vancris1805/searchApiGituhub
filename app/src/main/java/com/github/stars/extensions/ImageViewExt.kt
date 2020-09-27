package com.github.stars.extensions

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.stars.R
import com.github.stars.application.GlideRequest
import com.github.stars.application.GlideRequests
import kotlin.math.roundToInt

private const val THUMBNAIL_SIZE = 0.20F
private const val IMAGE_QUALITY = 0.9
private const val IMAGE_CLEAN_PARAMS = "%1s?impolicy=resize&amp;vwidth=%2s"
private const val BIG_SIZE = 0.8
private const val NORMAL_MAX_SIZE = 0.5
private const val NORMAL_MIN_SIZE = 0.2

fun ImageView.inflateImageUrl(
    url: String?,
    glide: GlideRequests,
    withPlaceHolder: Boolean = true,
    withTransition: Boolean = true
) {
    try {
        if (url.isNullOrEmpty()) {
            if (withPlaceHolder) setImageResource(R.drawable.ic_launcher_background)
        } else {
            addOnPreDrawListener {
                getBasicGlideCall(url, glide, true)
                    .preview(true)
                    .placeholder(withPlaceHolder)
                    .differType(measuredHeight >= measuredWidth)
                    .priority(getPriority())
                    .transition(withTransition)
                    .into(this)
            }
        }
    } catch (e: OutOfMemoryError) {
       Log.ERROR
    }
}

private fun GlideRequest<Drawable>.transition(withTransition: Boolean): GlideRequest<Drawable> {
    return if (withTransition) {
        this.transition(DrawableTransitionOptions.withCrossFade())
    } else {
        this
    }
}

private fun <TranscodeType> GlideRequest<TranscodeType>.differType(isPortrait: Boolean): GlideRequest<TranscodeType> {
    return if (isPortrait) {
        this.fitCenter()
    } else {
        this.centerInside()
    }
}

fun <TranscodeType> GlideRequest<TranscodeType>.placeholder(withPlaceHolder: Boolean): GlideRequest<TranscodeType> {
    return if (withPlaceHolder) {
        this.placeholder(R.drawable.ic_launcher_background)
    } else {
        this.placeholder(null)
    }
}

fun ImageView.getPriority(): Priority {
    val sizePercentage = measuredWidth.toFloat().div(context.windowWidth.toFloat())

    return when {
        sizePercentage >= BIG_SIZE -> Priority.IMMEDIATE
        sizePercentage >= NORMAL_MAX_SIZE -> Priority.HIGH
        NORMAL_MAX_SIZE > sizePercentage && sizePercentage > NORMAL_MIN_SIZE -> Priority.NORMAL
        else -> Priority.LOW
    }
}

private fun <TranscodeType> GlideRequest<TranscodeType>.preview(hasPreview: Boolean): GlideRequest<TranscodeType> {
    return if (hasPreview) {
        this.thumbnail(THUMBNAIL_SIZE)
    } else {
        this
    }
}

inline fun ImageView.addOnPreDrawListener(crossinline callback: () -> Unit) {
    this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            callback.invoke()
            return true
        }
    })
}

fun getFormattedUrl(url: String, vWidth: Int): String {
    return String.format(
        IMAGE_CLEAN_PARAMS,
        url,
        vWidth.times(IMAGE_QUALITY).roundToInt().toString()
    )
}

fun ImageView.getBasicGlideCall(
    url: String,
    glide: GlideRequests,
    cleanImage: Boolean
): GlideRequest<Drawable> {
    val imageUrl = if (cleanImage) getFormattedUrl(url, measuredWidth) else url
    return glide
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .skipMemoryCache(true)
        .override(
            measuredWidth,
            measuredHeight
        )
}