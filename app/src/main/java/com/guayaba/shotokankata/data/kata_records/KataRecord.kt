package com.guayaba.shotokankata.data.kata_records

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "kata_records")
data class KataRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val kataId: Int,
    val dateTime: LocalDateTime
)



