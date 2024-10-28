package id.overlogic.devent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _selectedFragmentId = MutableLiveData<Int>()
    val selectedFragmentId: LiveData<Int> = _selectedFragmentId

    fun setSelectedFragmentId(itemId: Int) {
        _selectedFragmentId.value = itemId
    }
}
