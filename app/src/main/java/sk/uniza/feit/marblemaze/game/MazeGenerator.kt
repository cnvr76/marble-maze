package sk.uniza.feit.marblemaze.game

import java.util.Stack
import kotlin.random.Random

object MazeGenerator {
    val grid = mutableListOf<Cell>()

    private fun setup(config: MazeConfig) {
        for (y in 0 until config.rows)
            for (x in 0 until config.cols)
                grid.add(Cell(x, y))
    }

    fun generate(config: MazeConfig, start: Cell, random: Random = Random): Level? {
        setup(config)
        var current: Cell? = grid[start.x + start.y * config.cols]
        val stack = java.util.ArrayDeque<Cell>()

        stack.push(current); current?.visited = true
        while (stack.isNotEmpty()) {
            current = stack.peek()
            val neighbor = current.pickUnvisitedNeighbor(grid, config)
            if (neighbor == null) {
                stack.pop()
            } else {
                current.removeWalls(neighbor)
                neighbor.visited = true
                stack.push(neighbor)
            }
        }
        return null
    }
}