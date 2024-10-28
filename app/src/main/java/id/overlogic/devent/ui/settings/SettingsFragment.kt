package id.overlogic.devent.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import id.overlogic.devent.databinding.FragmentSettingsBinding
import id.overlogic.devent.util.MyWorker
import id.overlogic.devent.util.SettingsPreferences
import id.overlogic.devent.util.dataStore
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = binding?.smTheme
        val switchNotification = binding?.smNotification
        workManager = WorkManager.getInstance(requireContext())

        activity?.let {
            val preferences = SettingsPreferences.getInstance(it.dataStore)
            val settingsViewModel = ViewModelProvider(this, ViewModelFactory(preferences)).get(SettingsViewModel::class.java)
            switchTheme?.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.saveThemeSetting(isChecked)
            }
            switchNotification?.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.saveNotificationSetting(isChecked)
            }

            settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->
                AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme?.isChecked = isDarkMode
            }
            settingsViewModel.getNotificationSettings().observe(viewLifecycleOwner) { isOn ->
                switchNotification?.isChecked = isOn
                if(isOn) {
                    val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
                        .setInitialDelay(0, TimeUnit.SECONDS)
                        .build()
                    workManager.enqueueUniquePeriodicWork("0", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
                } else {
                    workManager.cancelAllWork()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}