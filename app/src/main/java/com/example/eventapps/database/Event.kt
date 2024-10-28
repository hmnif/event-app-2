package com.example.eventapps.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteEvent")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "media_cover")
    var mediaCover: String? = null,
)

@Entity(tableName = "UpcomingEvent")
data class UpcomingEvent(

    @ColumnInfo("summary")
    val summary: String? = null,

    @ColumnInfo("mediaCover")
    val mediaCover: String? = null,

    @ColumnInfo("registrants")
    val registrants: Int? = null,

    @ColumnInfo("imageLogo")
    val imageLogo: String? = null,

    @ColumnInfo("link")
    val link: String? = null,

    @ColumnInfo("description")
    val description: String? = null,

    @ColumnInfo("ownerName")
    val ownerName: String? = null,

    @ColumnInfo("cityName")
    val cityName: String? = null,

    @ColumnInfo("quota")
    val quota: Int? = null,

    @ColumnInfo("name")
    val name: String? = null,

    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo("beginTime")
    val beginTime: String? = null,

    @ColumnInfo("endTime")
    val endTime: String? = null,

    @ColumnInfo("category")
    val category: String? = null
)


@Entity(tableName = "FinishedEvent")
data class FinishedEvent(

    @ColumnInfo("summary")
    val summary: String? = null,

    @ColumnInfo("mediaCover")
    val mediaCover: String? = null,

    @ColumnInfo("registrants")
    val registrants: Int? = null,

    @ColumnInfo("imageLogo")
    val imageLogo: String? = null,

    @ColumnInfo("link")
    val link: String? = null,

    @ColumnInfo("description")
    val description: String? = null,

    @ColumnInfo("ownerName")
    val ownerName: String? = null,

    @ColumnInfo("cityName")
    val cityName: String? = null,

    @ColumnInfo("quota")
    val quota: Int? = null,

    @ColumnInfo("name")
    val name: String? = null,

    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo("beginTime")
    val beginTime: String? = null,

    @ColumnInfo("endTime")
    val endTime: String? = null,

    @ColumnInfo("category")
    val category: String? = null
)