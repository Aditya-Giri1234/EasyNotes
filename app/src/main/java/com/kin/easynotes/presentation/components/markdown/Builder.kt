package com.kin.easynotes.presentation.components.markdown

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import java.text.BreakIterator
import androidx.compose.ui.text.withStyle
import com.aditya.lint_rules.annotation.Buggy
import com.aditya.lint_rules.annotation.NeedsImprovement
import com.aditya.lint_rules.annotation.WrongApproach

/**
 * This is class used iterator pattern. Like checking by hasNextLine() , nextLine() etc.
 * */
class MarkdownBuilder(internal val lines: List<String>, private var lineProcessors: List<MarkdownLineProcessor>) {
    var lineIndex = -1

    internal val content = mutableListOf<MarkdownElement>()

    fun add(element: MarkdownElement) {
        content.add(element)
    }

    fun parse() {
        while (hasNextLine()) {
            val line = nextLine()
            val processor = lineProcessors.find { it.canProcessLine(line) }
            if (processor != null) {
                processor.processLine(line, this)
            } else {
                add(NormalText(line))
            }
        }
    }

    private fun hasNextLine(): Boolean = lineIndex + 1 < lines.size

    private fun nextLine(): String {
        lineIndex++
        return lines[lineIndex]
    }
}

/**
 * Splits the input string by the specified delimiter and returns a list of index pairs.
 * Each pair represents the start and end indices of segments between delimiters.
 */
@WrongApproach
fun splitByDelimiter(input: String, delimiter: String): List<Pair<Int, Int>> {
    //Segment is nothing but a simple subString
    //Here we do nothing but split input string with delimiter and
    // here filter logic applied because there every odd place we have substring which is not delimiter like
    // if delimiter is *** and input string ***hi ***ok ***no
    // so here is string which is need is - { "hi " , "ok " , "no" }
    // when we make complete list which is  : - {"***" , "hi " , "****" ,"ok " ,"***","no"}
    // You can see every even position have delimiter.
    //"This is **bold** text."
    //"**This is **bold** text."
    //"**This** is **bold** text."
    //"This is bold text."
    val segments = mutableListOf<Pair<Int, Int>>()
    var startIndex = 0
    var delimiterIndex = input.indexOf(delimiter, startIndex)

    while (delimiterIndex != -1) {
        if (startIndex != delimiterIndex) {
            segments.add(Pair(startIndex, delimiterIndex))
            //By Adding this we make final list hold even position delimiter and odd position our substring
//            segments.add(Pair(delimiterIndex, delimiterIndex))
        } else {
            segments.add(Pair(startIndex, startIndex))
        }
        startIndex = delimiterIndex + delimiter.length
        delimiterIndex = input.indexOf(delimiter, startIndex)
    }

    if (startIndex < input.length) {
        segments.add(Pair(startIndex, input.length))
    } else if (startIndex == input.length) {
        segments.add(Pair(startIndex, startIndex))
    }

    // Only keep segments that are odd-indexed (i.e., inside delimiters)
    return segments.filterIndexed { index, _ -> index % 2 == 1 }
}

@NeedsImprovement
fun splitByDelimiterNew(input:String , delimiter: String) : List<Pair<Int,Int>>{
    val segments = mutableListOf<Pair<Int, Int>>()
    var startIndex = 0
    var delimiterIndex = input.indexOf(delimiter, startIndex)
    while(delimiterIndex != -1){
        var segmentIndex = delimiterIndex + delimiter.length
        when{
            (segmentIndex + delimiter.length) < input.length -> {
                if(input.substring(segmentIndex , segmentIndex + delimiter.length)==delimiter){
                    startIndex = input.indexOf(" " , segmentIndex + delimiter.length)
                    if(startIndex != -1){
                        delimiterIndex = input.indexOf(delimiter , startIndex)
                        continue
                    }else{
                        break
                    }
                }
            }
            else ->{
                //**This is ***bold*** *hi* text.**
                // This will handle if italic have * delimiter and it point to second last * and handle it which is not handle above part.
                while(segmentIndex < input.length){
                    if(input[segmentIndex] !in delimiter.toCharArray()){
                        break
                    }
                    segmentIndex++
                }
                if(segmentIndex == input.length){
                    break
                }
            }
        }

        val nextDelimiterIndex = input.indexOf(delimiter, segmentIndex)
        if(nextDelimiterIndex!=-1){
            if(input[nextDelimiterIndex-1] == ' '){
                startIndex = nextDelimiterIndex
                delimiterIndex = nextDelimiterIndex
                continue
            }else {
                segments.add(Pair(segmentIndex,nextDelimiterIndex))
                startIndex = nextDelimiterIndex + delimiter.length
                delimiterIndex = input.indexOf(delimiter, startIndex)
                continue
            }
        }else{
            break
        }
    }
    return segments
}

/**
 * Checks if a given index is within any of the provided segments.
 */
fun isInSegments(index: Int, segments: List<Pair<Int, Int>>): Boolean {
    return segments.any { segment -> index in segment.first until segment.second }
}

fun buildString(input: String, defaultFontWeight: FontWeight = FontWeight.Normal): AnnotatedString {
    val textStyleSegments: List<TextStyleSegment> = listOf(
        BoldSegment(),
        ItalicSegment(),
        ItalicBoldSegment(),
        HighlightSegment(),
        Strikethrough(),
        Underline(),
        CodeSegment()
    )

    //Here we make a map like below :
    // TextStyleSegment -> List<Int,Int>(which contain substring strat and end index  which split by delimiter)
    val allSegments = textStyleSegments.associateWith { splitByDelimiterNew(input, it.delimiter) }
    println("allSegments : $allSegments")

    @WrongApproach
    fun getSpanStyle(index: Int): SpanStyle {
        val styles = textStyleSegments.filter { segment -> isInSegments(index, allSegments[segment]!!) }
        return styles.fold(SpanStyle(fontWeight = defaultFontWeight)) { acc, segment -> acc.merge(segment.getSpanStyle()) }
    }

    val annotatedString = buildAnnotatedString {
        val iterator = BreakIterator.getWordInstance()
        iterator.setText(input)
        var start = iterator.first()
        var end = iterator.next()

        while (end != BreakIterator.DONE) {
            val substring = input.substring(start, end)

            // Skip delimiters and check Arabic substrings
            val isDelimiter = textStyleSegments.any { segment -> 
                // Only consider it a delimiter if it's the exact delimiter string
                // This ensures single characters like +, -, = are not filtered out
                segment.delimiter == substring
            }
            
            if (!isDelimiter) {
                withStyle(style = getSpanStyle(start)) {
                    append(substring) // Append full words or graphemes
                }
            }

            start = end
            end = iterator.next()
        }
    }
    return annotatedString
}
