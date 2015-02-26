package com.mycompany.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jkramer26
 */
public class DB_GenericMySQL implements IDB_GenericMySQL {

    private Statement stmt;     //Variable for our statement object to execute sql statement
    private Connection conn;    //Connection variable needed for statement
    private ResultSet rs;
    private PreparedStatement pstmt;
    private ResultSetMetaData metaData;

    /**
     *
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Override
    public final void openConnection(String driverClassName, String url,
            String username, String password) throws ClassNotFoundException,
            SQLException {
        Class.forName(driverClassName);
        conn = DriverManager.getConnection(url, username, password);
    }

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    @Override
    public final List<Map<String, Object>> getAllRecords(String tableName)
            throws SQLException {

        //Creating a list of maps to store multiple records
        List<Map<String, Object>> records = new ArrayList<>();

        //Building the sql statement and concatanating the passed in table name
        String sql = "select * from " + tableName;

        try {
            //We are creating our statement object and needs a connection with create method
            stmt = conn.createStatement();

            /**
             * Result sets is a way to store and manipulate the records returned
             * from a SQL query.
             */
            //We are executing our sql statement and returning it in a result set
            rs = stmt.executeQuery(sql);
            
            //Getting meta information about table from result set
            metaData = rs.getMetaData();
            
            //getting the number of columns in the table 
            int colCount = metaData.getColumnCount();
            
            
            /**
             * Moving the cursor to the next row in the table. When there are no 
             * more rows a false will be returned
             */
            while (rs.next()) {
                //Creating a new map each loop to hold column data for a record
                Map<String, Object> record = new HashMap();
                
                //Looping through columns and putting the column name and value into a record
                for (int i = 0; i < colCount; i++) {
                    record.put(metaData.getColumnName(i), rs.getObject(i));
                }
                //Adding one record to our list of records
                records.add(record);
            }
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            try {
                //Close statement and connection
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                throw sqle;
            }
        }
        return records;
    }

    /**
     *
     * @param tableName
     * @param primaryKeyField
     * @param keyValue
     * @return
     * @throws SQLException
     */
    @Override
    public final Map getRecordById(String tableName, String primaryKeyField,
            Object keyValue) throws SQLException {
        
        //Creating a map to store columns and values of a single record
        final Map record = new HashMap();
        try {
            //We are creating our statement object and needs a connection with create method
            stmt = conn.createStatement();
            
            //String to help build sql statement with keyValue
            String sqlKeyValue;
            
            //If the keyValue is a string then add equals and quotes otherwise just an equals sign
            if (keyValue instanceof String) {
                sqlKeyValue = "= '" + keyValue + "'";
            } else {
                sqlKeyValue = "=" + keyValue;
            }
            
            //Building complete sql statement
            final String sql = "SELECT * FROM " + tableName + " WHERE "
                    + primaryKeyField + sqlKeyValue;
            //Executing sql statement and returning it to the result set
            rs = stmt.executeQuery(sql);
            
            //getting the metadata and column count from metadata
            metaData = rs.getMetaData();
            metaData.getColumnCount();
            final int fields = metaData.getColumnCount();
            
            //Looping through the columns of the returned record and storing the column name and value
            if (rs.next()) {
                for (int i = 1; i <= fields; i++) {
                    record.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                throw sqle;
            }
        }
        return record;
    }

    /**
     * 
     * @param tableName
     * @param whereField
     * @param whereValue
     * @return
     * @throws SQLException 
     */
    public final int deleteRecords(String tableName, String whereField,
            Object whereValue) throws SQLException {
        
        int recordsDeleted = 0;

        try {
            //Building a prepared statement with connection, tablename, and the primary key field
            pstmt = buildDeleteStatement(conn, tableName, whereField);

            //we are converting the statement to whatever type the whereValue is 
            if (whereField != null) {
                if (whereValue instanceof String) {
                    pstmt.setString(1, (String) whereValue);
                } else if (whereValue instanceof Integer) {
                    pstmt.setInt(1, ((Integer) whereValue).intValue());
                } else if (whereValue instanceof Long) {
                    pstmt.setLong(1, ((Long) whereValue).longValue());
                } else if (whereValue instanceof Double) {
                    pstmt.setDouble(1, ((Double) whereValue).doubleValue());
                } else if (whereValue instanceof java.sql.Date) {
                    pstmt.setDate(1, (java.sql.Date) whereValue);
                } else if (whereValue instanceof Boolean) {
                    pstmt.setBoolean(1, ((Boolean) whereValue).booleanValue());
                } else {
                    //if where value is none of the above types then we are setting it as an object
                    if (whereValue != null) {
                        pstmt.setObject(1, whereValue);
                    }
                }
            }
            //We are executing the prepared statemetn
            recordsDeleted = pstmt.executeUpdate();

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException sqle) {
                throw sqle;
            }
        }
        return recordsDeleted;
    }

    /**
     * 
     * @param conn_loc
     * @param tableName
     * @param whereField
     * @return
     * @throws SQLException 
     */
    private PreparedStatement buildDeleteStatement(Connection conn_loc,
            String tableName, String whereField) throws SQLException {

        final StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(tableName);

        if (whereField != null) {
            sql.append(" WHERE ");
            (sql.append(whereField)).append(" = ?");
        }

        final String finalSQL = sql.toString();
        return conn_loc.prepareStatement(finalSQL);
    }

    @Override
    public final int updateRecords(String tableName, List colDescriptors,
            List colValues, String whereField, Object whereValue)
            throws SQLException, Exception {

        int recordsUpdated = 0;
        try {
            pstmt = buildUpdateStatement(conn, tableName, colDescriptors,
                    whereField);

            final Iterator i = colValues.iterator();
            int index = 1;
            boolean doWhereValueFlag = false;
            Object obj = null;

            while (i.hasNext() || doWhereValueFlag) {
                if (!doWhereValueFlag) {
                    obj = i.next();
                }

                if (obj instanceof String) {
                    pstmt.setString(index++, (String) obj);
                } else if (obj instanceof Integer) {
                    pstmt.setInt(index++, ((Integer) obj).intValue());
                } else if (obj instanceof Long) {
                    pstmt.setLong(index++, ((Long) obj).longValue());
                } else if (obj instanceof Double) {
                    pstmt.setDouble(index++, ((Double) obj).doubleValue());
                } else if (obj instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(index++, (java.sql.Timestamp) obj);
                } else if (obj instanceof java.sql.Date) {
                    pstmt.setDate(index++, (java.sql.Date) obj);
                } else if (obj instanceof Boolean) {
                    pstmt.setBoolean(index++, ((Boolean) obj).booleanValue());
                } else {
                    if (obj != null) {
                        pstmt.setObject(index++, obj);
                    }
                }

                if (doWhereValueFlag) {
                    break;
                } // only allow loop to continue one time
                if (!i.hasNext()) {          // continue loop for whereValue
                    doWhereValueFlag = true;
                    obj = whereValue;
                }
            }

            recordsUpdated = pstmt.executeUpdate();

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException sqle) {
                throw sqle;
            }
        }
        return recordsUpdated;
    }

    private PreparedStatement buildUpdateStatement(Connection conn_loc,
            String tableName, List colDescriptors, String whereField)
            throws SQLException {

        StringBuffer sql = new StringBuffer("UPDATE ");
        (sql.append(tableName)).append(" SET ");
        final Iterator i = colDescriptors.iterator();
        while (i.hasNext()) {
            (sql.append((String) i.next())).append(" = ?, ");
        }
        sql = new StringBuffer((sql.toString()).substring(0,
                (sql.toString()).lastIndexOf(", ")));
        ((sql.append(" WHERE ")).append(whereField)).append(" = ?");
        final String finalSQL = sql.toString();
        return conn_loc.prepareStatement(finalSQL);
    }

    @Override
    public final int insertRecords(String tableName, List colDescriptors,
            List colValues) throws SQLException {

        int recordsInserted = 0;

        try {
            pstmt = buildInsertStatement(conn, tableName, colDescriptors);

            final Iterator i = colValues.iterator();
            int index = 1;
            while (i.hasNext()) {
                final Object obj = i.next();
                if (obj instanceof String) {
                    pstmt.setString(index++, (String) obj);
                } else if (obj instanceof Integer) {
                    pstmt.setInt(index++, ((Integer) obj).intValue());
                } else if (obj instanceof Long) {
                    pstmt.setLong(index++, ((Long) obj).longValue());
                } else if (obj instanceof Double) {
                    pstmt.setDouble(index++, ((Double) obj).doubleValue());
                } else if (obj instanceof java.sql.Date) {
                    pstmt.setDate(index++, (java.sql.Date) obj);
                } else if (obj instanceof Boolean) {
                    pstmt.setBoolean(index++, ((Boolean) obj).booleanValue());
                } else {
                    if (obj != null) {
                        pstmt.setObject(index++, obj);
                    }
                }
            }
            recordsInserted = pstmt.executeUpdate();

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException sqle) {
                throw sqle;
            }
        }
        return recordsInserted;
    }

    private PreparedStatement buildInsertStatement(Connection conn_loc,
            String tableName, List colDescriptors) throws SQLException {

        StringBuffer sql = new StringBuffer("INSERT INTO ");
        (sql.append(tableName)).append(" (");
        final Iterator i = colDescriptors.iterator();
        while (i.hasNext()) {
            (sql.append((String) i.next())).append(", ");
        }
        sql = new StringBuffer((sql.toString()).substring(0,
                (sql.toString()).lastIndexOf(", ")) + ") VALUES (");
        for (int j = 0; j < colDescriptors.size(); j++) {
            sql.append("?, ");
        }
        final String finalSQL = (sql.toString()).substring(0,
                (sql.toString()).lastIndexOf(", ")) + ")";
        return conn_loc.prepareStatement(finalSQL);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, Exception {
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/hoteldb";
        String userName = "root";
        String password = "admin";
        DB_GenericMySQL db = new DB_GenericMySQL();
        db.openConnection(driverClassName, url, userName, password);
        List<String> colDescriptors = new ArrayList<>();
        List<String> colValues = new ArrayList<>();

        colDescriptors.add("hotel_name");
        colDescriptors.add("street_address");
        colDescriptors.add("city");
        colDescriptors.add("state");
        colDescriptors.add("postal_code");
        colDescriptors.add("notes");

        colValues.add("New");
        colValues.add("254 Test");
        colValues.add("Waukesha");
        colValues.add("WI");
        colValues.add("99999");
        colValues.add("Test Hotel");

//        System.out.println(db.getRecordById("hotel", "hotel_id", "1"));
//        db.updateRecords("hotel", colDescriptors, colValues, "city", "Milwaukee");
//        db.insertRecords("hotel", colDescriptors, colValues);
//        Object obj = new Object();
//        obj = 1;
////        System.out.println(db.getRecordById("hotel", "hotel_id", obj));
////        System.out.println(db.getAllRecords("hotel"));
        db.deleteRecords("hotel", "hotel_id", 5);

        //Batch report
    }
}
