package com.kin.easynotes.pure_unit_test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.kin.easynotes.presentation.components.markdown.buildString
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

/**
 * This is test failed due to wrong approach used in builder part.
 * */
class BuildStringTesting {

    @Test
    fun `checking bold string working`() {
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
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking italic string working`() {
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
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking highlight string working`() {
        // Arrange
        val input = "**This** is ***bold*** ==hi== text.**"
        println("This is bold hi text.")
        val expected = buildAnnotatedString {
            bold("This")
            normal(" ")
            normal("is")
            normal(" ")
            italic_bold("bold")
            normal(" ")
            normal("hi", getHighlightSpanStyle())
            normal(" ")
            normal("text")
            normal(".")
        }

        //Action
        val actual = buildString(input)
        println(actual)

        //Assert
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking strikethrough string working`() {
        // Arrange
        val input = "**This** is ***bold*** ==hi== ~~text.~~"
        println("This is bold hi text.")
        val expected = buildAnnotatedString {
            bold("This")
            normal(" ")
            normal("is")
            normal(" ")
            italic_bold("bold")
            normal(" ")
            normal("hi", getHighlightSpanStyle())
            normal(" ")
            normal("text", getStrikeThroughStyle())
            normal(".", getStrikeThroughStyle())
        }

        //Action
        val actual = buildString(input)
        println(actual)

        //Assert
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking underline string working`() {
        // Arrange
        val input = "_**This**_ is ***bold*** ==hi== ~~text.~~"
        println("This is bold hi text.")
        val expected = buildAnnotatedString {
            bold("This", getUnderLineStyle())
            normal(" ")
            normal("is")
            normal(" ")
            italic_bold("bold")
            normal(" ")
            normal("hi", getHighlightSpanStyle())
            normal(" ")
            normal("text", getStrikeThroughStyle())
            normal(".", getStrikeThroughStyle())
        }

        //Action
        val actual = buildString(input)
        println(actual)

        //Assert
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }

    @Test
    fun `checking codeStyle string working`() {
        // Arrange
        val input = "_**This**_ `is` ***bold*** ==hi== ~~text.~~"
        println("This is bold hi text.")
        val expected = buildAnnotatedString {
            bold("This", getUnderLineStyle())
            normal(" ")
            normal("is" , getCodeStyle())
            normal(" ")
            italic_bold("bold")
            normal(" ")
            normal("hi", getHighlightSpanStyle())
            normal(" ")
            normal("text", getStrikeThroughStyle())
            normal(".", getStrikeThroughStyle())
        }

        //Action
        val actual = buildString(input)
        println(actual)

        //Assert
        assert(expected == actual) {
            println("expected : ${expected.spanStyles}")
            println("actual : ${actual.spanStyles}")
        }
    }
}


fun annotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString {
    return buildAnnotatedString(builder)
}

fun AnnotatedString.Builder.normal(text: String, spanStyle: SpanStyle = SpanStyle()) {
    withStyle(spanStyle.merge(SpanStyle(fontWeight = FontWeight.Normal))) {
        append(text)
    }
}

fun AnnotatedString.Builder.bold(text: String, spanStyle: SpanStyle = SpanStyle()) {
    withStyle(spanStyle.merge(SpanStyle(fontWeight = FontWeight.Bold))) {
        append(text)
    }
}

fun AnnotatedString.Builder.italic(text: String, spanStyle: SpanStyle = SpanStyle()) {
    withStyle(
        spanStyle.merge(
            SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal
            )
        )
    ) {
        append(text)
    }
}

fun AnnotatedString.Builder.italic_bold(text: String) {
    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
        append(text)
    }
}

fun getHighlightSpanStyle() = SpanStyle(background = Color.Yellow.copy(alpha = 0.2f))
fun getStrikeThroughStyle() = SpanStyle(textDecoration = TextDecoration.LineThrough)
fun getUnderLineStyle() = SpanStyle(textDecoration = TextDecoration.Underline)
fun getCodeStyle() = SpanStyle(
    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
    background = Color.LightGray.copy(alpha = 0.3f)
)