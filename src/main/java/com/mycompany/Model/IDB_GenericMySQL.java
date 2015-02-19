
package com.mycompany.Model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDB_GenericMySQL {
    int insertRecords(String tableName, List colDescriptors, List colValues) 
            throws SQLException ;
    
    List<Map<String, Object>> getAllRecords(String tableName) 
            throws SQLException;
    
    int updateRecords(String tableName, List colDescriptors, List colValues, 
            String whereField, Object whereValue)throws SQLException, Exception;
    
    Map getRecordById(String tableName, String primaryKeyField, Object keyValue) 
            throws SQLException;

    void openConnection(String driverClassName, String url, String username, 
            String password) throws ClassNotFoundException, SQLException;
    
}
