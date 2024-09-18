package com.guayaba.shotokankata.ui.philosophy

import com.guayaba.shotokankata.data.NijuKun
import androidx.lifecycle.ViewModel

class PhilosophyViewModel: ViewModel() {

    fun getNijuKun() = NijuKun.entries.shuffled().first()
}