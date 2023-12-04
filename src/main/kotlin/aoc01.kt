import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println(measureTimeMillis {
        getPart1()
    })
    println(getPart1())

    println(measureTimeMillis {
        getPart2()
    })
    println(getPart2())
}

fun getPart1() : Int {
    var sum = 0
    val re = Regex("[^0-9]")
    File("input01.txt").forEachLine {
        val numbers = re.replace(it, "")
        val digits = "" + numbers.first() + numbers.last()
        sum += digits.toInt()
    }
    return sum
}

fun getPart2(): Int {
    var sum = 0
    val collection = digitMap.keys + digitMap.values
    File("input01.txt").forEachLine { line ->
        val first = line.findAnyOf(collection)?.second
        val last = line.findLastAnyOf(collection)?.second
        var digits = "$first$last"
        digitMap.forEach{ (k, v) -> digits =  digits.replace(k, v)}
        sum += digits.toInt()
    }
    return sum
}

val digitMap = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)
