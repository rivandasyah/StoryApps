package com.rivaphy.dicoding.submissionstory.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.rivaphy.dicoding.submissionstory.R

class EmailEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = if (!isTrue(p0.toString())) {
                    context.getString(R.string.erro_email)
                } else {
                    null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun isTrue(text: String): Boolean {
        return text.contains(context.getString(R.string.true_email))
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}