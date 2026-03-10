package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel

/**
 * ViewModel skeleton for the quiz screen.
 *
 * This file only defines placeholders for the team to complete later.
 */
class QuizViewModel : ViewModel() {

    /*
     * TODO (required): Store the list of quiz images used in the current quiz session.
     * TODO (required): Keep currentQuestionIndex in observable state.
     * TODO (required): Keep score in observable state.
     * TODO (required): Build quiz questions from persisted gallery data.
     * TODO (required): Make quiz state survive screen rotation through this ViewModel.
     * TODO (required): Rotation support is required; full process death support can be handled later if needed.
     * TODO (optional): Randomize the order of quiz questions.
     * TODO (optional): Avoid repeating the same question too often in one session.
     */

    fun answerQuestion(isCorrect: Boolean) {
        // TODO (required): Update score and answer state for the current question.
    }

    fun nextQuestion() {
        // TODO (required): Move to the next quiz question.
    }

    fun resetQuiz() {
        // TODO (required): Reset quiz state for a new round.
    }
}
