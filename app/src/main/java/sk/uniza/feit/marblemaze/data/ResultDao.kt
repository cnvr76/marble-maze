package sk.uniza.feit.marblemaze.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao{
    @Insert
    suspend fun insert(result: Result)

    @Query("SELECT * FROM game_results ORDER BY levelsPassed LIMIT 1")
    suspend fun getBestResult(): Result?
}