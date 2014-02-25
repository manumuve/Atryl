/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.dialog;

import android.support.v4.app.DialogFragment;

/** 
 * Interfaz MyDialogInterface.
 * La actividad que crea una instancia de un DialogFragment de la aplicación,
 * debe implenentar esta interface para recibir las llamadas de retorno.
 * Cada método devuelve el DialogFragment por si el receptor necesita consultarlo.
 */
public interface MyDialogInterface {
	/** Botón "aceptar" */
	public void onDialogPositiveClick(DialogFragment dialog);
	/** Botón "cancelar" */
	public void onDialogNegativeClick(DialogFragment dialog);
	/** Botón neutral (comportamiento personalizado en el DialogFragment) */
	public void onDialogNeutralClick (DialogFragment dialog);
}
