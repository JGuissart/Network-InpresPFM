<%-- 
    Document   : JSPPay
    Created on : 20-janv.-2016, 20:38:20
    Author     : adrie
--%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="Classes.*"%>
<%@taglib uri="/WEB-INF/tlds/Tag_library.tld" prefix="tl" %>
<%!
    HttpSession session;
    HashMap<Integer,Produits> caddy = null;
    HashMap<String,Reservations> reservations = null;
%>

<%
    session = request.getSession(true);
    if(session.getAttribute("login")==null)
    {
        String redirectURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/index.html";
        response.sendRedirect(redirectURL);

    }
    else
    {
        caddy = (HashMap<Integer,Produits>)session.getAttribute("caddy");
        reservations =(HashMap<String,Reservations>)session.getAttribute("reservation");
        
        if(reservations.isEmpty() && caddy.isEmpty())
        {
            String redirect = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/JSPInit.jsp";
            session.setAttribute("erreur", "Vous n'avez rien à acheter");
            response.sendRedirect(redirect);
        }
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    
        <h1>Paiement</h1>
        
        <%
            if(session.getAttribute("erreur")!=null)
            {
                out.print(session.getAttribute("erreur"));
                session.setAttribute("erreur", null);
            }
            /*<tl:CaddyTag>caddy;</tl:CaddyTag>*/
        %>
       
        <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="payer"/>
            <button type="submit"/>Continuer</button>
        </form>
        
        <form method="POST" action="MagasinServlet">
            <input type="hidden" name="type" value="retour"/>
            <button type="submit"/>Abandonner</button>
        </form>
    
</html>
