package sk.uniza.feit.marblemaze.game

data class Cell(val x: Int, val y: Int) {
    enum class SIDE { TOP, RIGHT, BOTTOM, LEFT }
    //                                [top, right, bottom, left]
    val sides: Array<Boolean> = arrayOf(true, true, true, true)
    var visited: Boolean = false

    fun pickUnvisitedNeighbor(grid: MutableList<Cell>, config: MazeConfig): Cell? {
        return listOfNotNull(
            getNeighbor(SIDE.TOP, grid, config),
            getNeighbor(SIDE.RIGHT, grid, config),
            getNeighbor(SIDE.BOTTOM, grid, config),
            getNeighbor(SIDE.LEFT, grid, config)
        ).filterNot { it.visited }.randomOrNull()
    }

    fun getNeighbor(side: SIDE, grid: MutableList<Cell>, config: MazeConfig): Cell? {
        return when (side) {
            SIDE.TOP -> grid.getOrNull(index(x, y - 1, config))
            SIDE.RIGHT -> grid.getOrNull(index(x + 1, y, config))
            SIDE.BOTTOM -> grid.getOrNull(index(x, y + 1, config))
            SIDE.LEFT -> grid.getOrNull(index(x - 1, y, config))
        }
    }

    fun removeWalls(other: Cell) {
        val diffx = x - other.x
        if (diffx == -1) {
            sides[SIDE.RIGHT.ordinal] = false // this.Right
            other.sides[SIDE.LEFT.ordinal] = false // other.Left
        } else if (diffx == 1) {
            sides[SIDE.LEFT.ordinal] = false // this.Left
            other.sides[SIDE.RIGHT.ordinal] = false // other.Right
        }

        val diffy = y - other.y
        if (diffy == 1) {
            sides[SIDE.TOP.ordinal] = false // this.Top
            other.sides[SIDE.BOTTOM.ordinal] = false // other.Bottom
        } else if (diffy == -1) {
            sides[SIDE.BOTTOM.ordinal] = false // this.Bottom
            other.sides[SIDE.TOP.ordinal] = false // this.Top
        }
    }

    private fun index(i: Int, j: Int, config: MazeConfig): Int {
        if (i !in 0 until config.cols || j !in 0 until config.rows)
            return -1
        return i + j * config.cols
    }
}