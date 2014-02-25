/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.activity;

import android.app.Activity;
import android.os.Bundle;

import com.manumuve.atryl.fragment.FragmentSettings;


/** Actividad de configuración de la aplicación
* Construye la pantalla de configuración que gestiona
* el un fragment de la clase FragmentSettings.
* @author Manumuve
*/
public class SettingsActivity extends Activity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FragmentSettings())
                .commit();
    }

}
