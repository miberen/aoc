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
    val possibilities1 = input.parallelStream().map { (string, errorInfo) ->
        val regexString = errorInfo.map { it.toString() }.mapIndexed { index, s ->
            if(errorInfo.count()-1 == index) "#{$s}"
            else "#{$s}\\.+"
        }.reduce { acc, i -> acc + i}
        val solutionRegex = Regex("\\.*$regexString\\.*")

        val groups = Regex("\\.+|#+|\\?+").findAll(string).toList()
        val permutations = generateCombinations(listOf(".", "#"), groups)
        val cartesianProduct = cartesianProductAsString(permutations).reduce { acc, s ->
            acc + "\n" + s
        }

        val result = solutionRegex.findAll(cartesianProduct).filter { it.value.length == string.length }.count()
        println("${cartesianProduct.count()} : $string : $result : $errorInfo")

        result

    }.reduce(Integer::sum)


    return possibilities1.get()
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

