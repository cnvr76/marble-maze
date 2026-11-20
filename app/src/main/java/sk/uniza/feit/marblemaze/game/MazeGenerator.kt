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
            if (cell.sides[Cell.SIDE.RIGHT.ordinal] && cell.x < config.cols - 1)
                walls.add(Wall(cell, cell.x + 1f - k, cell.x + 1f + k, cell.y - k, cell.y + 1f + k))

            // Bottom
            if (cell.sides[Cell.SIDE.BOTTOM.ordinal] && cell.y < config.rows - 1)
                walls.add(Wall(cell, cell.x - k, cell.x + 1f + k, cell.y + 1f - k, cell.y + 1f + k))
        }
        return walls
    }

    private fun removeRandomWalls(grid: MutableList<Cell>, config: MazeConfig, random: Random = Random) {
        fun tryRemoveWall(cell: Cell, side: Cell.SIDE) {
            val other = cell.getNeighbor(side, grid, config) ?: return
            if (random.nextFloat() < config.openness)
                cell.removeWalls(other)
        }

        for (cell in grid) {
            tryRemoveWall(cell, Cell.SIDE.BOTTOM)
            tryRemoveWall(cell, Cell.SIDE.RIGHT)
        }
    }

    private fun createHoles(walls: MutableList<Wall>, start: Cell, config: MazeConfig, random: Random = Random): MutableList<Hole> {
        val holes = mutableListOf<Hole>()
        val iterator = walls.iterator()
        while (iterator.hasNext()) {
            val wall = iterator.next()

            val nearStart = start.center.dist(wall.center) < Hole.SAFE_DISTANCE
            val nearOtherHole = holes.any { hole -> hole.center.dist(wall.center) < Hole.MIN_SELF_DISTANCE }

            if (random.nextFloat() < config.holeness && !nearStart && !nearOtherHole) {
                holes.add(Hole(wall.center.x, wall.center.y, 0.3f))
                iterator.remove()
            }
        }
        return holes
    }

    fun generate(config: MazeConfig, start: Cell): Level {
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

        removeRandomWalls(grid, config)
        val walls = createWalls(grid, config)
        val holes = createHoles(walls, start, config)

        return Level(config, grid, walls, holes, start)
    }
}