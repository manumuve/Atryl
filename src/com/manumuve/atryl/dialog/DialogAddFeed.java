package com.manumuve.atryl.dialog;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.manumuve.atryl.R;
import com.manumuve.atryl.asynctask.FeedLoad;
import com.manumuve.atryl.asynctask.FeedLoadInterface;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssFeed;
import com.manumuve.atryl.util.Utils;


public class DialogAddFeed extends DialogFragment implements FeedLoadInterface {

	
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

	private DataSingleton dataSingleton;
	private EditText etxFeedURL;
	private EditText etxFeedTitle;
	private EditText etxFeedDescription;
	private EditText etxNewCategory;
	private Button btSearchURL;
	private LinearLayout lyFeedDetails;
	private Spinner spCategories;

	private String feedUrl;
	private URL feedURL;
	
	private boolean _isLoaded;
	
	/* Variables gestión de suscripciones */
	private RssFeed newRssFeed = null;
	private int newRssFeedCategory;

    /**
     * Create a new instance of DialogEditCategory, providing "cat"
     * as an argument.
     */
//    public static DialogEditFeed newInstance(int category, int feed) {
//    	DialogEditFeed f = new DialogEditFeed();
//
//        // Supply cat input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("category", category);
//        args.putInt("feed", feed);
//        f.setArguments(args);
//
//        return f;
//    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSingleton = DataSingleton.getInstance();
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.dialog_add_feed, null);
		lyFeedDetails = (LinearLayout) view.findViewById(R.id.lyDialogAddFeed_FeedDetails);
		lyFeedDetails.setVisibility(View.GONE);
		etxFeedURL = (EditText) view.findViewById (R.id.editxDialogAddFeed_URL);
		etxFeedTitle = (EditText) view.findViewById(R.id.editxDialogAddFeed_FeedTitle);
		etxFeedDescription = (EditText) view.findViewById(R.id.editxDialogAddFeed_FeedDescription);
		etxNewCategory = (EditText) view.findViewById(R.id.editxDialogAddFeed_NewCategory);
		etxNewCategory.setVisibility(View.GONE);
		btSearchURL = (Button) view.findViewById(R.id.btDialogAddFeed_SearchURL);
		spCategories = (Spinner) view.findViewById(R.id.spDialogAddFeed_Category);
		
		builder
		.setTitle(getResources().getString(R.string.dialog_add_feed))
		
		.setView(view)
		
		.setPositiveButton(getResources().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				// TODO: Aquí las comprobaciones sobre formatos, etc
				if (_isLoaded) {
				
				if (!etxFeedTitle.getText().toString().equals("")) {
					newRssFeed.setTitle(etxFeedTitle.getText().toString());
				}
				
				if (!etxFeedDescription.getText().toString().equals("")) {
					newRssFeed.setDescription(etxFeedDescription.getText().toString());
				}
				}

				mListener.onDialogPositiveClick(DialogAddFeed.this);
			}
		})
		
		.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				mListener.onDialogNegativeClick(DialogAddFeed.this);
			}
		})
		;
		
		btSearchURL.setOnClickListener(new View.OnClickListener() {

            @Override
			public void onClick(View v) {
            	if (lyFeedDetails.getVisibility() == View.VISIBLE) {
            		lyFeedDetails.setVisibility(View.GONE);
            	}
            	feedUrl = etxFeedURL.getText().toString();

            	// Forzar añadido de http al inicio
            	if (!feedUrl.startsWith("http://") && !feedUrl.startsWith("https://")){
            		feedUrl = "http://" + feedUrl;
            	}
            	
            	feedURL = Utils.stringToURL (feedUrl); //TODO: comprobar si feedURL existe
            	
            	if (feedURL == null) {
            		Toast.makeText(getActivity(),  etxFeedURL.getText().toString() + " no parece una URL válida.", Toast.LENGTH_SHORT).show();
            	} else {
            		loadFeed (feedURL);
            	}
            }
            	
        });
		
		

		return builder.create();
	}
	
	/* Cargar feed desde la url indicada.
	 * Los resultados se recogen en FeedLoadInterface.
	 */
	public void loadFeed (URL url) {
		FeedLoad task = new FeedLoad(this, url);
		task.execute(url);
	}

	
	/* FeedLoadInterface --------------------------------------------------- */
	@Override
	public void onFeedLoadStart(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFeedLoadProgress(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFeedLoadComplete(RssFeed rssFeed) {
		if (rssFeed == null) {
			_isLoaded = false;
			Toast.makeText(getActivity(), getResources().getString(R.string.toast_error_loading_feed), Toast.LENGTH_LONG).show();
		}
		else {
			_isLoaded = true;
			
			newRssFeed = rssFeed;
			
			Toast.makeText(getActivity(),  getResources().getString(R.string.toast_feed_loaded) + " " + newRssFeed.getTitle(), Toast.LENGTH_SHORT).show();
			
			etxFeedTitle.setHint(newRssFeed.getTitle());
			etxFeedDescription.setHint(newRssFeed.getDescription());
			
			ArrayList<String> categorias = new ArrayList<String>();
			
			for (int i = 0 ; i<dataSingleton.categories.size(); i++) {
				categorias.add(i, dataSingleton.categories.get(i).getName());
			}
			
			categorias.add(dataSingleton.categories.size(), getResources().getString(R.string.string_new_category)); //TODO: recurso String
			
	        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categorias);
	        
	        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	         
	        spCategories.setAdapter(adaptador);

	        spCategories.setOnItemSelectedListener(
	                new AdapterView.OnItemSelectedListener() {
	                @Override
					public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
	                	if (position == dataSingleton.categories.size()) {
	                		etxNewCategory.setVisibility(View.VISIBLE);
	                		if (dataSingleton.categories.size() == 0)
	                			etxNewCategory.setText(getResources().getString(R.string.hint_new_category));
	                	} else {
	                		if (etxNewCategory.getVisibility() == View.VISIBLE) {
	                			etxNewCategory.setVisibility(View.GONE);
	                		}
	                		
	                		newRssFeedCategory = position;
	                	}

	                }
	         
	                @Override
					public void onNothingSelected(AdapterView<?> parent) {
	                   //TODO:
	                }
	        });
			
			lyFeedDetails.setVisibility(View.VISIBLE);
		}
		
	}
	/* --------------------------------------------------------------------- */
	
	
	
	/* Gestión de suscripciones -------------------------------------------- */

	public RssFeed getNewRssFeed() {
		return newRssFeed;
	}
	
	public String getNewRssCategoryName () {
		return etxNewCategory.getText().toString();
	}
	
	public int getNewRssFeedCategory () {
		return newRssFeedCategory;
	}
	/* --------------------------------------------------------------------- */

	
}
