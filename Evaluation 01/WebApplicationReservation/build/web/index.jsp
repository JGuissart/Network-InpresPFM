<%-- 
    Document   : index
    Created on : 11-oct.-2015, 0:43:30
    Author     : Adrien
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Connexion</title>
    </head>
    <body>
        <h1>Connexion</h1>
            <form method="post" action="FormConnexion">
            <p>Login: <input type="text" name="login" size="20"></p>
            <p>Mot de passe: <input type="text" name="mdp" size="20"></p>
            <p>Nouveau client ? <input type = "checkbox" name = "nouveauClient"></p>
            <p><input type="submit" value="se connecter"><br><br></p>
        </form>
            <%  
                if(session.getAttribute("erreur")!=null)
                {
            %>        
            <p><%=session.getAttribute("erreur")%></p>
              <%}%>
    </body>
</html>
