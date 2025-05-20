package com.krissphi.id.dicoding_events_mid_submission_app.ui.favorites

import androidx.lifecycle.*
import com.krissphi.id.dicoding_events_mid_submission_app.data.local.FavoriteEventEntity
import com.krissphi.id.dicoding_events_mid_submission_app.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: EventRepository) : ViewModel() {

    val favorites: LiveData<List<FavoriteEventEntity>> = repository.getAllFavoriteEvents()

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun setFavoriteStatus(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }

    fun toggleFavorite(event: FavoriteEventEntity, isCurrentlyFavorited: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorited) {
                repository.deleteFavorite(event)
            } else {
                repository.insertFavorite(event)
            }
        }
    }

    suspend fun checkIfFavorite(eventId: Int) {
        _isFavorite.value = repository.isFavorite(eventId)
    }
}
