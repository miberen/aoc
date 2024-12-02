package main.kotlin
import java.io.File


fun main(args: Array<String>) {

    val input = File("input15.txt").readText()

    println(getPart151(input))
    println("\n" + getPart152(input))
}
private fun getPart151(input: String): Int {
    return input.split(",").sumOf { doHash(it) }
}


private fun getPart152(input: String): Int {
    val instructions = Regex("([a-z]+[^=-])(.)?(\\d)?").findAll(input).map { match ->
        val matchGroups = match.groupValues.subList(1, match.groupValues.lastIndex+1).toList()
        Instruction(matchGroups[0], matchGroups[1].first(), if(matchGroups[2] != "") matchGroups[2].toInt() else 0)
    }.toList()
    val map = (0..255).associateWith { mutableListOf<Lens>() }

    instructions.forEach { instruction ->
        if(instruction.operation == '-'){
            val lens = map[instruction.hash]!!.firstOrNull { it.label == instruction.label }
            map[instruction.hash]!!.remove(lens)
        }

        else {
            if(map[instruction.hash]!!.any { it.label == instruction.label })
                map[instruction.hash]!!.first {it.label == instruction.label}.focus = instruction.focus
            else
                map[instruction.hash]!!.add(instruction.lens)
        }
//        println(instruction)
//        println(map.entries.filter { it.value.isNotEmpty() })
//        println()
    }
    val result = map.entries.sumOf { entry ->
        entry.value.fold(0) { acc, lens ->
            acc + (( 1 + entry.key) * (entry.value.indexOf(lens) + 1) * lens.focus)
        }.toInt()

    }
    return result
}

data class Instruction(val label : String, val operation : Char, val focus : Int) {
    val lens = Lens(label, focus)
    val hash = doHash(label)
}

data class Lens(var label : String, var focus : Int)

fun doHash(input: String): Int =
    input.fold(0) { acc, char -> (acc + char.code) * 17 % 256 }

