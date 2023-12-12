package main.kotlin

import java.io.File

fun main(args: Array<String>) {
    println(getPart91())
    println(getPart92())
}

private fun getPart91(): Long {
    val input = File("input09.txt")
        .readLines()
        .map { line ->
            line.split(" ").map { it.toLong() } }

    return input.sumOf { extrapolateLine(it) }
}

private fun extrapolateLine(line : List<Long>, reverse : Boolean = false) : Long {
    return if (line.all { it == 0L }) 0
    else if (reverse)  line.first() - extrapolateLine(line.zipWithNext { a, b -> b - a }, true)
    else line.last() + extrapolateLine(line.zipWithNext { a, b -> b - a })
}


private fun getPart92(): Long {
    val input = File("input09.txt")
        .readLines()
        .map { line ->
            line.split(" ").map { it.toLong() } }

    return input.sumOf { extrapolateLine(it, true) }
}
