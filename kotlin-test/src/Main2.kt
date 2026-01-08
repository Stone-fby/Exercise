
data class Student (val id: Int, val name: String, val scores: List<Int>) {
    init {
        scores.forEach { score ->
            require(score in 0..100)
        }
    }
}

data class Course(val courseId: Int, val courseName: String, val fullScore: Int = 100)

data class ClassInfo(val className: String, val students: List<Student>)

val course1 = Course(1, "数学")
val course2 = Course(2, "语文")
val course3 = Course(3, "英语")

val students = listOf(
    Student(1, "张三", listOf(95, 88, 92)),
    Student(2, "李四", listOf(78, 82, 85)),
    Student(3, "王五", listOf(90, 91, 89)),
    Student(4, "赵六", listOf(65, 70, 68)),
    Student(5, "钱七", listOf(99, 97, 100)),
    Student(6, "孙八", listOf(88, 85, 90)),
    Student(7, "周九", listOf(73, 77, 75))
)

val classInfo = ClassInfo("计算22-5", students)

/**
 * 计算学生平均分
 */
fun calculateAverageScore(student: Student): Double {
    return student.scores.average()
}

/**
 * 找出平均分最高的学生
 */
fun findTopStudent(classInfo: ClassInfo): Student? {
    return classInfo.students.maxByOrNull { calculateAverageScore(it) }
}

/**
 * 获取平均分在指定范围内的学生
 */
fun getStudentsByScoreRange(classInfo: ClassInfo, min: Int, max: Int): List<Student> {
    return classInfo.students.filter {
        val avg = calculateAverageScore(it).toInt()
        avg in min..max
    }
}

/**
 * 计算某门课程的全班平均分
 */
fun getCourseAverage(classInfo: ClassInfo, courseIndex: Int): Double {
    return classInfo.students
        .map { it.scores[courseIndex] }
        .average()
}


fun getRanking(classInfo: ClassInfo, studentId: Int) {
    val sortedStudents = classInfo.students
        .sortedByDescending { calculateAverageScore(it) }
}

/**
 * 将学生按等级分组
 */
fun groupStudentsByGrade(classInfo: ClassInfo): Map<String, List<Student>> {
    return classInfo.students.groupBy { student ->
        when (val avg = calculateAverageScore(student).toInt()) {
            in 90..100 -> "A"
            in 80..89 -> "B"
            in 70..79 -> "C"
            in 60..69 -> "D"
            else -> "F"
        }
    }
}



fun Student.totalScore(): Int = scores.sum()

fun main() {

}







