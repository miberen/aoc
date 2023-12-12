package main.kotlin

import java.io.File

fun main(args: Array<String>) {
    println(getPart81())
    println(getPart82())
}

private fun getPart81(): Int {

    val input = File("input08.txt").readText()
    val instructions = input.substringBefore("\r").toList()
    val networks = Regex("\\w+(?=.*\\))").findAll(input).map { it.value }.chunked(3).associate { Pair(it[0], Pair(it[1], it[2])) }
    var currentLocation = "AAA"

    var sum = 0
    val instCount = instructions.count()
    while(currentLocation != "ZZZ" ){
        val network = networks[currentLocation]!!
        currentLocation = if(instructions[sum % instCount] == 'L') network.first else network.second
        sum++
    }
    return sum
}

private fun getPart82(): Long {
    val input = File("input08.txt").readText()
    val instructions = input.substringBefore("\r").toList()
    val networks = Regex("\\w+(?=.*\\))").findAll(input).map { it.value }.chunked(3).associate { Pair(it[0], Pair(it[1], it[2])) }
    val startNetworks = networks.filterKeys { it.endsWith("A") }.map { it.key }

    val indexes = mutableListOf<Int>()

    startNetworks.parallelStream().forEach { startPosition ->
        var currentLocation = startPosition
        var index = 0
        val instCount = instructions.count()

        while(!currentLocation.endsWith("Z")){
            val network = networks[currentLocation]!!
            currentLocation = if(instructions[index++ % instCount] == 'L') network.first else network.second
        }
        indexes.add(index)
    }

    return findLCMOfListOfNumbers(indexes.map { it.toLong() })
}

private fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1..<numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}