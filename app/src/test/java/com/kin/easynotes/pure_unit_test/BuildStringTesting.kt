package com.kin.easynotes.pure_unit_test

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.kin.easynotes.presentation.components.markdown.buildString
import org.junit.Test

/**
 * This is test failed due to wrong approach used in builder part.
 * */
class BuildStringTesting {

    @Test
    fun `checking bold string working`(){
        // Arrange
        val input = "This is **bold** text."
        val expected = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append("This")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(" ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append("is")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(" ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("bold")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(" ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append("text")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(".")
            }
        }

        //Action
        val actual = buildString(input)

        //Assert
        assert(expected == actual){
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking italic string working`(){
        // Arrange
        val input = "**This is ***bold*** *hi* text.**"
        val expected = buildAnnotatedString {
            bold("This")
            bold(" ")
            bold("is")
            bold(" ")
            bold("bold")
            bold(" ")
            bold("hi")
            bold(" ")
            bold("text")
            bold(".")
        }

        //Action
        val actual = buildString(input)

        //Assert
        assert(expected == actual){
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }
}


fun annotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString {
    return buildAnnotatedString(builder)
}

fun AnnotatedString.Builder.normal(text: String) {
    withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
        append(text)
    }
}

fun AnnotatedString.Builder.bold(text: String) {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append(text)
    }
}

fun AnnotatedString.Builder.italic(text: String) {
    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal)) {
        append(text)
    }
}
fun AnnotatedString.Builder.italic_bold(text: String) {
    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
        append(text)
    }
}