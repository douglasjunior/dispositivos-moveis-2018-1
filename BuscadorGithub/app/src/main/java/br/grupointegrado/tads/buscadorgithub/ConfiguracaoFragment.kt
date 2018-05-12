package br.grupointegrado.tads.buscadorgithub

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class ConfiguracaoFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?,
                                     rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_github)
    }

}