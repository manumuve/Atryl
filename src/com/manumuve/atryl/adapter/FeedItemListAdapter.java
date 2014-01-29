package com.manumuve.atryl.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manumuve.atryl.R;
import com.manumuve.atryl.data.RssItem;



public class FeedItemListAdapter extends BaseAdapter {

	private Context context;
	private List<RssItem> items;
	
	public FeedItemListAdapter (Context context, List<RssItem> items) {
		this.context = context;
		this.items = items;
	}
	
	@Override
    public int getCount() {
        return this.items.size();
    }
 
    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create a new view into the list.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.feed_item_list_element, parent, false);
 
        // Set data into the view.
        TextView txTitle = (TextView) rowView.findViewById(R.id.txFeedItemListElementTitle);
        //TextView txDescription = (TextView) rowView.findViewById(R.id.txFeedItemListElementDescription);
        TextView txDescription = (TextView) rowView.findViewById(R.id.txFeedItemListElementDescription);
        
        if (this.items.get(position).getTitle().length() > 0) {
        	//txTitle.setText(Html.fromHtml(this.items.get(position).getTitle()));
            txTitle.setText(this.items.get(position).getTitle());
        }
        
        txDescription.setText(this.items.get(position).getPubDate());
 
        return rowView;
	}

}
