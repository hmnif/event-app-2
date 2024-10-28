package com.example.eventapps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteEvent(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent?>

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("DELETE FROM FavoriteEvent WHERE id = :id")
    suspend fun deleteFavoriteEventById(id: String)

}

@Dao
interface UpcomingEventDao {

    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingEvent(event: List<UpcomingEvent>)

    // delete
    @Query("DELETE FROM UpcomingEvent")
    suspend fun deleteAllUpcomingEvent()

    @Query("SELECT * FROM UpcomingEvent")
    fun getUpcomingEvent(): LiveData<List<UpcomingEvent>>

    @Query("SELECT * FROM UpcomingEvent WHERE id = :id")
    fun getUpcomingEventById(id: Int): LiveData<UpcomingEvent?>

}

@Dao
interface FinishedEventDao {
    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinishedEvent(event: List<FinishedEvent>)

    // delete
    @Query("DELETE FROM FinishedEvent")
    suspend fun deleteAllFinishedEvent()

    @Query("SELECT * FROM FinishedEvent")
    fun getFinishedEvent(): LiveData<List<FinishedEvent>>

    @Query("SELECT * FROM FinishedEvent WHERE id = :id")
    fun getFinishedEventById(id: Int): LiveData<FinishedEvent?>

}