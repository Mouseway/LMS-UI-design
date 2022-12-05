package com.example.lms.data

import android.util.Log
import com.example.lms.R
import com.example.lms.domain.Course
import com.example.lms.domain.Note
import com.example.lms.domain.Survey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class Repository {

    private var courses = listOf(MutableStateFlow(
        Course(
            id = 15,
            name = "BOZP",
            validityDate = LocalDate.of(2023, 1, 1),
            lastVisitedPage = 0,
            furthestVisitedPage = -1,
            notes = emptyList(),
            content = listOf(
                Pair("Úvod", 0),
                Pair("Pomoc při úrazu", 2),
                Pair("Požární prevence", 5)
            ),
            pages = listOf(
                R.drawable.bozp1,
                R.drawable.bozp2,
                R.drawable.bozp3,
                R.drawable.bozp4,
                R.drawable.bozp5,
                R.drawable.bozp6,
                R.drawable.bozp7
            ),
            completed = false,
            qualifyingQuiz = true,
            certificate = false,
            description = "Bezpečnost a ochrana zdraví při práci (zkratka BOZP) je velice široký interdisciplinární (mezioborový neboli vícevědní) obor. V současné době neexistuje její oficiální definice. V odborné literatuře můžete nalézt různé definice v závislosti na úhlu pohledu na zajištění BOZP, například: „Soubor opatření (technických, organizačních, výchovných), která při správné aplikaci nebo realizaci vytvoří podmínky k tomu, aby se pravděpodobnost ohrožení nebo poškození lidského zdraví snížila na minimum.",
            lessons = 1,
            activation = LocalDate.now()
        )),
        MutableStateFlow(Course(
            id = 1,
            name = "Školení řidičů",
            validityDate = LocalDate.of(2023, 2, 20),
            lastVisitedPage = 0,
            furthestVisitedPage = -1,
            notes = emptyList(),
            content = listOf(
                Pair("Úvod", 0),
                Pair("Proč se školit?", 1),
                Pair("Závěr", 2)
            ),
            pages = listOf(
                R.drawable.slide1,
                R.drawable.slide2,
                R.drawable.slide3,
            ),
            completed = false,
            qualifyingQuiz = true,
            certificate = false,
            description = "Školení tvoří sled témat a testových otázek. Jednotlivá témata jsou zaměřena na pravidla provozu (zák. č. 361/2000 Sb.), dopravní značky, řešení předností na křižovatkách, teorii a zásady bezpečné jízdy, jednání při dopravních nehodách, následky a postihy, nejčastější „mýty“, předpisy související s provozem, technická způsobilost vozidla, první pomoc.",
            lessons = 1,
            activation = LocalDate.of(2022, 11, 15)
        )),
    )

    private val surveys = listOf(
        Survey(title = "Anketa", completed = false, date = LocalDate.of(2022, 12, 4)),
        Survey(title = "Formulář", completed = true, date = LocalDate.of(2022, 10, 16)),
        Survey(title = "Dotazník", completed = false, date = LocalDate.of(2023, 2, 18))
    )

    private val coursesFlow = MutableStateFlow(courses.map { it.value })
    private val coursesInProgressFlow = MutableStateFlow(filterInProgress())
    private val coursesToStudyFlow = MutableStateFlow(getToStudy())

    private val lastVisitedCourse = MutableStateFlow(courses[0].value)

    private val flows = mutableListOf<MutableStateFlow<Course>>()

    fun updateCourse(course: Course){
        courses.find { it.value.id == course.id}?.value = course
//        courses = courses.toMutableList().also{
//            val index = it.indexOfFirst { it.id == course.id }
//            it.removeAt(index)
//            it.add(index, course)
//        }
        Log.i("LMS course", course.toString())
//        val filtered = courses.filter { it.id != course.id}

        coursesFlow.value = courses.map { it.value }
        coursesToStudyFlow.value = getToStudy()
        coursesInProgressFlow.value = filterInProgress()
        lastVisitedCourse.value = courses.first { it.value.id == lastVisitedCourse.value.id }.value
    }

    fun getCoursesInProgress(): Flow<List<Course>> {
        return coursesInProgressFlow.asStateFlow()
    }

    fun getCoursesToStudy(): Flow<List<Course>> {
        return coursesToStudyFlow.asStateFlow()
    }

    fun getSurveysToFill(): List<Survey>{
        return surveys.filter {
            !it.completed
        }
    }

    fun getLastVisitedCourse() : Flow<Course> = lastVisitedCourse

    fun getCourseById(id: Int): Flow<Course>{
        return courses.find { it.value.id == id } ?: throw IllegalArgumentException()
    }

    private fun filterInProgress(): List<Course> {
        return courses.filter {
            it.value.furthestVisitedPage > 0 && !it.value.completed
        }.map { it.value }
    }

    private fun getToStudy(): List<Course>{
        return courses.filter {
            !it.value.completed
        }.map { it.value }
    }

    fun setLastVisitedCourse(courseId: Int){
        lastVisitedCourse.value = courses.first{it.value.id == courseId}.value
    }
}