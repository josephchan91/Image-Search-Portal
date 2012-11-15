package edu.upenn.mkse212;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.simpledb.*;
import com.amazonaws.services.simpledb.model.*;
import com.amazonaws.auth.*;

/**
 * Amazon SimpleDB key/value storage class
 * @author zives
 * @author ahae
 *
 */
public class SimpleDBStorage implements IKeyValueStorage {
	
  AmazonSimpleDB db = null;
  boolean compress = false;
  String dbName;

  public SimpleDBStorage(String dbName, String path, String userID, String authKey, boolean compress) {
    init(dbName, path, userID, authKey);
    this.compress = compress;
  }

  @Override
  public void init(String dbNameArg, String path, String userID, String authKey) {
    db = new AmazonSimpleDBClient(new BasicAWSCredentials(userID, authKey));
    dbName = dbNameArg;

    try {
      ListDomainsResult listDomainsResult = db.listDomains();
      java.util.List<String> domainNameList  =  listDomainsResult.getDomainNames();

      if (domainNameList.contains(dbName))
        return;

      db.createDomain(new CreateDomainRequest(dbName));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Set<String> get(String search) {
    Set<String> result = new HashSet<String>();
    
    try {
      GetAttributesResult attribs = db.getAttributes(new GetAttributesRequest(dbName, search));
      java.util.List<Attribute> attributeList = attribs.getAttributes();

      // Get all matches
      for (Attribute a : attributeList) {
        if (a.getName().equals("match"))
       	  result.add(a.getValue());
      }

      return result;
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  @Override
  public boolean exists(String search) {
    return get(search).size() > 0;
  }

  @Override
  public void put(String keyword, String value) {
    try {
      ReplaceableAttribute attr0 = new ReplaceableAttribute("keyword", keyword, false);
      ReplaceableAttribute attr1 = new ReplaceableAttribute("match", value, false);
      List<ReplaceableAttribute> list = new ArrayList<ReplaceableAttribute>(); 
      list.add(attr0);
      list.add(attr1);

      db.putAttributes(new PutAttributesRequest(dbName, keyword, list, new UpdateCondition()));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void close() {
  }

  @Override
  public void sync() {
  }
}
