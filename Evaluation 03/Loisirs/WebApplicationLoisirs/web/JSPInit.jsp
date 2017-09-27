<%-- 
    Document   : JSPInit
    Created on : 19-janv.-2016, 10:22:50
    Author     : adrie
--%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Classes.*"%>
<%@taglib uri="/WEB-INF/tlds/Tag_library.tld" prefix="tl" %>
<%!
    HttpSession session;
    HashMap<Integer,Produits> caddy = null;
    HashMap<String,Reservations> reservations = null;
    String login;
    String langue;
    String[] loc;
    ResourceBundle res;
%>

<%
    login = request.getParameter("login");
    langue = (String)session.getAttribute("langue");
    session = request.getSession(true);
    if(session.getAttribute("login")==null)
    {
        if(login==null)
        {
            String redirectURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/index.html";
            response.sendRedirect(redirectURL);
        }
        else
        {
            session.setAttribute("login", login);
            caddy = new HashMap<Integer,Produits>();
            session.setAttribute("caddy", caddy);
            reservations = new HashMap<String,Reservations>();
            session.setAttribute("reservation",reservations);
        }
    }
    else
    {
        caddy = (HashMap<Integer,Produits>)session.getAttribute("caddy");
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
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%out.print(res.getString("Bonjour"));%> <%out.println(session.getAttribute("login"));%></h1>
        
        <tl:DateTag langue="<%=langue%>"/> 

        
        <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="achatBillet"/>
            <button type="submit"><%out.print(res.getString("AchatBillet"));%></button>
        </form>
        
        <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="achatProduit"/>
            <button type="submit"/><%out.print(res.getString("AchatProduit"));%></button>
        </form>
        
        <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="paiement"/>
            <button type="submit"/><%out.print(res.getString("Paiement"));%></button>
        </form>
        
         <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="deconnexion"/>
            <button type="submit"/><%out.print(res.getString("Deconnexion"));%></button>
        </form>
        <h3>
            <%
                if(session.getAttribute("erreur")!=null)
                {
                    out.print(session.getAttribute("erreur"));
                    session.setAttribute("erreur", null);
                }
            %>
        </h3>
    </body>
</html>
