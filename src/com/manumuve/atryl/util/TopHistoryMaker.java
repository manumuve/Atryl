/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;

/**
 * Clase del algoritmo encargado de generar la lista de noticias relevantes.
 * @author Manumuve
 * @version 0.2
 */
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
	
	/** Constantes para variar el algoritmo de relevantes */
	private static final int MAX_MINUTES_AGE = 60*24; // 24 horas
	private static final int MIN_KEYWORD_FREQUENCY = 4; // 3 repeticiones
	
	/**
	 * Constructor de clase. Inicializa las variables necesarias.
	 */
	public TopHistoryMaker () {
		dataSingleton = DataSingleton.getInstance();
		stories = new ArrayList<RssItem>();
		topStories = new ArrayList<RssItem>();
		todosLosTitulos = new StringBuilder();
		keyWords = new ArrayList<String>();
	}
	
	/**
	 * Algoritmo que construye la lista de noticias relevantes. Resumen de
	 * funcionamiento:
	 * 1) Construir una lista con las noticias publicadas hace menos de
	 *    MAX_MINUTES_AGE
	 * 2) Construir una lista con las palabras que se repiten al menos
	 *    MIN_KEYWORD_FREQUENCY veces en los títulos de las noticias
	 *    obtenidas en el paso anterior
	 * 3) Construir una lista con las noticias en cuyo título aparece una
	 *    de las palabras de la lista construida en el paso anterior.
	 * 4) Devolver la última lista construida.
	 * 
	 * @return ArrayList<RssItem> topStories (error = null)
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
							try {
								stringPubDate = rssItem.getPubDate();
								DateFormat formatter = new SimpleDateFormat(MyConstants.DATE_FORMAT, Locale.ENGLISH);
								datePubDate = formatter.parse(stringPubDate); // Posible excepción
							} catch (ParseException e) {
								// Captura de excepción
								e.printStackTrace();
								continue; // Descartar item actual y pasar a siguiente
							}
						}
						
						diffMSec = new Date().getTime() - datePubDate.getTime();
						diffMinutes = (int) (diffMSec / (1000*60));
						
						// Descartar por edad
						if (diffMinutes < MAX_MINUTES_AGE) {
							stories.add(rssItem);
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
	
	/**
	 * Método que elabora la lista de palabras clave.
	 * Una palabra clave es una palabra que se repite
	 * más de MIN_KEYWORD_FREQUENCY veces 
	 * @param input
	 */
	private void findKeyWords(String input) {

		input = input.replaceAll("[^0-9a-zA-Z\\s]+", ""); // borrar caracteres especiales
		input = input.replaceAll("\\b\\w{1,4}\\s?\\b", ""); // borrar menos de 4 caracteres
		String[] words = input.toLowerCase().split("\\s+");
		HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();

		for (String word : words) {
			Integer count = wordCounts.get(word);
			if (count == null) {
				count = 0;
			}

			if (count >= MIN_KEYWORD_FREQUENCY) { // relevante detectada
				if (keyWords.contains(word) == false) {
					keyWords.add(word);
					Log.d("Atryl", "Relevante: " + word);
				}
				continue;
			}

			wordCounts.put(word, count + 1);
		}
	}
}
