package sk.uniza.feit.marblemaze.game

import kotlin.random.Random

object MazeGenerator {
    private fun setup(config: MazeConfig): MutableList<Cell> {
        val grid = mutableListOf<Cell>()
        for (y in 0 until config.rows)
            for (x in 0 until config.cols)
                grid.add(Cell(x, y))
        return grid
    }

    private fun createWalls(grid: MutableList<Cell>, config: MazeConfig): MutableList<Wall> {
        val walls = mutableListOf<Wall>()
        val k = Wall.THICKNESS / 2
        for (cell in grid) {
            // Right
            if (cell.sides[1] && cell.x < config.cols - 1)
                walls.add(Wall(cell.x + 1f - k, cell.x + 1f + k, cell.y - k, cell.y + 1f + k))

            // Bottom
            if (cell.sides[2] && cell.y < config.rows - 1)
                walls.add(Wall(cell.x - k, cell.x + 1f + k, cell.y + 1f - k, cell.y + 1f + k))
        }
        return walls
    }

    fun generate(config: MazeConfig, start: Cell, random: Random = Random): Level {
        val grid = setup(config)
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

        val walls = createWalls(grid, config)

        return Level(config, grid, walls, start)
    }
}