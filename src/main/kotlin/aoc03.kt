import java.io.File
import java.lang.StringBuilder
import kotlin.math.exp

fun main(args: Array<String>) {
    println(getPart31())
    println(getPart32())
}


fun getPart31() : Int {
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
            if(specialSignRegex.findAll(searchString).count() > 0){
                sum += it.value.toInt()
            }
        }
        lastString = s
    }
    return sum
}

fun getPart32(): Int {
    val input = File("input03demo.txt").readLines()
    val numberRegex = Regex("\\d+")
    val gearRegex = Regex("\\*")
    var sum = 0

    var lastString = ""
    input.forEachIndexed { index, s ->
        val gears = gearRegex.findAll(s).toList()

        gears.forEach {
            var searchString : String
            var numMatches: Sequence<MatchResult>
            var numbers = mutableListOf<Int>()

            if(lastString.isNotEmpty()) {
                searchString = getSearchString(it.range, lastString)
                numMatches = numberRegex.findAll(searchString)
                numbers.addAll(numMatches.map { num -> expandNumber(num.range, lastString) })
            }

            searchString = getSearchString(it.range, s)
            numMatches = numberRegex.findAll(searchString)
            numbers.addAll(numMatches.map { num -> expandNumber(num.range,  s) })

            if(index < input.count()-1) {
                searchString = getSearchString(it.range, input[index+1])
                numMatches = numberRegex.findAll(searchString)
                numbers.addAll(numMatches.map { num -> expandNumber(num.range, input[index+1]) })
            }


            if(numbers.count() == 2){
                sum += numbers.first() * numbers.last()
            }
        }
        lastString = s
    }
    return sum
}

fun getSearchString(range : IntRange, input : String) : String {
    return input.substring(IntRange(0.coerceAtLeast(range.first-1), (input.count()-1).coerceAtMost(range.last+1)))
}

fun expandNumber(range : IntRange, input : String,) : Int {
    var min = range.first
    var max = range.last
    if(min != 0){
        var endFound = false
        while (!endFound) {
            if(input[min-1].isDigit())
                min--
            else
                endFound = true
        }
    }
    if(max < input.length){
        var endFound = false
        while (!endFound) {
            if(input[max+1].isDigit())
                max++
            else
                endFound = true
        }
    }
    return input.substring(min, max).toInt()
}