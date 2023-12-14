package main.kotlin
import java.io.File

data class Pattern (val normal: Map<Int, String>, val rotated : Map<Int, String>)

fun main(args: Array<String>) {

    val input = File("input13.txt").readText().split("\r\n\r\n").map { pattern ->
        val normal = pattern.lines().mapIndexed { y, s ->
            y to s
        }.toMap()
        val rotated = rot90Deg(pattern.lines()).mapIndexed {  y, s ->
            y to s
        }.toMap()
        Pattern(normal, rotated)
    }

    println(getPart131(input))
    println(getPart132(input))
}
private fun getPart131(input: List<Pattern>): Int {
    return input.sumOf { (normal, rotated) ->
        println("New pattern ${input.indexOfLast { it.normal == normal }+1}")
        getMirror(normal).takeIf { it != 0 } ?: getMirror(rotated, false)
    }
}
private fun getPart132(input: List<Pattern>): Long {
  return 0
}
private fun getMirror(pattern : Map<Int, String>, scanForHorizontalMirror : Boolean = true) : Int {
    val max = pattern.keys.max()
    for (i in 0..max) {
        var sum = 0
        while (pattern[i - sum] == pattern[i + 1 + sum]) {
            sum++
            if((i-sum == -1 || i+sum == max) ) {
                val result = (i+1) * if(scanForHorizontalMirror) 100 else 1
                println("${if (scanForHorizontalMirror) "HOR" else "VER"}\n${pattern[i]}\n${pattern[i + 1]} Sum: $sum It: $i ItCheck: ${i-sum} Max: $max MaxCheck: ${i+sum} Result: $result \n")
                return result
            }
        }
    }
    return 0
}

private fun carefulForceMirrorSmudges() {

}



private fun rot90Deg(strings: List<String>): MutableList<String>  {
    val maxLength = strings.maxOfOrNull { it.length } ?: return mutableListOf()
    return (0..<maxLength).map { i ->
        strings.map { it.getOrNull(i) ?: ' ' }.reversed().joinToString("")
    }.toMutableList()
}

