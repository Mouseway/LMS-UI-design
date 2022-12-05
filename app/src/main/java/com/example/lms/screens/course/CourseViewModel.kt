package com.example.lms.screens.course

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lms.data.Repository
import com.example.lms.domain.Course
import com.example.lms.domain.Note
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.random.Random

class CourseViewModel(courseId: Int, private val repository: Repository) : ViewModel() {

    var course: MutableLiveData<Course> = MutableLiveData()

    val screenState = MutableLiveData(CourseScreenState.PRESENTATION)
    val noteState = MutableLiveData("")
    private var noteCreated = false
    var note: Note? = null

    init {
        viewModelScope.launch {
            repository.getCourseById(courseId).collect{
                course.value = it
            }
        }


        setLastVisited()
    }

    fun setLastPage(page: Int){
        course.value?.let {
            val furtherPage = maxOf(page, it.furthestVisitedPage)
            val newCourse = it.copy(lastVisitedPage = page, furthestVisitedPage = furtherPage)
            course.value = newCourse
            repository.updateCourse(newCourse)
        }
    }

    fun updateNote(note: Note, newText: String){
        course.value?.let{ course ->
            val notes = course.notes
            Log.i("LMS", note.toString())
            val updatedNotes =
                if(notes.contains(note)){
                    Log.i("LMS", "if")
                    notes.toMutableList().also{
                        val index = it.indexOf(note)
                        it.remove(note)
                        it.add(index, note.copy(text = newText))
                    }
                }else {
                    Log.i("LMS", "else")
                    notes.toMutableList().also{
                        it.add(note.copy(text = newText))
                    }
                }

            val updatedCourse = course.copy(notes = updatedNotes)
            repository.updateCourse(updatedCourse)
            Log.i("LMS", updatedCourse.notes.toString())
//            this.course.value = updatedCourse
        }
    }
    fun deleteNote(note: Note){
        course.value?.let { course ->
            val notes = course.notes.toMutableList().also {
                it.remove(note)
            }
            val updatedCourse = course.copy(notes = notes)
            repository.updateCourse(updatedCourse)
            this.course.value = updatedCourse
        }
    }

    fun addNewNote(text: String? = null, callback: ()->Unit = {}): Note {
        course.value?.let { course ->
            val newNote = Note(id = Random.nextInt(), date = LocalDate.now(), text = text ?: "")
            val notesCopy = course.notes.toMutableList().also{
                it.add(newNote)
            }
            val updatedCourse = course.copy(notes = notesCopy)
            Log.i("LMS   A", updatedCourse.notes.toString())
            repository.updateCourse(updatedCourse)
//            this.course.value = updatedCourse
            Log.i("LMS", updatedCourse.toString())
            callback()
            return newNote
        }
        throw IllegalArgumentException()
    }

    fun setScreenState(state: CourseScreenState){
        screenState.value = state
    }

    fun setNoteState(note: String){
        if(noteState.value == "" && note.isNotBlank() && this.note == null){
            this.note = addNewNote(note)
        }
        this.note?.let {
            updateNote(it , note )
        }
        this.note = this.note?.copy(text = note)
        noteState.value = note
    }

    fun openNewNote(){
        note = null
        noteCreated = false
        noteState.value = ""
    }

    fun completeCourse(){
        course.value?.let {
            repository.updateCourse(it.copy(completed = true))
        }
    }

    private fun setLastVisited(){
        course.value?.let {
            repository.setLastVisitedCourse(it.id)
        }
    }
}