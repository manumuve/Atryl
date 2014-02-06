package com.manumuve.atryl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import android.util.Log;

import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;

public class TopHistoryMaker {

	private static DataSingleton dataSingleton;
	private ArrayList<RssItem> stories;
	private ArrayList<RssItem> topStories;
	private RssItem rssItem;
	
	private String stringPubDate;
	private Date datePubDate;
	private long diffMSec;
	private int diffMinutes;
	private StringBuilder todosLosTitulos;
	private ArrayList<String> keyWords;
	
	/** Máx horas para ser considerado item destacado */
	private static final int MAX_MINUTES_AGE = 60*24;
	
	public TopHistoryMaker () {
		dataSingleton = DataSingleton.getInstance();
		stories = new ArrayList<RssItem>();
		topStories = new ArrayList<RssItem>();
		todosLosTitulos = new StringBuilder();
		keyWords = new ArrayList<String>();
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
							stories.add(rssItem);
							//todosLosTitulos = todosLosTitulos + rssItem.getTitle().replaceAll("\\b\\w{1,4}\\s?\\b", "") + " ";
							todosLosTitulos
								.append(rssItem.getTitle())
								.append(" ");
						}
					}
				}
			}
		}
		
		findKeyWords (todosLosTitulos.toString());
		
		for (int i = 0; i<stories.size(); i++) {
			if (topStories.contains(stories.get(i)) == false) {
				
				String title = stories.get(i).getTitle().toLowerCase();
				title = title.replaceAll("[^0-9a-zA-Z\\s]+", ""); // borrar caracteres especiales

				for (int j = 0; j < keyWords.size(); j++) {
					if (title.contains(keyWords.get(j)) && topStories.contains(stories.get(i)) == false) {
						topStories.add(stories.get(i));
					}
				}
			}

		}
		
		return topStories;
	}
	
	
	private void findKeyWords (String input) {

			input = input.replaceAll("[^0-9a-zA-Z\\s]+", "");   // borrar caracteres especiales
			input = input.replaceAll("\\b\\w{1,4}\\s?\\b", ""); // borrar menos de 4 caracteres
			String[] words = input.toLowerCase().split("\\s+");
			HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
			
			for (String word : words) {
			    Integer count = wordCounts.get(word);
			    if (count == null) {
			        count = 0;
			    }
			    
			    if (count > 3) { // relevante detectada
			    	if (keyWords.contains(word) == false) {
			    		keyWords.add(word);
				    	Log.d("Atryl", "Relevante: " + word);
			    	}
			    	continue;
			    }
			    
			    wordCounts.put(word, count + 1);
			}
			
			//Log.d("Atryl", "Count: " + wordCounts);
						
//			Collection<Integer> c = wordCounts.values();
//			for (int i=0; i<5; i++) {
//				for (int j=0; j<wordCounts.size(); j++) {
//					
//				}
//			}
//			
//			Iterator<Entry<String, Integer>> iter = wordCounts.entrySet().iterator();
//			while (iter.hasNext()) {
//			    Entry<String, Integer> entry = iter.next();
//			    if (entry.getValue().equals(Collections.max(c))) {
//			        String key_you_look_for = entry.getKey();
//			        Log.d("Atryl", "Máx: " + key_you_look_for + " " + Collections.max(c).toString());
//			    }
//			}
			
			

	}
}
	
	
//	private void funcion (String input) {
//		String[] split = input.split(" ");
//		Map<String, Integer> counts = new HashMap<String,Integer>(split.length*(split.length-1)/2,1.0f);
//		int idx0 = 0;
//		for(int i=0; i<split.length-1; i++){
//			int splitIpos = input.indexOf(split[i],idx0);
//			int newPhraseLen = splitIpos-idx0+split[i].length();
//			String phrase = input.substring(idx0, idx0+newPhraseLen);
//			for(int j=i+1; j<split.length; j++){
//				newPhraseLen = phrase.length()+split[j].length()+1;
//				phrase=input.substring(idx0, idx0+newPhraseLen);
//				Integer count = counts.get(phrase);
//				if(count==null){
//					counts.put(phrase, 1);
//				} else {
//					counts.put(phrase, count+1);
//				}
//			}
//			idx0 = splitIpos+split[i].length()+1;
//		}
//
//		Map.Entry<String, Integer>[] entries = counts.entrySet().toArray(new Map.Entry[0]);
//		Arrays.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
//			@Override
//			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//				return o2.getValue().compareTo(o1.getValue());
//			}
//		});
//		int rank=1;
//		Log.d ("ATRYL", "Rank Freq Phrase");
//		for(Map.Entry<String,Integer> entry:entries){
//			int count = entry.getValue();
//			if(count>1){
//				Log.d ("ATRYL", rank++ + " " + count + " " + entry.getKey());
//				
//			}
//		}
//	}
	
	
//	public class CountPhrases {
//	    public void main(String[] arg){
//	        String input = "my name is john jane doe jane doe doe my name is jane doe doe my jane doe name is jane doe I go by the name of john joe jane doe is my name";
//
//	        String[] split = input.split(" ");
//	        Map<String, Integer> counts = new HashMap<String,Integer>(split.length*(split.length-1)/2,1.0f);
//	        int idx0 = 0;
//	        for(int i=0; i<split.length-1; i++){
//	            int splitIpos = input.indexOf(split[i],idx0);
//	            int newPhraseLen = splitIpos-idx0+split[i].length();
//	            String phrase = input.substring(idx0, idx0+newPhraseLen);
//	            for(int j=i+1; j<split.length; j++){
//	                newPhraseLen = phrase.length()+split[j].length()+1;
//	                phrase=input.substring(idx0, idx0+newPhraseLen);
//	                Integer count = counts.get(phrase);
//	                if(count==null){
//	                     counts.put(phrase, 1);
//	                } else {
//	                     counts.put(phrase, count+1);
//	                }
//	            }
//	            idx0 = splitIpos+split[i].length()+1;
//	        }
//
//	        Map.Entry<String, Integer>[] entries = counts.entrySet().toArray(new Map.Entry[0]);
//	        Arrays.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
//	            @Override
//	            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//	                return o2.getValue().compareTo(o1.getValue());
//	            }
//	        });
//	        int rank=1;
//	        System.out.println("Rank Freq Phrase");
//	        for(Map.Entry<String,Integer> entry:entries){
//	            int count = entry.getValue();
//	            if(count>1){
//	                System.out.printf("%4d %4d %s\n", rank++, count,entry.getKey());
//	            }
//	        }
//	    }
//	}