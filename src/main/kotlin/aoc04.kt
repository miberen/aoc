import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
    println(getPart41())
    println(getPart42())
}

fun getPart41(): Int {
    val input = File("input04.txt").readLines()
    val sum = input.sumOf { card ->
        card.substringAfter(":")
            .split("|")
            .map { x ->
                x.trim().replace("  ", " ").split(" ")
            }
            .zipWithNext()
            .map { (winning, scratch) ->
                val count = winning.intersect(scratch.toSet()).count()

                if (count > 0) multiply(count, 1)
                else 0
            }.first()
    }

    return sum
}

fun multiply(matchCount: Int, number: Int): Int {
    if (matchCount == 1) return number

    return multiply(matchCount - 1, number * 2)
}

fun calcCard(index: Int, scoreMap: MutableMap<Int, Int>, cardValues: List<Int>) {
    if (index == cardValues.count()) return

    for (i in 1..cardValues[index]) {
        scoreMap[index + i] = scoreMap[index + i]!! + scoreMap[index]!!
    }
    calcCard(index + 1, scoreMap, cardValues)
}

fun getPart42(): Int {
    val input = File("input04.txt").readLines()
    val scoreMap =
        (0..input.count())
            .zip(List(input.count()) { 1 })
            .toMap().toMutableMap()

    val cardValues = input.map { card ->
        card.substringAfter(":")
            .split("|")
            .map { x ->
                x.trim().replace("  ", " ").split(" ")
                    .map { y -> y.toInt() }
            }.zipWithNext()
            .sumOf { (winning, scratch) ->
                winning.intersect(scratch.toSet()).count()
            }
    }

    calcCard(0, scoreMap, cardValues)
    return scoreMap.values.sum()
}

