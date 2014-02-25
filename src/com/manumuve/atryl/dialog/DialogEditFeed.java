/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;

/** Clase encargada de la gestión del diálodo "Editar suscripción"
 * 
 * @author Manu
 *
 */
public class DialogEditFeed extends DialogFragment {

	
	/** 
     * Comprobar que la clase que hace la llamada implementa
     * la interfaz necesaria para los eventos callback
     *
     * @see http://developer.android.com/intl/es/training/basics/fragments/communicating.html
     */
	// Use this instance of the interface to deliver action events
	MyDialogInterface mListener;

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (MyDialogInterface) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	DataSingleton dataSingleton;
	EditText etxFeedTitle;
	EditText etxFeedDescription;
	int category;
	int feed;
	String feedNewTitle;
	String feedNewDescription;
	CheckBox checkboxDeleteFeed;
	Boolean deleteFeed;

    /**
     * Create a new instance of DialogEditCategory, providing "cat"
     * as an argument.
     */
    public static DialogEditFeed newInstance(int category, int feed) {
    	DialogEditFeed f = new DialogEditFeed();

        // Supply cat input as an argument.
        Bundle args = new Bundle();
        args.putInt("category", category);
        args.putInt("feed", feed);
        f.setArguments(args);

        return f;
    }
    
    /**
     * Iniciar las variables necesarias.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getInt("category");
        feed = getArguments().getInt("feed");
        feedNewTitle = null;
        feedNewDescription = null;
        deleteFeed = false;
        dataSingleton = DataSingleton.getInstance();
    }
    
    /**
     * Método que crea el diálogo "Editar suscripción".
     * 
     * @return diálogo creado.
     */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.dialog_edit_feed, null);
		
		checkboxDeleteFeed = (CheckBox) view.findViewById(R.id.checkboxDialogEditFeed_DeleteFeed);

		etxFeedTitle = (EditText) view.findViewById(R.id.editxDialogEditFeed_Title);
		etxFeedTitle.setHint(dataSingleton.categories.get(category).getFeed(feed).getTitle());
		
		etxFeedDescription = (EditText) view.findViewById(R.id.editxDialogEditFeed_Description);
		etxFeedDescription.setHint(dataSingleton.categories.get(category).getFeed(feed).getDescription());
		
		builder
		.setTitle(getResources().getString(R.string.dialog_edit_feed)) // TODO: strings.xml y lo mismo para los botones
		
		.setView(view)
		
		.setPositiveButton(getResources().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				if (!etxFeedTitle.getText().toString().equals("")) {
					feedNewTitle = etxFeedTitle.getText().toString();
				}
				
				if (!etxFeedDescription.getText().toString().equals("")) {
					feedNewDescription = etxFeedDescription.getText().toString();
				}

				mListener.onDialogPositiveClick(DialogEditFeed.this);
			}
		})
		
		.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogNegativeClick(DialogEditFeed.this);
			}
		})
		
		.setNeutralButton(getResources().getString(R.string.button_delete_feed), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				if (checkboxDeleteFeed.isChecked()) {
					deleteFeed = true;
				} else {
					deleteFeed = false;
				}
				
				mListener.onDialogNeutralClick(DialogEditFeed.this);
			}
		});

		return builder.create();
	}
	
	/**
	 * Devuelve la posición de la categoría a la que pertenece
	 * el feed a editar.
	 * @return posición de la categoría.
	 */
	public int getCategory () {
		return category;
	}
	
	/**
	 * Devuelve la posición del feed a editar.
	 * @return posición del feed a editar.
	 */
	public int getFeed () {
		return feed;
	}
	
	/**
	 * Devuelve el nuevo nombre del feed que se editar.
	 * @return nuevo nombre del feed.
	 */
	public String getFeedNewTitle () {
		return feedNewTitle;
	}
	
	/**
	 * Devuelve la nueva descripción del feed a editar.
	 * @return nueva descripción del feed.
	 */
	public String getFeedNewDescription () {
		return feedNewDescription;
	}
	
	/**
	 * Devuelve la confirmación de eliminar el feed.
	 * @return true si la eliminación está confirmada, false si no lo está.
	 */
	public Boolean getDeleteFeed () {
		return deleteFeed;
	}

}
