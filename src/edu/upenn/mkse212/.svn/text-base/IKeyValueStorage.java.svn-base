package edu.upenn.mkse212;

import java.util.Set;

/**
 * Basic interface to key/value stores
 * 
 * @author zives
 *
 */
public interface IKeyValueStorage {
	/**
	 * Initialize storage system
	 * @param dbName
	 */
	public void init(String dbName, String path, String userID, String authKey);
	
	/**
	 * Get result set by key
	 * @param search
	 * @return
	 */
	public Set<String> get(String search);
	
	/**
	 * Test if search key has a match
	 * 
	 * @param search
	 * @return
	 */
	public boolean exists(String search);
	
	/**
	 * Put key/value pair
	 * @param keyword
	 * @param category
	 */
	public void put(String keyword, String value);
	
	/**
	 * Shut down storage system
	 */
	public void close();
	
	public void sync();
}
