package br.grupointegrado.tads.buscadorgithub

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

class ConfiguracaoFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?,
                                     rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_github)

        for (i in 0 until preferenceScreen.preferenceCount) {
            val pref = preferenceScreen.getPreference(i)
            atualizarPreferenceSummary(pref)
        }
    }

    private fun atualizarPreferenceSummary(preference: Preference) {
        val sharedPreferences = this.preferenceScreen.sharedPreferences

        if (preference is ListPreference) {
            val valor = sharedPreferences.getString(preference.key, "")
            val itemSelecionado = preference.findIndexOfValue(valor)
            if (itemSelecionado >= 0) {
                preference.setSummary(preference.entries[itemSelecionado])
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val pref = findPreference(key)
        if (pref != null) {
            atualizarPreferenceSummary(pref)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

}