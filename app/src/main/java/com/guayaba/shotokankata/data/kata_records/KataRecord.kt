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
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KataRecord

        if (id != other.id) return false
        if (kataId != other.kataId) return false
        if (dateTime != other.dateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + kataId
        result = 31 * result + dateTime.hashCode()
        return result
    }
}



