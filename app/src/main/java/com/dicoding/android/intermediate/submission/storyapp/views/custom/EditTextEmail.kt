package com.dicoding.android.intermediate.submission.storyapp.views.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import com.dicoding.android.intermediate.submission.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class EditTextEmail : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            /**
             * Reference:
             * https://stackoverflow.com/questions/36040154/
             * email-validation-on-edittext-android
             */
            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (!TextUtils.isEmpty(s) &&
                    !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                    setError(context.getString(R.string.edit_text_email_error_msg), null)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing.
            }
        })
    }
}