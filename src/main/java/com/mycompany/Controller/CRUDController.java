/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controller;

import com.mycompany.Model.Hotel;
import com.mycompany.Model.HotelService;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author owner
 */
//@WebServlet(name = "CRUDControl", urlPatterns = {"/CRUD"})
public class CRUDController extends HttpServlet {

    //this is the page information will be forwarded to
    private static final String RESULT_PAGE = "HotelPage.jsp";

    //defining what the key is within the query string
    private static String KEY = "action";
    private static String KEY2 = "value";
    //setting variables for the different type of operations 
    private static String INSERT_TYPE = "insert";
    private static String DELETE_TYPE = "delete";
    private static String UPDATE_TYPE = "update";
    private static String EDIT_TYPE = "edit";
    private static String VIEW_TYPE = "view";
    private static String ALL_TYPE = "all";
    String editForm = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        //setting a variable equal to the key variable defined above
        String type = request.getParameter(KEY);

        //we are checking to see which value was set to key in query string
        if (ALL_TYPE.equals(type)) {

            //Create new instance of model
            HotelService hs = new HotelService();

            //call method from model to calculate area with retrieved values
            //set the result of the calculation into the result variable
            List<Hotel> hotels = null;
            try {
                hotels = hs.getAllHotels();

                //set the attribute with the calculated result
                request.setAttribute("hotelList", hotels);

            } catch (Exception ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (EDIT_TYPE.equals(type)) {
            //retrieve values from form
            String idValue = request.getParameter(KEY2);
            String id = idValue;
            HotelService hs = new HotelService();
            Hotel h;
            List<Hotel> hotels = null;
            try {
                h = hs.getHotelById(id);
                request.setAttribute("hotelIdResult", h);
                request.setAttribute("theHotelId", id);
                request.setAttribute("hotelName", h.getHotelName());
                request.setAttribute("address", h.getStreetAddress());
                request.setAttribute("city", h.getCity());
                request.setAttribute("state", h.getState());
                request.setAttribute("postal", h.getPostalCode());
                request.setAttribute("notes", h.getNotes());

            } catch (SQLException ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String editForm = "not null";
            request.setAttribute("editForm", editForm);

        } else if (UPDATE_TYPE.equals(type)) {
            
            //retrieve values from form
            String hotelId = request.getParameter("hotelIdEdit");
            String name = request.getParameter("hotelName");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String postal = request.getParameter("postal");
            String notes = request.getParameter("notes");
            Integer hId = Integer.parseInt(hotelId);

            //Create new Hotel & set values
            Hotel updatedHotel = new Hotel(name, address, city, state, postal, notes);
            updatedHotel.setHotelId(hId);
            //Create new instance of model
            HotelService hs = new HotelService();

            //List to display all hotels
            List<Hotel> hotels = null;
            try {
                //Update the db with altered values
                hs.saveHotel(updatedHotel);

                //Display message that db was successfully updated
                String message = "Record was successfully updated";
                request.setAttribute("message", message);

                //Hide edit form
                String editForm = null;
                request.setAttribute("editForm", editForm);

                //Redisplay all hotels
                hotels = hs.getAllHotels();

                //set the attribute with the calculated result
                request.setAttribute("hotelList", hotels);
            } catch (Exception ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (DELETE_TYPE.equals(type)) {
            String tableName = "hotel";         //This is our table name
            String columnName = "hotel_id";     //This is our column name we want to identify record by

            //Retreive id from the form
            String idValue = request.getParameter(KEY2);
            //Create a new instance of model 
            HotelService hs = new HotelService();

            List<Hotel> hotels = null;
            try {
                hs.deleteHotels(tableName, columnName, idValue);

                //Display message that db was successfully updated
                String message = "Record was successfully updated";
                request.setAttribute("message", message);

                //Redisplay all hotels
                hotels = hs.getAllHotels();

                //set the attribute with the calculated result
                request.setAttribute("hotelList", hotels);

            } catch (SQLException ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (INSERT_TYPE.equals(type)) {
            //retrieve values from form
            String name = request.getParameter("hotelNameInsert");
            String address = request.getParameter("addressInsert");
            String city = request.getParameter("cityInsert");
            String state = request.getParameter("stateInsert");
            String postal = request.getParameter("postalInsert");
            String notes = request.getParameter("notesInsert");
            
            //Create new Hotel & set values
            Hotel updatedHotel = new Hotel(name, address, city, state, postal, notes);
            
            //Create new instance of model
            HotelService hs = new HotelService();

            //List to display all hotels
            List<Hotel> hotels = null;
            try {
                //Insert the new hotel into the db
                hs.saveHotel(updatedHotel);

                //Display message that db was successfully updated
                String message = "Record was successfully inserted";
                request.setAttribute("message", message);

                //Hide edit form
                String insertForm = null;
                request.setAttribute("insertForm", insertForm);

                //Redisplay all hotels
                hotels = hs.getAllHotels();

                //set the attribute with the calculated result
                request.setAttribute("hotelList", hotels);
            } catch (Exception ex) {
                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (VIEW_TYPE.equals(type)) {
            String insertForm = "not null";
            request.setAttribute("insertForm", insertForm);
        }

        //This is faster than requestdispatcher
        //response.sendRedirect(destination);
        RequestDispatcher view
                = request.getRequestDispatcher(RESULT_PAGE);
        //forwarding the request and response
        view.forward(request, response);

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        response.setContentType("text/html");

        //setting a variable equal to the key variable defined above
        String type = request.getParameter(KEY);

//        
//        private static String INSERT_TYPE = "insert";
//    private static String DELETE_TYPE = "delete";
//    private static String UPDATE_TYPE = "update";
//    private static String EDIT_TYPE = "edit";
//    private static String ALL_TYPE = "all";
        //we are checking to see which value was set to key in query string
        if (ALL_TYPE.equals(type)) {
            //retrieve values from form

            //Create new instance of model
            HotelService hs = new HotelService();
            List<Hotel> hotels;
            try {
                //call method to get all hotel records
                hotels = hs.getAllHotels();

            } catch (Exception ex) {
                Logger.getLogger(CRUDController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            //set the attribute with all the results
//            request.setAttribute("triArea", hotels);
        } //else if (INSERT_TYPE.equals(type)) {
        //retrieve values from form
//            String length = request.getParameter("length");
//            String width = request.getParameter("width");
//            
//            //If length or width is empty display error message else calculate the area
//            if (length.isEmpty() || width.isEmpty()) {
//                String resultError = "You cannot submit an empty value.";
//                request.setAttribute("recError", resultError);
//            } else {
//                //Create new instance of model
//                AreaCalculator ac = new AreaCalculator();
//
//                //call method from model to calculate area with retrieved values
//                //set the result of the calculation into the result variable
//                String result = ac.getRectangleArea(length, width);
//
//                //set the attribute with the calculated result
//                request.setAttribute("recArea", result);
//            }
//        } else if (UPDATE_TYPE.equals(type)) {
//            //retrieve values from form
//            String radius = request.getParameter("radius");
//
//            //Create new instance of model
//            AreaCalculator ac = new AreaCalculator();
//
//            //call method from model to calculate area with retrieved values
//            //set the result of the calculation into the result variable
//            String result = ac.getCircleArea(radius);
//
//            //set the attribute with the calculated result
//            request.setAttribute("circArea", result);
//        } else if (EDIT_TYPE.equals(type)) {
//            
//        } else if (DELETE_TYPE.equals(type)) {
//            
//        }

        //forwards the data to the result page 
        RequestDispatcher view
                = request.getRequestDispatcher(RESULT_PAGE);
        //forwarding the request and response
        view.forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
