package main.kotlin

import java.io.File

fun main(args: Array<String>) {
    println(getPart21())
    println(getPart22())
}


private fun getPart21() : Int {
    val input = File("input02.txt").readText()
    val re = Regex("\\d* [brg]")
    var sum = 0
    input.lines().forEachIndexed { index, line ->
        val res = re.findAll(line).map { Pair(it.value.substringBefore(' ').trim().toInt(), it.value.last()) }.filter { it.first > 14 || it.first > 13 && it.second == 'g' || it.first > 12 && it.second == 'r' }.toList()
        if(res.isEmpty()){
            sum += index+1
        }
    }
    return sum
}

private fun getPart22(): Int {
    val input = File("input02.txt").readText()
    val re = Regex("\\d* [brg]")
    var sum = 0
    input.lines().forEachIndexed { index, line ->
        val res = re.findAll(line).map { Pair(it.value.substringBefore(' ').trim().toInt(), it.value.last()) }.sortedByDescending { it.first }.toList()
        val red = res.find { it.second == 'r' }?.first ?: 1
        val green = res.find { it.second == 'g' }?.first ?: 1
        val blue = res.find { it.second == 'b' }?.first ?: 1
        sum += (red * green * blue)
    }

    return sum
}