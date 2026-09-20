package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {

    @Insert
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM Student")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    suspend fun getStudentById(studentId: String): Student?

    @Query("SELECT * FROM Student WHERE schoolEmail = :schoolEmail")
    suspend fun getStudentByEmail(schoolEmail: String): Student?
}