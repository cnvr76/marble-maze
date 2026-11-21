package sk.uniza.feit.marblemaze.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao{
    @Insert
    suspend fun insert(result: Result)

    @Query("SELECT MAX(levelsPassed) FROM game_results ORDER BY levelsPassed LIMIT 1")
    suspend fun getBest(): Int?

    @Query("""
        SELECT 
            date(playedAt / 1000, 'unixepoch', 'localtime') as day,
            MAX(levelsPassed) AS best,
            COUNT(*) AS runs
        FROM game_results
        GROUP BY day
        ORDER BY playedAt DESC 
    """)
    suspend fun getDailyStats(): List<DailyStat>
}