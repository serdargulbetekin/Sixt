package com.example.sixth.uikit


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.sixth.R
import com.example.sixth.databinding.LayoutSixtStateBinding
import com.example.sixth.util.setGone
import com.example.sixth.util.setVisible

class SixtStateLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleArr: Int = 0
) : FrameLayout(context, attributes, defStyleArr) {
    private val binding by lazy {
        LayoutSixtStateBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun showProgress() {
        binding.apply {
            frameLayoutProgressContainer.setVisible()
            linearLayoutMessageContainer.setGone()
            frameLayoutProgressOverContentContainer.setGone()
        }
    }
    fun hideProgress() {
        binding.apply {
            frameLayoutProgressContainer.setGone()
            linearLayoutMessageContainer.setGone()
            frameLayoutProgressOverContentContainer.setGone()
        }
    }

    fun showProgressOverContent() {
        binding.apply {
            frameLayoutProgressOverContentContainer.setVisible()
            linearLayoutMessageContainer.setGone()
            frameLayoutProgressContainer.setGone()
        }
    }

    fun showMessage(
        title: CharSequence?,
        message: CharSequence?,
        iconResId: Int = R.drawable.icon_warning,
        negativeText: CharSequence?,
        onNegativeClick: (() -> Unit)? = null,
        positiveText: CharSequence?,
        onPositiveClick: (() -> Unit)? = null
    ) {
        binding.apply {
            linearLayoutMessageContainer.setVisible()
            textViewTitle.setVisible()
            textViewMessage.setVisible()
            imageViewMessage.setVisible()
            textViewTitle.text = title
            textViewMessage.text = message
            imageViewMessage.setImageResource(iconResId)
            negativeText?.let {
                linearLayoutButtonContainer.setVisible()
                buttonNegative.text = it
                buttonNegative.setOnClickListener {
                    onNegativeClick?.invoke()
                }
            }
            positiveText?.let {
                linearLayoutButtonContainer.setVisible()
                buttonPositive.text = it
                buttonPositive.setOnClickListener {
                    onPositiveClick?.invoke()
                }
            }

        }

    }

}