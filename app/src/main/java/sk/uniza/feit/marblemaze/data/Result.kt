package sk.uniza.feit.marblemaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val levelsPassed: Int,
    val playedAt: Long
)