package com.example.skysiren.SettingFragment.SettingView

import kotlinx.coroutines.flow.Flow

interface SettingInterface {
    suspend fun getTemp(temp: Flow<String>)
}