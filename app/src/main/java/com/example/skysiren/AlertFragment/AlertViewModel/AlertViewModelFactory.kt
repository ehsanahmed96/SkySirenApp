package com.example.skysiren.AlertFragment.AlertViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skysiren.Model.RepositoryInterface

class AlertViewModelFactory (private val repo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}