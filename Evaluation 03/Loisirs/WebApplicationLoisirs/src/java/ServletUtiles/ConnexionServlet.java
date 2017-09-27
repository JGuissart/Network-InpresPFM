/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletUtiles;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDOracle;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Queries.QueryInsert;
import Queries.QuerySelect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author adrie
 */
public class ConnexionServlet extends HttpServlet
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            try
            {
                String type = request.getParameter("type");
                String login = request.getParameter("login");
                String pwd = request.getParameter("pwd");
                
                bean = new BeanBDOracle("127.0.0.1", "1521", "xe", "loisirs", "loisirs");

                ResultSet rs = bean.Select(new QuerySelect("*","clients"));
                boolean bTrouve = false;
                switch(type)
                {
                    case "connexion":
                            
                        while(rs.next() && bTrouve == false)
                            if(login.equals(rs.getString(1)) == true)
                                    if(pwd.equals(rs.getString(2)) == true)
                                        bTrouve = true;

                        if(bTrouve == false)
                        {
                            out.println("Mauvais mot de passe");
                        }
                        else
                        {
                            out.println("OK");
                        }
                        
                        break;
                        
                    case "verifLogin" : 
                        while(rs.next())
                        if(login.equals(rs.getString(1)) == true)
                            bTrouve = true;
                         
                        if(bTrouve)
                        {
                            out.println("Cet identifiant existe déjà !"); 
                        }
                        else
                        {
                            out.println("OK");
                        }
                        
                        break;
                        
                    case "password" :
                            
                        QueryInsert QI = new QueryInsert("clients", "Login", "'" + login + "'");
                        QI.AddValue("Password", "'" + pwd + "'");
                        bean.Insert(QI);
                        bean.Commit();

                        out.println("OK");
                        
                        break;
                   
                }
            }
            catch(SQLException ex)
            {
                Logger.getLogger(ConnexionServlet.class.getName()).log(Level.SEVERE, null, ex);        
            }
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
            throws ServletException, IOException
    {
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
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

    private void Session(HttpServletRequest request, String login)
    {
        HttpSession session = request.getSession(true);
        
        String loginSession = (String)session.getAttribute("login");
        if(loginSession==null)
        {
            session.setAttribute("login",login);
            return;
        }
        
        if(loginSession.equals(login))
        {
            session.invalidate();
            session=request.getSession(true);
            session.setAttribute("login",login);
        }
        
    }
}
