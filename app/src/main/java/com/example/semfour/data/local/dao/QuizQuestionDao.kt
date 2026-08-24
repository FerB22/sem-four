package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.QuizQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizQuestionDao {

    @Query("SELECT * FROM quiz_questions WHERE topicId = :topicId ORDER BY isCustom DESC, id ASC")
    fun getQuestionsForTopic(topicId: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions WHERE topicId = :topicId ORDER BY isCustom DESC, id ASC")
    suspend fun getQuestionsForTopicSync(topicId: String): List<QuizQuestionEntity>

    @Query("SELECT * FROM quiz_questions WHERE topicId = :topicId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsForTopic(topicId: String, limit: Int = 3): List<QuizQuestionEntity>

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuestions(): Flow<List<QuizQuestionEntity>>

    @Query("SELECT COUNT(*) FROM quiz_questions")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM quiz_questions WHERE topicId = :topicId")
    suspend fun countForTopic(topicId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuizQuestionEntity)

    @Update
    suspend fun updateQuestion(question: QuizQuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuizQuestionEntity)

    @Query("DELETE FROM quiz_questions WHERE id = :id")
    suspend fun deleteQuestionById(id: String)
}
