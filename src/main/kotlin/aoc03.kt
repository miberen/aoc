package main.kotlin

import java.io.File

fun main(args: Array<String>) {
    println(getPart31())
    println(getPart32())
}

private fun getPart31() : Int {
    val input = File("input03.txt").readLines()
    val numberRegex = Regex("\\d+")
    val specialSignRegex = Regex("[^A-Za-z0-9.\\n]")
    var sum = 0

    var lastString = ""
    input.forEachIndexed { index, s ->
        val numbers = numberRegex.findAll(s).toList()

        numbers.forEach {
            var searchString = ""
            if(lastString.isNotEmpty()) {
                searchString += getSearchString(it.range, lastString)
            }
            searchString += getSearchString(it.range, s)

            if(index < input.count()-1) {
                searchString += getSearchString(it.range, input[index+1])
            }
            if(specialSignRegex.find(searchString) != null){
                sum += it.value.toInt()
            }
        }
        lastString = s
    }
    return sum
}

private fun getPart32(): Int {
    val input = File("input03.txt").readLines()
    val numberRegex = Regex("\\d+")
    val gearRegex = Regex("\\*")
    var sum = 0

    var lastString = ""
    input.forEachIndexed { index, s ->
        val gears = gearRegex.findAll(s).toList()

        gears.forEach {
            var searchStrings = mutableListOf<String>()
            val numbers = mutableListOf<Int>()

            if(lastString.isNotEmpty())
                searchStrings.add(getSearchString(it.range, lastString, true))

            searchStrings.add(getSearchString(it.range, s, true))

            if(index < input.count()-1)
                searchStrings.add(getSearchString(it.range, input[index+1], true))

            searchStrings.forEach {searchString ->
                numbers.addAll(numberRegex.findAll(searchString).map { num -> num.value.toInt() })
            }

            if(numbers.count() == 2){
                sum += numbers.first() * numbers.last()
            }
        }
        lastString = s
    }
    return sum
}

private fun getSearchString(range : IntRange, input : String, expand: Boolean = false) : String {
    var searchRange = IntRange(0.coerceAtLeast(range.first-1), (input.length-1).coerceAtMost(range.last+1))
    if(expand){
        var min = range.first
        var max = range.last

        var endFound = false
        while (!endFound && min > 0) {
            if(input[min-1].isDigit())
                min--
            else
                endFound = true
        }
        endFound = false
        while (!endFound && max < input.length-1) {
            if(input[max+1].isDigit())
                max++
            else
                endFound = true
        }
        searchRange = IntRange(min, max)
    }


    return input.substring(searchRange)
}