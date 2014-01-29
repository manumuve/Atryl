package com.manumuve.atryl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;

public class RelevantHistoryMaker {

	private static DataSingleton dataSingleton;
	private ArrayList<RssItem> topStories;
	private RssItem rssItem;
	
	private String stringPubDate;
	private Date datePubDate;
	private long diffMSec;
	private int diffMinutes;
	
	private class ClassKeyWord {
		String word;
		int count;
		
		/** Un objeto es igual a otro si coincide su variable word */
		@Override
		public boolean equals(Object o) {
		  if (o instanceof ClassKeyWord) {
			ClassKeyWord p = (ClassKeyWord)o;
		    return this.word.equals(p.word);
		  } else {
		    return false;
		  }
		}
		
		@Override
		public int hashCode() {
		  return this.word.length();
		}
		
	}
	HashSet<ClassKeyWord> keyWords;
	
	/** Máx horas para ser considerado item destacado */
	private static final int MAX_MINUTES_AGE = 60*36;
	
	public RelevantHistoryMaker () {
		dataSingleton = DataSingleton.getInstance();
		topStories = new ArrayList<RssItem>();
		keyWords = new HashSet<ClassKeyWord>();
	}
	
	/** Algoritmo que construye la lista de noticias
	 * relevantes
	 * 
	 * @return
	 * 	ArrayList<RssItem> topStories
	 * 	null if error
	 */
	public ArrayList<RssItem> buildList () {
		
		if (dataSingleton == null)
			return null;
		
		//if (dataSingleton._isLoaded == false)
		//	return null;
		
		
		for (int i=0; i<dataSingleton.categories.size(); i++) {
			for (int j=0; j<dataSingleton.categories.get(i).getFeeds().size(); j++) {
				for (int k=0; k<dataSingleton.categories.get(i).getFeed(j).getItems().size(); k++) {
					rssItem = dataSingleton.categories.get(i).getFeed(j).getItem(k);

					if (rssItem != null) {
						datePubDate = rssItem.getDatePubDate();
						
						if (datePubDate == null) {
							/* Check time limit */
							try {
								stringPubDate = rssItem.getPubDate();
								DateFormat formatter = new SimpleDateFormat(MyConstants.DATE_FORMAT, Locale.ENGLISH);
								datePubDate = formatter.parse(stringPubDate); // Posible excepción
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue; // Descartar item actual y pasar a siguiente
							}
						}
						
						diffMSec = new Date().getTime() - datePubDate.getTime();
						diffMinutes = (int) (diffMSec / (1000*60));
						
						if (diffMinutes < MAX_MINUTES_AGE) {
							
							String words[] = rssItem.getTitle().split(" ");
							for (int z = 0; z<words.length; z++) {
								ClassKeyWord classKeyWord = new ClassKeyWord();
								classKeyWord.word = words[z];
								if (keyWords.contains(classKeyWord)) {
									
								}
							}
							
							topStories.add(rssItem);
						}
					}
				}

			}
		}
		
		return topStories;
	}
}