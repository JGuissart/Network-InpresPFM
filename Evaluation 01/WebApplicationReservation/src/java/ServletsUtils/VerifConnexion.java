/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletsUtils;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDOracle;
import Queries.QueryInsert;
import Queries.QuerySelect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Julien
 */
public class VerifConnexion extends HttpServlet
{
    private BeanBDAccess bean;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            HttpSession session = request.getSession(true);
        
            String login = request.getParameter("login");
            String mdp = request.getParameter("mdp");
            
            bean = new BeanBDOracle("127.0.0.1", "1521", "orcl", "BD_TRAFIC", "azerty123"); // MDP ==> Adrien = trafic ||| Julien = azerty123
            
            ResultSet rs = bean.Select(new QuerySelect("*", "users"));
            if(request.getParameter("nouveauClient") != null)
            {
                boolean bTrouve = false;
                while(rs.next())
                    if(login.equals(rs.getString(1)) == true)
                            bTrouve = true;
                
                if(bTrouve)
                {
                        session.setAttribute("erreur", "Ce client existe déjà");
                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/WebApplicationReservation");
                }
                else
                {
                    out.println("<p> Insertion </p>" );
                    QueryInsert QI = new QueryInsert("users", "Login", "'" + login + "'");
                    QI.AddValue("Password", "'" + mdp + "'");
                    bean.Insert(QI);
                    bean.Commit();
                }
            }
            else
            {   
                boolean bTrouve = false;
                while(rs.next() && bTrouve == false)
                    if(login.equals(rs.getString(1)) == true)
                            if(mdp.equals(rs.getString(2)) == true)
                                bTrouve = true;
                
                if(bTrouve == false)
                {
                    session.setAttribute("erreur", "Identification incorrecte");
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/WebApplicationReservation");
                }
            }
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Réservation</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Réservation d'un emplacement</h1>");
            rs = bean.Select(new QuerySelect("Ville", "destination"));
            out.println("<form method=\"post\" action=\"FormConnexion\">");
            out.println("<p>Identifiant du container : <input type=\"text\" name=\"identifiant\" size=\"20\" required></p>");
            out.println("<p>Ville de destination : ");
            out.println("<SELECT name=\"ville\">");
            while(rs.next())
            {
                out.println("<option value=\"" + rs.getString(1) + "\">" + rs.getString(1) + "</option>" );
            }
            out.println("</SELECT>");
            out.println("<p>Date d'arrivée: <input type=\"text\" name=\"date\" size=\"20\" required></p>");
            out.println("<p><input type=\"submit\" value=\"Réserver\"><br><br></p>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(VerifConnexion.class.getName()).log(Level.SEVERE, null, ex);
        }
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
