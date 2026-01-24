package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE isActive = 1 ORDER BY date ASC")
    fun getActiveCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses ORDER BY date ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: Int): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("UPDATE courses SET seatsRemaining = seatsRemaining - 1 WHERE id = :courseId AND seatsRemaining > 0")
    suspend fun reserveSeat(courseId: Int)

    @Query("UPDATE courses SET seatsRemaining = seatsRemaining + 1 WHERE id = :courseId AND seatsRemaining < classSize")
    suspend fun cancelSeat(courseId: Int)

    @Query("SELECT * FROM courses WHERE seatsRemaining > 0 AND isActive = 1 ORDER BY date ASC LIMIT 5")
    fun getUpcomingCoursesWithSeats(): Flow<List<Course>>
}