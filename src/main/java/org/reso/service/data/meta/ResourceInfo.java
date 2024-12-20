package org.reso.service.data.meta;


import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.reso.service.data.common.CommonDataProcessing;
import org.reso.service.servlet.RESOservlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceInfo
{
   protected String tableName;
   protected String resourceName;
   protected String resourcesName;
   protected FullQualifiedName fqn;
   protected String primaryKeyName;

   protected static final Logger LOG = LoggerFactory.getLogger(ResourceInfo.class);

   /**
    * Accessors
    */

   public String getTableName()
   {
      return tableName;
   }

   public String getResourcesName()
   {
      return resourcesName;
   }

   public String getResourceName()
   {
      return resourceName;
   }

   public String getPrimaryKeyName()
   {
      return primaryKeyName;
   }

   public ArrayList<FieldInfo> getFieldList()
   {
      return null;
   }

   public Boolean useCustomDatasource() { return false; }

   public FullQualifiedName getFqn(String namespace)
   {
      if (this.fqn==null)
         this.fqn = new FullQualifiedName(namespace, getResourceName());

      return this.fqn;
   }

   public void findPrimaryKey(Connection connect) throws SQLException
   {
      String primaryKey = null;

      ResultSet pkColumns = connect.getMetaData().getPrimaryKeys(null, null, getTableName());

      while(pkColumns.next())
      {
         String pkColumnName = pkColumns.getString("COLUMN_NAME");
         Integer pkPosition = pkColumns.getInt("KEY_SEQ");
         LOG.debug(""+pkColumnName+" is the "+pkPosition+". column of the primary key of the table "+tableName);
         primaryKey = pkColumnName; //.toLowerCase();  // lowercase only needed for PostgreSQL
      }


    String[] splitKey = primaryKey.split("Numeric");
      if (splitKey.length>=1)
         primaryKey = splitKey[0];

      ArrayList<FieldInfo> fields = this.getFieldList();
      for (FieldInfo field : fields)
      {
         String fieldName = field.getFieldName();
         if (primaryKey.equals(fieldName))
            primaryKey = field.getODATAFieldName();
      }


      this.primaryKeyName = primaryKey;
   }


   public Entity getData(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates)
   {
      return null;
   }

   public EntityCollection getData(EdmEntitySet edmEntitySet, UriInfo uriInfo, boolean isCount) throws ODataApplicationException
   {
      return null;
   }
}
