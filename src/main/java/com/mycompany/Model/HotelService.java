/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author owner
 */
public class HotelService {

    private IHotelDAO hotelDAO;
    private static IDB_GenericMySQL db;

    public HotelService() {
        db = new DB_GenericMySQL();
        hotelDAO = new HotelDAO((DB_GenericMySQL) db);

    }

    public final List<Hotel> getAllHotels() throws Exception {
        return hotelDAO.getAllHotels();
    }

    public final void deleteHotels(String tableName, String whereField,
            Object whereValue) throws SQLException, ClassNotFoundException {
        hotelDAO.deleteHotels(tableName, whereField, whereValue);
    }

    public final void saveHotel(Hotel hotel) throws SQLException, Exception {
        hotelDAO.saveHotels(hotel);
    }

    public final Hotel getHotelById(String id) throws SQLException, ClassNotFoundException {

        return hotelDAO.getHotelById(id);
    }

    public static void main(String[] args) throws Exception {
        HotelService hotelService = new HotelService();

//      Hotel hotel = new Hotel("Jessica's Hotel","","","","","");       
//      hotelService.save(hotel);
//      System.out.println(hotelService.getAllHotels());
//        System.out.println(hotelService.getHotelById("2"));
        
        String hotelName = "Holiday InnNew";
        String streetAddress = "Testing St";
        String city = "TestCIty";
        String state = "WI";
        String postalCode = "53220";
        String notes = "Test hotel created in DAO";
        
        Hotel hotel = new Hotel(hotelName, streetAddress, city, state,
                postalCode, notes);
        
//        hotelService.saveHotel(hotel);
         
//      hotelService.deleteHotel("hotel", "hotel_name", "Test");
        
        
        List<Hotel> hotels = hotelService.getAllHotels();
        for (int i = 0; i < hotels.size(); i++) {
            System.out.println(hotels.get(i));
        }
    }
}
