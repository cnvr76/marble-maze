package sk.uniza.feit.marblemaze.game

data class Level(
    val config: MazeConfig,
    val cells: MutableList<Cell>,
    val walls: MutableList<Wall>,
    val holes: MutableList<Hole>,
    val start: Cell,
    val end: ExitHole
)