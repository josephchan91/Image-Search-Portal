package edu.upenn.mkse212.server;

import edu.upenn.mkse212.dbpedia.QueryImages;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import edu.upenn.mkse212.client.ImageService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
 
@SuppressWarnings("serial")
public class ImageServiceImpl extends RemoteServiceServlet implements ImageService {
	
   public ImageServiceImpl() {
   }

   public Set<String> fetchImageResults(String input) throws IllegalArgumentException {
     Set<String> results = new HashSet<String>();
     
     // TODO: call QueryImages appropriately
     QueryImages querier = new QueryImages();
     results = querier.query(input);

     // Get rid of dead images by testing for HTTP response code
     Set<String> validResults = new HashSet<String>();
     for (String imgURL : results) {
   	  try {
	    	  URL url = new URL(imgURL); 
			  HttpURLConnection conn =  (HttpURLConnection)url.openConnection(); 
			  conn.setRequestMethod("GET"); 
			  conn.connect() ; 
			  int code = conn.getResponseCode();
			  // If valid code, add to valid results set
			  if (code == 200) {
				  validResults.add(imgURL);
			  }
   	  } catch (Exception e) {
   	  }
     }
     
     return validResults;
   }
}
