package com.example.sixth.uikit


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.sixth.R
import com.example.sixth.databinding.LayoutSixtToolbarBinding

class SixtToolbar @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleArr: Int = 0
) : FrameLayout(context, attributes, defStyleArr) {
    private val binding by lazy {
        LayoutSixtToolbarBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }


    fun show(
        title: CharSequence,
        showBack: (() -> Unit)? = null,
        showMenu: (() -> Unit)? = null,
    ) {
        binding.apply {
            textViewTitle.text = title

            if (showBack != null) {
                imageViewBack.visibility = View.VISIBLE
                imageViewBack.setOnClickListener { showBack() }
            }
            if (showMenu != null) {
                imageViewMenu.visibility = View.VISIBLE
                imageViewMenu.setOnClickListener { showMenu() }
            }
        }
    }

}