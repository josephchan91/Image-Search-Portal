package edu.upenn.mkse212.dbpedia;

import edu.upenn.mkse212.KeyValueStoreFactory;

public class Settings {

  // The type of key/value store you are using. Initially set to BERKELEY;
  // will be changed to SIMPLEDB for the second milestone.
  public static final KeyValueStoreFactory.STORETYPE storeType 
    = KeyValueStoreFactory.STORETYPE.SIMPLEDB;
  
  public static final String pathToDatabase = "/home/mkse212/bdb/";
    
  // Dummy value for the first milestone; set to your Amazon Access
  // Key ID for the second milestone.
  public static final String accessKeyID = "AKIAJHGTF7UA5D2XKVTA";
  
  // Dummy value for the first milestone; set to your Secret Access Key
  // for the second milestone.  
  public static final String secretAccessKey = "aHtdyCu2GR//1EfFBvBBTob9jLa+XXYV8NgXnpP8";
  
  // For the second milestone, you may want to set this to true to save
  // bandwidth.
  public static final boolean compress = true;
  
  // Restrict the topics that should be indexed. For example, when this is
  // set to 'X', you should only index topics that start with an X.
  // Set this to "A" for the first milestone, and to "Ar" for the second.
  public static final String filter = "Ar";
  
  public static final String titleFileName = "labels_en.nt";

  public static final String imageFileName = "images_en.nt";	
  
}
