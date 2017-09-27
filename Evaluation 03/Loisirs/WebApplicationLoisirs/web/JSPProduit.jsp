<%-- 
    Document   : JSPProduit
    Created on : 20-janv.-2016, 20:37:03
    Author     : adrie
--%>

<%@page import="Classes.Produits"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/Tag_library.tld" prefix="tl" %>
<%!
    HttpSession session;
    HashMap<Integer,Produits> caddy = null;
    
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
        caddy = (HashMap<Integer,Produits>)session.getAttribute("caddy");
    }
    
    if(langue == null)
    {
        response.sendRedirect("Contexte.jsp");
        return;
    }
    
    loc = langue.split("_");
    
    res = ResourceBundle.getBundle("Bundles.TextesLoisirs", new Locale(loc[0],loc[1]));
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%out.print(res.getString("AchatProduit"));%></title>
    </head>
    <body>
        <h1><%out.print(res.getString("AchatProduit"));%></h1>
        <tl:ProduitTag/>
    </body>
</html>
