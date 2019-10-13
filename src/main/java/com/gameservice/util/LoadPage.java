package com.gameservice.util;

import org.jsoup.nodes.Document;

import com.gameservice.exception.UrlException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadPage {
	final String fileName;
	final String url;

	public static LoadPage parseUrl(String url) {
		return new LoadPage(null, url);
	}

	public static LoadPage parseFile(String fileName) {
		return new LoadPage(fileName, null);
	}

	public Document parse() throws UrlException {
		return fileName == null ? JsoupHelper.connectToLivePage(url) : JsoupHelper.connectToLocalPage(fileName);
	}
}
