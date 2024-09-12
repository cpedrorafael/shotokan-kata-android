package com.guayaba.shotokankata.data.quizzes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResult)

    @Query("select * from quiz_results")
    suspend fun getAll(): List<QuizResult>

    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId ORDER BY dateTime DESC LIMIT 1")
    suspend fun getLatestRecordForQuiz(quizId: Long): QuizResult?

    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId ORDER BY dateTime")
    suspend fun getAllResultsForQuiz(quizId: Long): List<QuizResult>

    @Query("SELECT * FROM quiz_results ORDER BY dateTime DESC")
    suspend fun getAllSortedByDateDesc(): List<QuizResult>

    @Delete
    fun delete(result: QuizResult)
}