package com.ims.common.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Future;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class html2pdf {

	
	@SuppressWarnings("unused")
	public static void generatePdf(JsonObject data ,Handler<AsyncResult<JsonObject>> handler) throws Exception {
		System.out.println("generate pdf  Started");
		 
		File htmlFile = new File("");
		Document doc = Jsoup.parse(htmlFile,"UTF-8");
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.html);
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		handler.handle(Future.succeededFuture());
			
	}
	
	
	
}
