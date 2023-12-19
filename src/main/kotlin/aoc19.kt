package main.kotlin
import java.io.File

data class MachinePart(val x : Int, val m : Int, val a : Int, val s : Int, var next: String)
private class WorkFlow( val flows : List<(MachinePart) -> String> ) {
    fun getNextStepForPart(part : MachinePart) : String {
        flows.forEach { flow ->
            val result = flow(part)
            if(result != "") return result
        }
        return ""
    }
}

fun main(args: Array<String>) {
    val (flowString, partString) = File("input19demo.txt").readText().split("\r\n\r\n").take(2)

    //println(getPart181(partString, flowString))
    println(getPart182(flowString))
}
private fun getPart181(partString : String, flowString : String): Int {
    val parts = partString.lines().map { part ->
        val (x, m ,a, s) = Regex("\\d+").findAll(part).map { it.value.toInt() }.toList().take(4)
        MachinePart(x, m, a, s, "in")
    }
    val workflows = flowString.lines().associate { workflow ->
        val name = workflow.substringBefore("{")
        val steps = workflow.substringAfter("{").trim('}').split(',')
        val defaultOut = steps.first { !it.contains(':') }
        val criteria = steps.filter { it.contains(':') }.map { criteria ->
            val field = criteria.first().toString()
            val number = Regex("\\d+").find(criteria)!!.value.toInt();
            { part: MachinePart ->
                val property = MachinePart::class.members.first { it.name == field }
                val gt = criteria.contains(">");
                if (gt && property.call(part) as Int > number || !gt &&  (property.call(part) as Int) < number)
                    criteria.substringAfter(":")
                else ""
            }
        }.toMutableList()

        criteria.add { _: MachinePart -> defaultOut }
        name to WorkFlow(criteria.toList())
    }
    for(part in parts){
        while(part.next != "A" && part.next != "R") {
            println(part)
            part.next = workflows[part.next]!!.getNextStepForPart(part)
        }
    }
    return parts.filter { it.next == "A" }.sumOf { it.m + it.s + it.x + it.a }
}

data class Criteria(val variable : String, val range : IntRange, val next : String)
data class Flow(val name : String, val criteria : List<Criteria>, val default : String) {
    fun isFinished() : Boolean {
        return criteria.any {it.next == "A" || it.next == "R" }
    }
}

private fun getPart182(workflows: String): Long {
    val flows = workflows.lines().associate { line ->
        val name = line.substringBefore("{")
        val default = line.substringAfterLast(",").trim('}')
        val criteria = Regex("\\w?[<>]+\\w+:\\w+").findAll(line).map { match ->
            val (variable, number, next) = match.value.split('<', '>', ':')
            val greaterThan = match.value.contains(">")
            val range = if (greaterThan) IntRange(number.toInt(), 4000) else IntRange(0, number.toInt())
            Criteria(variable, range, next)
        }.toList()
        name to Flow(name, criteria, default)
    }
    //flows.forEach { println(it) }

    val start = flows["in"]!!
    val paths = mutableListOf(mutableListOf(start))

    while (paths.any { path -> !path.last().isFinished() }) {
        val currentPath = paths.first { path -> !path.last().isFinished() }
        println(currentPath.last().default)

        if(currentPath.last().default != "A" && currentPath.last().default != "R")
            currentPath.add(flows[currentPath.last().default]!!)

        currentPath.last().criteria.forEach { criteria ->
            val sidePath = currentPath.toMutableList()
            sidePath.add(flows[criteria.next]!!)
            paths.add(sidePath)
        }


    }
    println(paths)

    return 0
}
