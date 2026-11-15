package sk.uniza.feit.marblemaze.game

data class Level(
    val config: MazeConfig,
    val cells: MutableList<Cell>,
    val walls: List<Wall>,
    val start: Cell
)