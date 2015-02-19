/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Model;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author owner
 */
public interface IHotelDAO {
    List<Hotel> getAllHotels() throws Exception;
    
    void saveHotels(Hotel hotel) throws SQLException, Exception;
    
    void deleteHotels(String tableName, String whereField, Object whereValue) 
            throws SQLException, ClassNotFoundException;
    
    public Hotel getHotelById(String id) throws SQLException, ClassNotFoundException;
}
