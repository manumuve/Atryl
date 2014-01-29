package com.manumuve.atryl.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;


public class DialogEditCategory extends DialogFragment {

	
	/* -------------------------------------------------------
     * Comprobar que la clase que hace la llamada implementa
     * la interfaz necesaria para los eventos callback
     * -------------------------------------------------------
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
    /* -------------------------------------------------------  */

	DataSingleton dataSingleton;
	EditText etxCategoryName;
	int category;
	String categoryNewName;

    /**
     * Create a new instance of DialogEditCategory, providing "cat"
     * as an argument.
     */
    public static DialogEditCategory newInstance(int cat) {
    	DialogEditCategory f = new DialogEditCategory();

        // Supply cat input as an argument.
        Bundle args = new Bundle();
        args.putInt("cat", cat);
        f.setArguments(args);

        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getInt("cat");
        categoryNewName = null;
        dataSingleton = DataSingleton.getInstance();
    }
    

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.dialog_edit_category, null);

		etxCategoryName = (EditText) view.findViewById(R.id.editxDialogEditCategory_Name);
		etxCategoryName.setHint(dataSingleton.categories.get(category).getName());
		
		builder
		.setTitle(getResources().getString(R.string.dialog_edit_category))
		
		.setView(view)
		
		.setPositiveButton(getResources().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				if (!etxCategoryName.getText().toString().equals("")) {
					categoryNewName = etxCategoryName.getText().toString();
				}

				mListener.onDialogPositiveClick(DialogEditCategory.this);
			}
		})
		
		.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogNegativeClick(DialogEditCategory.this);
			}
		})
		;

		return builder.create();
	}
	
	public int getCategory () {
		return category;
	}
	
	public String getCategoryNewName () {
		return categoryNewName;
	}

}