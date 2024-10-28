package id.overlogic.devent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "summary")
    val summary: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String,

    @ColumnInfo(name = "mediaCover")
    val mediaCover: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "ownerName")
    val ownerName: String,

    @ColumnInfo(name = "cityName")
    val cityName: String,

    @ColumnInfo(name = "quota")
    val quota: Int,

    @ColumnInfo(name = "registrants")
    val registrants: Int,

    @ColumnInfo(name = "beginTime")
    val beginTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "active")
    val active: Boolean = false,

    @ColumnInfo(name = "bookmark")
    val bookmark: Boolean = false,
)
