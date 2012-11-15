package edu.upenn.mkse212;

/**
 * Simple "factory" class to instantiate key/value stores
 * 
 * @author zives
 * @author ahae
 */
 
public class KeyValueStoreFactory {
  /**
   * Different kinds of key/value stores we are allowed to instantiate.
   * 
   * BERKELEY = local BerkeleyDB storage
   * SIMPLEDB = remote Amazon SimpleDB storage
   *
   */
   
  public static enum STORETYPE {BERKELEY, SIMPLEDB};

  /**
   * Create a new key/value store
   * 
   * @param typ Type of storage system to create
   * @param dbName Name of the key/value store
   * @param userID User ID for authentication (not used by BerkeleyDB; needed for Amazon SimpleDB)
   * @param authKey Authentication key (not used by BerkeleyDB; needed for Amazon SimpleDB)
   * @param compress Indicates whether to try to compress string data (only supported by BerkeleyDB)
   * @return
   */
  public static IKeyValueStorage getKeyValueStore(STORETYPE typ, String dbName, String path, String userID, String authKey, boolean compress) {
    switch (typ) {
    case BERKELEY:
      return new BdbStorage(dbName, path, userID, authKey, compress);
    case SIMPLEDB:
      return new SimpleDBStorage(dbName, path, userID, authKey, compress);
    default:
      return null;
    }
  }
}
