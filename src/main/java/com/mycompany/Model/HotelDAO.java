/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  NEED TO MAKE THIS MORE FLEXIBLE BY USING GENERIC DB STRAT OBJECT
 * @author jkramer26
 */
public class HotelDAO implements IHotelDAO {
    //could use factory pattern for this
    private static final String FIND_ALL_HOTELS = "hotel";
    //THIS VARIABLE SHOULD BE REMOVED FOR STRAT OBJECT
    private DB_GenericMySQL db;
    //THIS VARIABLES SHOULD BE PUT IN CONTEXT PARAM IN XML FILE
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/hoteldb";
    private String userName = "root";
    private String password = "admin";

    public HotelDAO(DB_GenericMySQL db) {
        this.db = db;
    }

    private void openConnection() throws ClassNotFoundException, SQLException {
        try {
            db.openConnection(driverClassName, url, userName, password);
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public List<Hotel> getAllHotels() throws Exception {

        List<Map<String, Object>> rawData = new ArrayList<>();
        List<Hotel> records = new ArrayList<>();
        Hotel hotel = null; 
        try {
            openConnection();
            rawData = db.getAllRecords(FIND_ALL_HOTELS);
                        
            

            for (Map m : rawData) {
                hotel = new Hotel();
                
                String id = m.get("hotel_id").toString();
                hotel.setHotelId(new Integer(id));
                
                String hotelName = m.get("hotel_name").toString();
                hotel.setHotelName(hotelName);
                
                String streetAddress = m.get("street_address").toString();
                hotel.setStreetAddress(streetAddress);
                
                String city = m.get("city").toString();
                hotel.setCity(city);
                
                String state = m.get("state").toString();
                hotel.setState(state);
                
                String postalCode = m.get("postal_code").toString();
                hotel.setPostalCode(postalCode);
                
                String notes = m.get("notes").toString();
                hotel.setNotes(notes);
                
                records.add(hotel);
            }
        } catch (Exception e) {
            throw e;
        }
        return records;
    }

    
    public Hotel getHotelById(String id) throws SQLException, ClassNotFoundException {
        openConnection();
        
        Map record = new HashMap();
        try {
            record = db.getRecordById("hotel", "hotel_id", new Long(id));
        } catch (SQLException e)  {
            throw new SQLException();
        }
        
        Hotel hotel = new Hotel();
        hotel.setHotelId(new Integer(record.get("hotel_id").toString()));
        hotel.setHotelName(record.get("hotel_name").toString());
        hotel.setStreetAddress(record.get("street_address").toString());
        hotel.setCity(record.get("city").toString());
        hotel.setState(record.get("state").toString());
        hotel.setPostalCode(record.get("postal_code").toString());
        hotel.setNotes(record.get("notes").toString());
        
        return hotel;
    }

    @Override
    public void deleteHotels(String tableName, String whereField,
            Object whereValue) throws SQLException, ClassNotFoundException {
        openConnection();
        
        try {   
            db.deleteRecords(tableName, whereField, whereValue);
        } catch (SQLException sqle) {
            throw sqle;

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveHotels(Hotel hotel) throws SQLException, Exception {
        openConnection();
        
        String tableName = "hotel";
        
        List<String> colDescriptors = new ArrayList<>();
        colDescriptors.add("hotel_name");
        colDescriptors.add("street_address");
        colDescriptors.add("city");
        colDescriptors.add("state");
        colDescriptors.add("postal_code");
        colDescriptors.add("notes");

        List<String> colValues = new ArrayList<>();
        colValues.add(hotel.getHotelName());
        colValues.add(hotel.getStreetAddress());
        colValues.add(hotel.getCity());
        colValues.add(hotel.getState());
        colValues.add(hotel.getPostalCode());
        colValues.add(hotel.getNotes());
        
        //
        try {
            //This is either updating or creating new hotels depending on if there is a id already
            if (hotel.getHotelId() == null || hotel.getHotelId() == 0) {
                db.insertRecords(tableName, colDescriptors, colValues);
            } else {
                db.updateRecords(
                        tableName, colDescriptors,
                        colValues, "hotel_id", hotel.getHotelId());
            }
        } catch (SQLException sqle) {
            throw sqle;

        } catch (Exception e) {
            throw e;
        }

    }
    
    //Insert a record by id
//    public void insertHotelWithId(Hotel hotel) throws SQLException, Exception {
//        openConnection();
//        
//        String tableName = "hotel";
//
//        List<String> colDescriptors = new ArrayList<>();
//        colDescriptors.add("hotel_id");
//        colDescriptors.add("hotel_name");
//        colDescriptors.add("street_address");
//        colDescriptors.add("city");
//        colDescriptors.add("state");
//        colDescriptors.add("postal_code");
//        colDescriptors.add("notes");
//
//        List<String> colValues = new ArrayList<>();
////        colValues.add(new Integer(hotel.getHotelId()));
//        colValues.add(hotel.getHotelName());
//        colValues.add(hotel.getStreetAddress());
//        colValues.add(hotel.getCity());
//        colValues.add(hotel.getState());
//        colValues.add(hotel.getPostalCode());
//        colValues.add(hotel.getNotes());
//        
//        //
//        try {
//            
//            db.insertRecords(tableName, colDescriptors, colValues);
//            
//        } catch (SQLException sqle) {
//            throw sqle;
//
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    
    public static void main(String[] args) throws Exception {
//        HotelDAO db = new HotelDAO();
//        Class.forName(db.driverClassName);
//        db.conn = DriverManager.getConnection(db.url, db.userName, db.password);
        
        String hotelName = "The Most Updated";
        String streetAddress = "Testing St";
        String city = "TestCIty";
        String state = "WI";
        String postalCode = "53220";
        String notes = "Test hotel created in DAO";
        HotelDAO dao = new HotelDAO(new DB_GenericMySQL());
        Hotel hotel = new Hotel(hotelName, streetAddress, city, state,
                postalCode, notes);
        hotel.setHotelId(8);
//        hotel.setHotelId(7);
//        dao.openConnection();
//        dao.saveHotels(hotel);
//        System.out.println(hotel.getHotelId());
//        System.out.println(dao.getAllHotels());
//        dao.deleteHotels("hotel", "hotel_name", "Test");
//        dao.deleteHotel("hotel", "hotel_name", "Hotel California");
//        for (Hotel hotel : records) {
//            System.out.println(hotel);
//        }
//        System.out.println(dao.getAllHotels());
        System.out.println(dao.getHotelById("1"));
    }
}
