package com.mozzartbet.gameservice.parser;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Player;

public class JSoupParser {
	public void readPlayers(String pageUrl) {
		URL url;
		Document doc = null;
		try {
			url = new URL(pageUrl);
			doc = Jsoup.parse(url,3000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Elements rows = doc.select("table#roster tr");
		System.out.println("\n\n\nPlayers:   Position:  Height:  W:  Birth Date:  E:  College:  ");
		System.out.println("***********************************************************************");
		for(int i=0;i<rows.size();i++) {
			
			Elements cols= rows.get(i).select("td");
			for(int j=0;j<cols.size();j++) {
				if(j==5)
					continue;
				System.out.print(cols.get(j).text()+" ");
				
			}
			System.out.println("\n-------------------------------------------------------------------");
		}
		
	}
	
	public LinkedList<Player> returnListOfPlayers(String pageUrl){
			LinkedList<Player> listOfPlayers = new LinkedList<Player>();
			URL url;
			Document doc = null;
			try {
				url = new URL(pageUrl);
				doc = Jsoup.parse(url,3000);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			Elements rows = doc.select("table#roster tr");
			rows.remove(0);
			Elements th = rows.select("th");
			for(int i=0;i<rows.size();i++) {
				Elements cols=rows.get(i).select("td");
				Player player = new Player();
				//System.out.println(cols.get(0).text());
				player.setNumber(Integer.parseInt(th.get(i).text()));
				player.setName(cols.get(0).text());
				player.setPosition(cols.get(1).text());
				player.setHeight(cols.get(2).text());
				player.setWeight(cols.get(3).text());
				player.setBirthDate(cols.get(4).text());
				player.setNationality(cols.get(5).text());
				if(!cols.get(6).text().equals("R"))
					player.setExpirience(Integer.parseInt(cols.get(6).text()));
				else player.setExpirience(0);
				if(!cols.get(7).text().isEmpty())
					player.setCollege(cols.get(7).text());
				else player.setCollege("unkown");
				listOfPlayers.add(player);
				//System.out.println(player);
			}
			
		return listOfPlayers;
	}
	
	
	
}
