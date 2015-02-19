<%-- 
    Document   : HotelPage
    Created on : Feb 12, 2015, 5:08:16 PM
    Author     : owner

//way to say that we don't have data we need from controller
Ojbect objData = request.getAttribute("hotels");
if (objData == null) {
    response.sendRedirect("CRUDCOntroller);
}
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hotels</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    </head>
</head>
<body>
    <div class="container-fluid">
        <h1>Hotels</h1>
        
        <!-- ---------------- Hotel List ---------------- -->
        <c:choose>
            <c:when test="${hotelList == null}">
                <c:set var="tableVisible" value="display:none;" />
            </c:when>
            <c:otherwise>
                <c:set var="tableVisible" value="display:visible;" />
            </c:otherwise>
        </c:choose>
        <div class="table-responsive" style="${tableVisible}">
            <table class="table">
                <thead>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>City</th>
                <th>State</th>
                <th>ZipCode</th>
                <th>Notes</th>
                <th colspan="2"><button class="btn btn-info" type="button" id="hotelId" name="hotelId"  onclick="location.href = 'CRUD?action=view'">Insert Hotel Record</button></th>
            </thead>
                <tbody>
                    <c:forEach var="hotel" items="${hotelList}" >
                        <tr>
                            <td><a href="hotelController?id=${hotel.hotelId}">${hotel.hotelId}</a></td>
                            <td>${hotel.hotelName}</td>
                            <td>${hotel.streetAddress}</td>
                            <td>${hotel.city}</td>
                            <td>${hotel.state}</td>
                            <td>${hotel.postalCode}</td>
                            <td>${hotel.notes}</td>
                            <td><button class="btn btn-success" type="button" id="hotelId" name="hotelId"  onclick="location.href = 'CRUD?action=edit&value=${hotel.hotelId}'">Edit</button></td>
                            <td><button class="btn btn-danger" type="button" id="hotelId" name="hotelId"  onclick="location.href = 'CRUD?action=delete&value=${hotel.hotelId}'">Delete</button></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <!-- --------------------------------------- Edit Hotel Record Form --------------------------------------------------- -->
        <c:choose>
            <c:when test="${editForm == null}">
                <c:set var="formVisible" value="display:none;" />
            </c:when>
            <c:otherwise>
                <c:set var="formVisible" value="display:visible;" />
            </c:otherwise>
        </c:choose>
        <form class="form-horizontal" id="hotelForm" name="rectangleForm" method="POST" action="CRUD?action=update" style="${formVisible}">
            <fieldset>
                <legend>Edit an Existing Hotel</legend>
            </fieldset>

            <input class="form-control" id="hotelIdEdit" name="hotelIdEdit" type="text" placeholder="${theHotelId}" value="${theHotelId}" style="display:none;" />


            <div class="form-group">
                <label for="hotelName" class="col-sm-2 control-label">Hotel Name: </label>
                <div class="col-sm-3">
                    <input class="form-control" id="hotelName" name="hotelName" type="text" placeholder="${hotelName}" value="${hotelName}"/>
                </div>
            </div>
            <div class="form-group">
                <label for="address" class="col-sm-2 control-label">Hotel Address: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="address" id="address" placeholder="${address}" value="${address}"/>           
                </div>
            </div> 
            <div class="form-group">
                <label for="city" class="col-sm-2 control-label">Hotel City: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="city" id="city" placeholder="${city}" value="${city}"/>           
                </div>
            </div>
            <div class="form-group">
                <label for="state" class="col-sm-2 control-label">Hotel State: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="state" id="state" placeholder="${state}" value="${state}"/>           
                </div>
            </div>

            <div class="form-group">
                <label for="postal" class="col-sm-2 control-label">Hotel Zipcode: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="postal" id="postal"  value="${postal}"/>           
                </div>
            </div>
            <div class="form-group">
                <label for="notes" class="col-sm-2 control-label">Hotel Notes: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="notes" id="notes"  value="${notes}"/>           
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input class="btn btn-warning" type="submit" name="submit" id="submit" value="Update Hotel" />           
                    <button class="btn btn-primary" type="button" id="hotelId" name="hotelId"  onclick="location.href = 'CRUD?action=all'">Cancel</button>
                </div>
            </div> 
        </form>

        <!-- ------------------------------------------------ Insert Hotel Record Form ------------------------------------------------------ -->
        <c:choose>
            <c:when test="${insertForm == null}">
                <c:set var="insertFormVisible" value="display:none;" />
            </c:when>
            <c:otherwise>
                <c:set var="insertFormVisible" value="display:visible;" />
            </c:otherwise>
        </c:choose>
        <form class="form-horizontal" id="hotelForm" name="rectangleForm" method="POST" action="CRUD?action=insert" style="${insertFormVisible}">
            <fieldset>
                <legend>Insert a Hotel Record</legend>
            </fieldset>
            <div class="form-group">
                <label for="hotelNameInsert" class="col-sm-2 control-label">Hotel Name: </label>
                <div class="col-sm-3">
                    <input class="form-control" id="hotelNameInsert" name="hotelNameInsert" type="text" value=""/>
                </div>
            </div>
            <div class="form-group">
                <label for="addressInsert" class="col-sm-2 control-label">Hotel Address: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="addressInsert" id="addressInsert" value=""/>           
                </div>
            </div> 
            <div class="form-group">
                <label for="cityInsert" class="col-sm-2 control-label">Hotel City: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="cityInsert" id="cityInsert" value=""/>           
                </div>
            </div>
            <div class="form-group">
                <label for="stateInsert" class="col-sm-2 control-label">Hotel State: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="stateInsert" id="stateInsert" value=""/>           
                </div>
            </div>

            <div class="form-group">
                <label for="postalInsert" class="col-sm-2 control-label">Hotel Zipcode: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="postalInsert" id="postalInsert"  value=""/>           
                </div>
            </div>
            <div class="form-group">
                <label for="notesInsert" class="col-sm-2 control-label">Hotel Notes: </label>
                <div class="col-sm-3">
                    <input class="form-control" type="text" name="notesInsert" id="notesInsert" value=""/>           
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input class="btn btn-warning" type="submit" name="submit" id="submit" value="Insert Hotel Record" />           
                    <button class="btn btn-primary" type="button" id="hotelId" name="hotelId"  onclick="location.href = 'CRUD?action=all'">Cancel</button>
                </div>
            </div> 
        </form>
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>
