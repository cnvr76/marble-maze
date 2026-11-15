package sk.uniza.feit.marblemaze

import org.junit.Test
import sk.uniza.feit.marblemaze.game.Cell
import sk.uniza.feit.marblemaze.game.MazeConfig
import sk.uniza.feit.marblemaze.game.MazeGenerator

class MazeGeneratorTest {

    @Test
    fun printMaze() {
        val config = MazeConfig(rows = 6, cols = 8)
        MazeGenerator.generate(config, Cell(0, 0))
//        println(ascii(MazeGenerator.grid, config.cols, config.rows))
    }

    // sides = [top, right, bottom, left]
    private fun ascii(grid: List<Cell>, cols: Int, rows: Int): String {
        val sb = StringBuilder()
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                sb.append("+").append(if (grid[x + y * cols].sides[0]) "---" else "   ")
            }
            sb.append("+\n")
            for (x in 0 until cols) {
                val c = grid[x + y * cols]
                sb.append(if (c.sides[3]) "|" else " ").append(if (c.visited) "   " else " # ")
            }
            sb.append(if (grid[cols - 1 + y * cols].sides[1]) "|" else " ").append("\n")
        }
        for (x in 0 until cols) {
            sb.append("+").append(if (grid[x + (rows - 1) * cols].sides[2]) "---" else "   ")
        }
        return sb.append("+").toString()
    }
}