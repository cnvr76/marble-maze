package sk.uniza.feit.marblemaze.game

data class Cell(val x: Int, val y: Int) {
    //                                [top, right, bottom, left]
    val sides: Array<Boolean> = arrayOf(true, true, true, true)
    var visited: Boolean = false

    fun pickUnvisitedNeighbor(grid: MutableList<Cell>, config: MazeConfig): Cell? {
        return listOfNotNull(
            grid.getOrNull(index(x, y - 1, config)), // Top
            grid.getOrNull(index(x + 1, y, config)), // Right
            grid.getOrNull(index(x, y + 1, config)), // Bottom
            grid.getOrNull(index(x - 1, y, config))  // Left
        ).filterNot { it.visited }.randomOrNull()
    }

    fun removeWalls(other: Cell) {
        val diffx = x - other.x
        if (diffx == -1) {
            sides[1] = false // this.Right
            other.sides[3] = false // other.Left
        } else if (diffx == 1) {
            sides[3] = false // this.Left
            other.sides[1] = false // other.Right
        }

        val diffy = y - other.y
        if (diffy == 1) {
            sides[0] = false // this.Top
            other.sides[2] = false // other.Bottom
        } else if (diffy == -1) {
            sides[2] = false // this.Bottom
            other.sides[0] = false // this.Top
        }
    }

    private fun index(i: Int, j: Int, config: MazeConfig): Int {
        if (i !in 0 until config.cols || j !in 0 until config.rows)
            return -1
        return i + j * config.cols
    }
}