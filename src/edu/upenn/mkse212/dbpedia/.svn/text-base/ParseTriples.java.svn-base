package edu.upenn.mkse212.dbpedia;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.upenn.mkse212.Triple;

public class ParseTriples {

  BufferedReader r;
        
  public ParseTriples(String docName) throws FileNotFoundException 
  {
    r = new BufferedReader(new FileReader(docName));		
  }
	
  public Triple getNextTriple() throws IOException 
  {
    String str = r.readLine();
    while ((str != null) && str.startsWith("#"))
      str = r.readLine();
		
    if (str == null)
      return null;

    int subjLAngle = 0;
    int subjRAngle = str.indexOf('>');
    int predLAngle = str.indexOf('<', subjRAngle + 1);
    int predRAngle = str.indexOf('>', predLAngle + 1);
    int objLAngle = str.indexOf('<', predRAngle + 1);
    int objRAngle = str.indexOf('>', objLAngle + 1);
			
    if (objLAngle == -1) {
      objLAngle = str.indexOf('\"', predRAngle + 1);
      objRAngle = str.indexOf('\"', objLAngle + 1); 
    }

    String subject = str.substring(subjLAngle + 1, subjRAngle);
    String predicate = str.substring(predLAngle + 1, predRAngle);
    String object = str.substring(objLAngle + 1, objRAngle);
			
    return new Triple(subject, predicate, object);
  }
	
  public void close() throws IOException 
  {
    r.close();
  }
}
