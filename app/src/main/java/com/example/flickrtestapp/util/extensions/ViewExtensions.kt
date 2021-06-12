package com.example.flickrtestapp.util.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addOnTextChangedListener(onTextChanged: (String) -> Unit): TextWatcher {

    val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(text.toString())
        }

        override fun afterTextChanged(editable: Editable?) {}
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}