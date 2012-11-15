package edu.upenn.mkse212.dbpedia;

import java.util.Set;
import edu.upenn.mkse212.IKeyValueStorage;
import edu.upenn.mkse212.KeyValueStoreFactory;
import edu.upenn.mkse212.KeyValueStoreFactory.STORETYPE;
import edu.upenn.mkse212.PorterStemmer;

import java.util.Iterator;
import java.util.HashSet;

public class QueryImages {
  IKeyValueStorage imageStore;
  IKeyValueStorage titleStore;
  STORETYPE typ;
  String path;
  String userID;
  String authKey;
  boolean compress;
	
  public QueryImages() 
  {
	typ = Settings.storeType;
	path = Settings.pathToDatabase;
	userID = Settings.accessKeyID;
	authKey = Settings.secretAccessKey;
	compress = Settings.compress;
	  
    // TODO: Open the databases here
	imageStore = KeyValueStoreFactory.getKeyValueStore(typ, "images", path, userID, authKey, compress);
	titleStore = KeyValueStoreFactory.getKeyValueStore(typ, "terms", path, userID, authKey, compress);
  }
	
  public Set<String> query(String word)
  {
    // TODO: Return the set of URLs that match the given word,
    //       or an empty set if there are no matches
	HashSet<String> imgURLs = new HashSet<String>();
	// Parse over all categories returned by searching for a term
	for(String category : titleStore.get(PorterStemmer.stem(word.toLowerCase())))
	{
		// For each category, add to the return set all image URLs that match
		for(String imgURL : imageStore.get(category))
		{
			imgURLs.add(imgURL);
		}
	}
    return imgURLs;
  }
        
  public void close()
  {
    // TODO: Close the databases
	imageStore.close();
	titleStore.close();
  }
	
  public static void main(String args[]) 
  {
    // TODO: Add your own name and SEAS login here
    System.out.println("*** Author: Joseph Chan (joch)");
    QueryImages myQuery = new QueryImages();

    for (int i=0; i<args.length; i++) {
      System.out.println(args[i]+":");
      Set<String> result = myQuery.query(args[i]);
      Iterator<String> iter = result.iterator();
      while (iter.hasNext()) 
        System.out.println("  - "+iter.next());
    }
    
    myQuery.close();
  }
}
