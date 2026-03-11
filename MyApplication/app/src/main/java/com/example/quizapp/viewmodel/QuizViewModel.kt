package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.data.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel skeleton for the quiz screen.
 *
 * This file only defines placeholders for the team to complete later.
 */
class QuizViewModel(
    private val repository: GalleryRepository
) : ViewModel() {

    companion object {
        private const val MINIMUM_BILDER = 3
        private const val ANTALL_FEIL_SVAR = 2
    }

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    init {
        loadNextQuestion()
    }

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

    fun submitAnswer(chosenAnswer: String) {
        val phase = _uiState.value.phase
        if (phase !is QuizPhase.VenterPaaSvar) return

        val riktigSvar = phase.gjeldendeBilder.navn
        val erRiktig = chosenAnswer == riktigSvar

        _uiState.update { state ->
            state.copy(
                phase = QuizPhase.SvarGitt(
                    gjeldendeBilder = phase.gjeldendeBilder,
                    valgtSvar = chosenAnswer,
                    riktigSvar = riktigSvar,
                    erRiktig = erRiktig
                ),
                scoreRiktige = state.scoreRiktige + if (erRiktig) 1 else 0,
                scoreTotalt = state.scoreTotalt + 1
            )
        }
    }

    fun nextQuestion() {
        loadNextQuestion()
    }

    fun resetQuiz() {
        _uiState.update { it.copy(scoreRiktige = 0, scoreTotalt = 0) }
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        viewModelScope.launch {
            val previousBilde = when (val p = _uiState.value.phase) {
                is QuizPhase.VenterPaaSvar -> p.gjeldendeBilder
                is QuizPhase.SvarGitt -> p.gjeldendeBilder
                else -> null
            }

            _uiState.update { it.copy(phase = QuizPhase.Loading) }

            val list = repository.getAllAsc().first()

            if (list.size < MINIMUM_BILDER) {
                _uiState.update { it.copy(phase = QuizPhase.ForFaaBilder) }
                return@launch
            }

            val candidates = if (previousBilde != null) {
                list.filter { it.id != previousBilde.id }
            } else {
                list
            }
            val riktigBilde = (candidates.ifEmpty { list }).random()

            val feilSvar = list.filter { it.id != riktigBilde.id }.map { it.navn }.shuffled().take(ANTALL_FEIL_SVAR)
            val alleSvar = (listOf(riktigBilde.navn) + feilSvar).shuffled()

            _uiState.update { state ->
                state.copy(
                    phase = QuizPhase.VenterPaaSvar(
                        gjeldendeBilder = riktigBilde,
                        svaralternativer = alleSvar
                    )
                )
            }
        }
    }
}

data class QuizUiState(
    val phase: QuizPhase = QuizPhase.Loading,
    val scoreRiktige: Int = 0,
    val scoreTotalt: Int = 0
)

sealed class QuizPhase {
    data object Loading : QuizPhase()
    data object ForFaaBilder : QuizPhase()

    data class VenterPaaSvar(
        val gjeldendeBilder: BildeOppforing,
        val svaralternativer: List<String>
    ) : QuizPhase()

    data class SvarGitt(
        val gjeldendeBilder: BildeOppforing,
        val valgtSvar: String,
        val riktigSvar: String,
        val erRiktig: Boolean
    ) : QuizPhase()
}