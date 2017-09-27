<%-- 
    Document   : JSPBillet
    Created on : 19-janv.-2016, 17:14:50
    Author     : adrie
--%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.HashMap"%>
<%@page import="Classes.*"%>
<%!
    HttpSession session;
    HashMap<String,Reservations> reservations = null;
    
    ResourceBundle res;
    
    String langue;
    String[] loc;
%>

<%
    session = request.getSession(true);
    langue = (String)session.getAttribute("langue");
    if(session.getAttribute("login")==null)
    {
        String redirectURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/index.html";
        response.sendRedirect(redirectURL);

    }
    else
    {
        reservations =(HashMap<String,Reservations>)session.getAttribute("reservation");
    }
    
    if(langue == null)
    {
        response.sendRedirect("Contexte.jsp");
        return;
    }
    
    loc = langue.split("_");
    
    res = ResourceBundle.getBundle("Bundles.TextesLoisirs", new Locale(loc[0],loc[1]));
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%out.print(res.getString("AchatBillet"));%></title>
    </head>
    <body>
        <h1><%out.print(res.getString("AchatBillet"));%></h1>
        
        <%
            if(session.getAttribute("erreur")!=null)
            {
                out.print(session.getAttribute("erreur"));
                session.setAttribute("erreur", null);
            }
        %>
        
        <div>
            <form method="POST" action="MagasinServlet">
                <input type="hidden" name="type" value="validationBillet"/>
                <label for ="nbrReservation"><%out.print(res.getString("NombreTickets"));%>:</label>
                <input type="number" name="nbrReservation" min="1" max="20"/>/10 euros p.p<br/>
                <label><%out.print(res.getString("DateReservation"));%>:</label>
                <input type="number" name="jourReservation"/>/
                <input type="number" name="moisReservation"/>/
                <input type="number" name="anneeReservation"/>
                
                <br/><button type="submit"/><%out.print(res.getString("Valider"));%></button>
            </form>
            <form method="POST" action="MagasinServlet">
                <input type="hidden" name="type" value="retour"/>
                <button type="submit"/><%out.print(res.getString("Retour"));%></button>
            </form>
        </div>
    </body>
</html>
