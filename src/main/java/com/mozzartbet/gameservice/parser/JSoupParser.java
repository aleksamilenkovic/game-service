package com.mozzartbet.gameservice.parser;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupParser {
	public void readPlayers(String pageUrl) {
		URL url;
		Document doc = null;
		try {
			url = new URL(pageUrl);
			doc = Jsoup.parse(url,3000);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element table = doc.select("table#roster").first();
		//Element tbody =table.select("tbody").first();
		Elements rows = table.getElementsByTag("tr"); 
		//System.out.println(tbody.text());
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
			//System.out.println(rows.get(i).text());
		}
		
	}
	
public LinkedList<Player> returnPlayers(){
		
		
		
		return null;
	}
	
}
