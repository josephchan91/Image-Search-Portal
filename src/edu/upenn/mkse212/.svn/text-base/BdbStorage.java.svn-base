package edu.upenn.mkse212;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;


/**
 * BerkeleyDB key/value storage class
 * @author zives
 *
 */
public class BdbStorage implements IKeyValueStorage {
	
	public static Environment myEnv = null;
	public static int dbCount = 0;
	public static boolean compress = false;
	Database myDb;
	
	public BdbStorage(boolean compress) {
		BdbStorage.compress = compress; 
	}
	
	public BdbStorage(String dbName, String path, String userID, String authKey, boolean compress) {
		init (dbName, path, userID, authKey);
		BdbStorage.compress = compress; 
	}

	/**
	 * Convert (possibly compressed) data from a Java string
	 * 
	 * @param content
	 * @return
	 * @throws IOException
	 */
	private DatabaseEntry getEntryFromString(String content) throws IOException {
		if (compress)
			return new DatabaseEntry(StringZipper.zipStringToBytes(content));
		else
			return new DatabaseEntry(content.getBytes("UTF-8"));
	}
	
	/**
	 * Convert (possibly compressed) data back to a String
	 * 
	 * @param content
	 * @return
	 * @throws IOException
	 */
	private String getStringFromEntry(DatabaseEntry content) throws IOException {
		if (compress)
	    	return StringZipper.unzipStringFromBytes(content.getData());
		else
			return new String(content.getData());
	}
	
	@Override
	public void init (String dbName, String path, String userID, String authKey) {
		synchronized (this) {
			if (myEnv == null) {
				// Instantiate an environment configuration object
		        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		
		        myEnvConfig.setReadOnly(false);
		
		        myEnvConfig.setAllowCreate(true);
		        
//		        myEnvConfig.setCacheSize((long) 1048*1048576L);
		        
		        myEnvConfig.setCachePercent(90);
		
		        (new File(path)).mkdir();

		        // Instantiate the Environment. This opens it and also possibly
		        // creates it.
		        myEnv = new Environment(new File(path), myEnvConfig);
			}
			dbCount++;
		}

     // Open the database. Create it if it does not already exist.
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        dbConfig.setDeferredWrite(true);
        dbConfig.setTransactional(false);
        myDb = myEnv.openDatabase(null, dbName, dbConfig); 
	}
	
	@Override
	public boolean exists(String search) {
		try {
		    // Create a pair of DatabaseEntry objects. theKey
		    // is used to perform the search. theData is used
		    // to store the data returned by the get() operation.
		    DatabaseEntry theKey = getEntryFromString(search);
		    DatabaseEntry theData = new DatabaseEntry();

		 // Perform the search
		    if (myDb.get(null, theKey, theData, LockMode.DEFAULT) ==
		    	OperationStatus.SUCCESS)
		    	return true;
		    else
		    	return false;
		} catch (Exception e) {
		    // Exception handling goes here
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Set<String> get(String search) {
		Set<String> ret = new HashSet<String>();
		try {
		    // Create a pair of DatabaseEntry objects. theKey
		    // is used to perform the search. theData is used
		    // to store the data returned by the get() operation.
		    DatabaseEntry theKey = getEntryFromString(search);
		    DatabaseEntry theData = new DatabaseEntry();

		 // Open a cursor using a database handle
		    Cursor cursor = myDb.openCursor(null, null);
		    
		 // Perform the search
		    OperationStatus retVal = cursor.getSearchKey(theKey, theData, LockMode.DEFAULT);
		    
		    while (retVal == OperationStatus.SUCCESS) {
		    	String foundKey = getStringFromEntry(theKey);
		        String foundData = getStringFromEntry(theData);

		        //System.out.println("Found record " + foundKey + "/" + foundData);
		    	if (!foundKey.equals(search))
		    		break;

		        ret.add(foundData);
		        retVal = cursor.getNextDup(theKey, theData, LockMode.DEFAULT);
		    }
		    cursor.close();
		} catch (Exception e) {
		    // Exception handling goes here
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void put(String keyword, String value) {
		try {
		    DatabaseEntry theKey = getEntryFromString(keyword);
		    DatabaseEntry theData = getEntryFromString(value);
		    
		    myDb.put(null, theKey, theData);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	@Override
	public void sync() {
		myDb.sync();
	}

	@Override
	public void close() {
		try {
			if (myDb != null) {
				myDb.sync();
				myDb.close();
			}

		} catch (DatabaseException dbe) {
			dbe.printStackTrace();
		} 

		synchronized (this) {
			dbCount--;
			if (myEnv != null && dbCount == 0) {
				try {
					myEnv.close();
					myEnv = null;
				} catch(DatabaseException dbe) {
					System.err.println("Error closing environment" + 
							dbe.toString());
				}
			}
		}
	}
}
