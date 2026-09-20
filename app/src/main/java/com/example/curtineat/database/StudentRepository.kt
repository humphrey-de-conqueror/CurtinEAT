package com.example.curtineat.database

class StudentRepository(
    private val studentDao: StudentDao
) {

    suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(student)
    }

    suspend fun getAllStudents(): List<Student> {
        return studentDao.getAllStudents()
    }

    suspend fun getStudentById(studentId: String): Student? {
        return studentDao.getStudentById(studentId)
    }

    suspend fun getStudentByEmail(schoolEmail: String): Student? {
        return studentDao.getStudentByEmail(schoolEmail)
    }
}