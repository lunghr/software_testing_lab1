package com.example.software_testing_lab1.services

import org.springframework.stereotype.Service

@Service
class BFSService {

    fun breadthFirstSearch(graph: Map<Int, List<Int>>, start: Int): List<Int> {
        require(start in graph.keys) { "Start vertex should be in graph" }
        require(graph.isNotEmpty()) { "Graph should not be empty" }
        tailrec fun bfs(queue: List<Int>, visited: List<Int>, result: List<Int>): List<Int> {
            if (queue.isEmpty()) return result
            val (head, tail) = queue.first() to queue.drop(1)
            val newVisited = graph[head]?.filterNot { it in visited } ?: emptyList()
            return bfs(tail + newVisited, visited + newVisited, result + head)
        }
        return bfs(listOf(start), listOf(start), emptyList())
    }

}