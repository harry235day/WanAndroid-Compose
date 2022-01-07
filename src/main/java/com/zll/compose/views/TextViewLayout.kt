package com.zll.compose.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.zll.compose.R

class TextViewLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private lateinit var tv:TextView
    lateinit var btn:Button
    init {
        View.inflate(context, R.layout.android_view1, this)
        tv = findViewById(R.id.tv)
        btn = findViewById(R.id.btn)
    }

    fun setTextView(int: Int){
        tv.text = "Compose中设置了$int"
    }
}

