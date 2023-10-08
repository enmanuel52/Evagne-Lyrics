package com.example.evagnelyrics.ui.screen.list.model

data class ListFilter(
    val favorite: Boolean = false,
    val searchMode: Boolean = false,
    val searchText: String = "",
)

sealed interface ListFilterEvent{
    object Favorite: ListFilterEvent
    object SearchMode: ListFilterEvent
    data class SearchText(val text: String): ListFilterEvent
}