package main.kotlin
import java.io.File
import kotlin.math.abs
import kotlin.math.pow

private data class Node(val point: PointStar, var f : Int = 0, var h : Int = 0, var g : Int = 0, var initCost : Int = 0, var parent : Node? = null) {
    fun distance(otherNode : Node) : Int {
        return abs(this.point.x - otherNode.point.x) + abs(this.point.y - otherNode.point.y)
    }

    override fun toString(): String {
        return "$point"
    }
    fun bridgeTooFar(newNode : Node) : Boolean {
        if(parent?.parent != null){
            if(abs(abs(newNode.point.x - parent!!.point.x) - parent!!.parent!!.point.x) == 3 ||
                abs(abs(newNode.point.y - parent!!.point.y) - parent!!.parent!!.point.y) == 3)
                println(abs(abs(newNode.point.y - parent!!.point.y) - parent!!.parent!!.point.y))
                return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        return if(other is Node)
            other.point == this.point
        else false
    }

    override fun hashCode(): Int {
        var result = point.x
        result = 31 * result + point.y
        return result
    }
}

private data class PointStar(val x : Int, val y : Int) : Comparable<PointStar>  {
    override fun compareTo(other: PointStar) = compareValuesBy( this, other,
        { it.x },
        { it.y })
}

private class PathFinder(){
    val openList = mutableListOf<Node>()
    val closedList = mutableListOf<Node>()

    fun findPath(maze : Set<Node>, startNode : Node, endNode : Node) : List<Node> {
        openList.add(startNode)

        while(openList.isNotEmpty()) {
            var currentNode = openList.minByOrNull { it.f }!!
            openList.remove(currentNode)
            closedList.add(currentNode)

            if(currentNode == endNode) { // Trace path back
                val path = mutableListOf<Node>()
                do {
                    path.add(currentNode)
                    currentNode = currentNode.parent!!
                }
                while (currentNode.parent != null)
                return path.reversed()
            }

            val children = mutableListOf<Node>()
            for(neighbour in listOf(
                0 to -1,
                0 to 1,
                -1 to 0,
                1 to 0,)) {
                val newPos = PointStar(currentNode.point.x + neighbour.first, currentNode.point.y + neighbour.second)
                val mazePoint = maze.firstOrNull{ it.point == newPos }?.copy()
                if(mazePoint != null) {
                    mazePoint.parent = currentNode
                    children.add(mazePoint)
                }
            }

            for(child in children)
            {
                if(closedList.contains(child))
                    continue
                //Calc g, f, h
                child.g = child.initCost
                child.h = child.distance(endNode) - child.initCost
                child.f = child.g + child.h

                //If in open and g is higher, skip
                if(openList.contains(child)){
                    if(child.g > openList[openList.indexOf(child)].g )
                        continue
                }

                //add to open
                openList.add(child)
            }
        }
        return mutableListOf()
    }
}

fun main(args: Array<String>) {

    val input = File("input17demo.txt").readText().lines().flatMapIndexed { yIndex, line ->
        line.mapIndexed { xIndex, c ->
            Node(PointStar(xIndex, yIndex), initCost = c.digitToInt())
        }
    }.toSet()

    println(getPart171(input))
    //println("\n" + getPart172(input))
}
private fun getPart171(input: Set<Node>): Int {
    val finder = PathFinder()

    val result = finder.findPath(input, input.minBy { it.point }, input.maxBy { it.point })

    for (y in 0..input.maxOf { it.point.y }){
        for (x in 0 .. input.maxOf { it.point.x }){
            if(result.any { it.point == PointStar(y, x)})
                print("X")
            else
                print(input.first {it.point == PointStar(x, y)}.initCost )
        }
        print("\n")
    }

    return result.sumOf { it.initCost }
}

private fun getPart172(input: List<Pair<String, List<Int>>>): Long {
    return 0
}