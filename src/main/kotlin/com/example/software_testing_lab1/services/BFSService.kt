package com.example.software_testing_lab1.services

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class BFSService {

    fun breadthFirstSearch(graph: Map<Int, List<Int>>, start: Int): List<Int> {
        require(start in graph.keys) { "Start vertex should be in graph" }
        tailrec fun bfs(queue: List<Int>, visited: List<Int>, result: List<Int>): List<Int> {
            if (queue.isEmpty()) return result
            val (head, tail) = queue.first() to queue.drop(1)
            val newVisited = graph[head]?.filterNot { it in visited } ?: emptyList()
            return bfs(tail + newVisited, visited + newVisited, result + head)
        }
        return bfs(listOf(start), listOf(start), emptyList())
    }

    fun generateGraph(vertices: Int, edgeProbability: Double = 0.3): Map<Int, List<Int>> {
        require(vertices > 0) { "Graph must have at least one vertex" }
        require(edgeProbability in 0.0..1.0) { "Edge probability must be between 0 and 1" }
        val graph = mutableMapOf<Int, MutableSet<Int>>().apply {
            (1..vertices).forEach { this[it] = mutableSetOf() }
        }
        (1..vertices).forEach { v ->
            val possibleNeighbors = (1..vertices).filter { it != v && it !in graph[v]!! }
            val neighborsToAdd = possibleNeighbors.filter { Random.nextDouble() < edgeProbability }
            neighborsToAdd.forEach { neighbor ->
                graph[v]!!.add(neighbor)
                graph[neighbor]!!.add(v)
            }
        }
        return graph.mapValues { it.value.toList() }
    }
}
