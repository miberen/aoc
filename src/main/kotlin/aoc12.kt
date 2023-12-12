package main.kotlin
import java.io.File

fun main(args: Array<String>) {
    val input = File("input12.txt").readText().lines().map {
        val (firstPart, secondPart) = it.split(" ", limit = 2)

        firstPart to secondPart.split(",").map(String::toInt)
    }

    println(getPart121(input))
    println(getPart122(input))
}
private fun getPart121(input: List<Pair<String, List<Int>>>): Int {
    val possibilities = input.map { (string, errorInfo) ->
        val regexString = errorInfo.map { it.toString() }.mapIndexed { index, s ->
            if(errorInfo.count()-1 == index) "#{$s}"
            else "#{$s}\\.+"
        }.reduce { acc, i -> acc + i}
        val solutionRegex = Regex("[\\n|.]$regexString(?=[\\n|.])")

        val groups = Regex("\\.+|#+|\\?+").findAll(string).toList()
        val permutations = generateCombinations(listOf(".", "#"), groups)
        val cartesianProduct = cartesianProductAsString(permutations).reduce { acc, s ->
            acc + "\n" + s
        }
        println(solutionRegex.findAll(cartesianProduct).count())
        solutionRegex.findAll(cartesianProduct).count()

    }.sum()

    return possibilities
}

fun cartesianProduct(lists: List<List<String>>): List<List<String>> {
    if (lists.any { it.isEmpty() }) return emptyList()

    var result: List<List<String>> = listOf(emptyList())

    for (list in lists) {
        result = list.flatMap { element ->
            result.map { existingList ->
                existingList + element
            }
        }
    }

    return result
}
fun cartesianProductAsString(lists: List<List<String>>): Set<String> {
    if (lists.any { it.isEmpty() }) return emptySet()

    var result: List<List<String>> = listOf(emptyList())

    for (list in lists) {
        result = list.flatMap { element ->
            result.map { existingList ->
                existingList + element
            }
        }
    }

    return result.map { it.joinToString("") }.toSet()
}
fun generateCombinations(strings: List<String>, ranges: List<MatchResult>): List<List<String>> {
    fun generatePermutations(list: List<String>, length: Int): List<String> {
        if (length == 1) return list
        return list.flatMap { item ->
            generatePermutations(list, length - 1).map { item + it }
        }
    }
    return ranges.map { matchResult ->

        if(matchResult.value.contains("?"))
            generatePermutations(strings, matchResult.range.last - matchResult.range.first + 1)
        else
            listOf(matchResult.value)
    }
}

private fun getPart122(input: List<Pair<String, List<Int>>>): Long {
    return 0
}

