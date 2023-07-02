package com.example.skysiren.FavouritesFragment.FavouritesModelView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModel
import com.example.skysiren.Model.RepositoryInterface

class FavouritViewModelFactory (private val repo : RepositoryInterface) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouritViewModel::class.java)) {
            FavouritViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}