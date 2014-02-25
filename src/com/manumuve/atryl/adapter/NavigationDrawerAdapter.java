/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssCategory;
import com.manumuve.atryl.data.RssFeed;


/**
 * Adapter class para rellenar una lista expandible de categorías
 * y sus feeds.
 * Los datos se obtienen desde una estructura Singleton, el adaptador
 * es completamente independiente.
 * Se intenta implementar el adaptador EFICIENTEMENTE
 * @see http://www.jmanzano.es/blog/?p=166
 */
public class NavigationDrawerAdapter extends BaseExpandableListAdapter {
	
	/** Datos en Singleton */
	DataSingleton dataSingleton;
	
	/** Keeps reference to avoid future findViewById() */
	private ElementsViewHolder viewHolder;

	/**
	 * Constructor estándar.
	 * @param categories
	 */
	public NavigationDrawerAdapter (List<RssCategory> categories) {
		//super(context, textViewResourceId, elements);
		dataSingleton = DataSingleton.getInstance();
	}
	
	/** Devuelve la vista de un hijo
	 * (un feed).
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		viewHolder = new ElementsViewHolder();
		
		/* Si la vista no está creada, inflarla, asignar textviews y establecer objeto etiqueta */
		if (convertView == null) {
            LayoutInflater li = (LayoutInflater) dataSingleton.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.navigation_feed_element, null);
            
            viewHolder.txFeedTitle = (TextView) convertView.findViewById(R.id.txLeftDrawerFeedTitle);
			viewHolder.txFeedSubtitle = (TextView) convertView.findViewById(R.id.txLeftDrawerFeedSubtitle);
			
			convertView.setTag(viewHolder);
        }
		/* Si la vista ya existe, recuperar objeto etiqueta */
		else {
			viewHolder = (ElementsViewHolder) convertView.getTag();
		}
		
		/* Si el feed existe, asignar valores de texto a textviews */
		RssFeed feed = dataSingleton.categories.get(groupPosition).getFeed(childPosition);
		if (feed != null) {
			viewHolder.txFeedTitle.setText(feed.getTitle());
			viewHolder.txFeedSubtitle.setText(feed.getDescription());
		}
		
		/* TODO: se adecua la imagen dependiendo del hijo a inflar (más adelante) */
		
		return convertView;
	}
	
	/**
	 * Devuelve la vista de un padre
	 * (una categoría).
	 */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    	viewHolder = new ElementsViewHolder();
    	
    	/* Si la vista no está creada, inflarla, asignar textviews y establecer objeto etiqueta */
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) dataSingleton.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.navigation_category_element, null);
            
            viewHolder.txCategoryName = (TextView) convertView.findViewById(R.id.txLeftDrawerCategoryName);
            
            convertView.setTag(viewHolder);
        }
        /* Si la vista ya existe, recuperar objeto etiqueta */
        else {
        	viewHolder = (ElementsViewHolder) convertView.getTag();
        }
        
        /* Si la categoría existe, asignar valores de texto a textviews */
        RssCategory category = dataSingleton.categories.get(groupPosition);
        if (category != null) {
        	viewHolder.txCategoryName.setText(category.getName());
        }
        
        /* TODO: cualquier tipo de modificación y personalización (más adelante) */
        
        return convertView;
    }

	@Override
	public RssFeed getChild(int groupPosition, int childPosition) {
		return dataSingleton.categories.get(groupPosition).getFeed(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return dataSingleton.categories.get(groupPosition).getFeedCount();
    }

	@Override
	public RssCategory getGroup(int groupPosition) {
		return dataSingleton.categories.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		int i = 0;
        try {
        	i = dataSingleton.categories.size();
        } catch (Exception e) {
        }
 
        return i;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Vistas que va a contener cada fila
	 * @author Manu
	 */
	static class ElementsViewHolder {
		TextView txCategoryName;
		TextView txFeedTitle;
		TextView txFeedSubtitle;
	}
}
