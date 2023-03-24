package ru.movie.quiz.common

import android.text.Html

fun String.parseHtml():String{
    return Html.fromHtml(
        this,
        Html.FROM_HTML_MODE_LEGACY
    ).toString()
}