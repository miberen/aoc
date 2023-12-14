package main.kotlin
import java.io.File
import kotlin.math.ceil

data class Pattern (val normal: Map<Int, String>, val rotated : Map<Int, String>)

fun main(args: Array<String>) {

    val input = File("input13demo.txt").readText().split("\r\n\r\n").map { pattern ->
        val normal = pattern.lines().mapIndexed { y, s ->
            y to s
        }.toMap()
        val rotated = rot90Deg(pattern.lines()).mapIndexed {  y, s ->
            y to s
        }.toMap()
        Pattern(normal, rotated)
    }

    println(getPart131(input))
    //println("\n" + getPart132(input))
}
private fun getPart131(input: List<Pattern>): Int {
    return input.sumOf { (normal, rotated) ->
        println("new pattern")
        getMirror(normal).takeIf { it != 0 } ?: getMirror(rotated, false)
    }
}

private fun getMirror(pattern : Map<Int, String>, horizontal : Boolean = true) : Int {
    println("new rotation")
    val max = pattern.keys.max()
    var result = 0
    for (i in 0..max) {
        var sum = 0
        while (pattern[i - sum] == pattern[i + 1 + sum]) {
            println("${pattern[i - sum]} ${pattern[i + 1 + sum]}")
            sum++
            if (sum >= ceil((max / 2f).toDouble())) {
                println(sum)
                result = (sum + 1) * if(horizontal) 100 else 1
                break
            }
        }
    }
    return result
}

private fun getPart132(input: List<Pair<String, List<Int>>>): Long {
    return 0
}

private fun rot90Deg(strings: List<String>): MutableList<String>  {
    val maxLength = strings.maxOfOrNull { it.length } ?: return mutableListOf()
    return (0..<maxLength).map { i ->
        strings.map { it.getOrNull(i) ?: ' ' }.reversed().joinToString("")
    }.toMutableList()
}

