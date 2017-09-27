/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletUtiles;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDOracle;
import Classes.Produits;
import Classes.Reservations;
import Queries.QueryInsert;
import Queries.QuerySelect;
import Queries.QueryUpdate;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author adrie
 */
public class MagasinServlet extends HttpServlet implements HttpSessionListener
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
            switch(request.getParameter("type"))
            {
                case "achatBillet" :
                    response.sendRedirect("JSPBillet.jsp");
                    break;
                
                case "retour" :
                    response.sendRedirect("JSPInit.jsp");
                    break;
                            
                case "validationBillet":
                    validationBillet(request,response);
                    response.sendRedirect("JSPBillet.jsp");
                    break;

                case "achatProduit" :
                    response.sendRedirect("JSPProduit.jsp");
                    break;
                
                case "validationProduit" :
                    validationProduit(request,response);
                    response.sendRedirect("JSPProduit.jsp");
                    break;
                    
                case "paiement" :
                    response.sendRedirect("JSPPay.jsp");
                    break;
                    
                case "payer":
                    payer(request, response);
                    response.sendRedirect("JSPInit.jsp");
                    break;
                    
                case "deconnexion" :
                    request.getSession().invalidate();
                    response.sendRedirect("index.html");
                    break;
                    
                case "langue" :
                    request.getSession().setAttribute("langue",request.getParameter("local"));
                    response.sendRedirect("JSPInit.jsp");
                    break;
                    
                default : 
                    response.sendRedirect("JSPInit.jsp");
                    break;
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

    @Override
    public void sessionCreated(HttpSessionEvent se)
    {
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
        HashMap<String,Reservations> reservations = (HashMap<String,Reservations>)session.getAttribute("reservation");
        HashMap<Integer,Produits> caddy = (HashMap<Integer,Produits>)session.getAttribute("caddy");
        
        System.out.println(reservations.size());
        for(HashMap.Entry<String,Reservations> entry : reservations.entrySet())
        {
            try
            {
                /*ResultSet rs = getBean().Select("*","reservations","dateReservation=to_date('"+entry.getValue().getDateReservation()+"','DD/mm/YYYY')");
                if(rs.next())
                {  */ 
                    QueryUpdate qu = new QueryUpdate("reservations");
                    qu.AddValue("nbrReservation","nbrReservation-"+ entry.getValue().getNbrReservation());
                    qu.AddWhere("dateReservation="+"'"+entry.getValue().getDateReservation()+"'");
                    
                    getBean().Update(qu);
                
                    getBean().Commit();
                //}
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(MagasinServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(HashMap.Entry<Integer,Produits> entry : caddy.entrySet())
        {
            try
            {
                /*ResultSet rs = getBean().Select("*","produits","idProduit='"+ entry.getValue().getIdProduit()+"'");
                if(rs.next())
                {*/
                    QueryUpdate qu = new QueryUpdate("produits");
                    qu.AddValue("quantite", "quantite+" + entry.getValue().getQuantite());
                    qu.AddWhere("idProduit="+"'"+entry.getValue().getIdProduit()+"'");                  

                    getBean().Update(qu);
                    
                    getBean().Commit();
               // }
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(MagasinServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    }
    
    private void validationBillet(HttpServletRequest request, HttpServletResponse response)
    {
        int nombre, jour, mois, annee;
        String date;
        HashMap<String,Reservations> reservation = (HashMap<String,Reservations>)request.getSession().getAttribute("reservation");
        Reservations res;

        try
        {
            nombre = Integer.parseInt(request.getParameter("nbrReservation"));
            jour = Integer.parseInt(request.getParameter("jourReservation"));
            mois = Integer.parseInt(request.getParameter("moisReservation"));
            annee = Integer.parseInt(request.getParameter("anneeReservation"));

            if(jour<10)
                date = "0" + jour;
            else
                date=Integer.toString(jour);

            if(mois<10)
                date=date+"/0"+mois;
            else
                date=date+"/"+mois;
            
            date = date + "/" + annee;
  
            ResultSet rs = getBean().Select("*","reservations","dateReservation=to_date('"+date+"','DD/mm/YYYY')");

            if(rs.next())
            {   
                if(rs.getInt("nbrReservation")+nombre <=20)
                {
                    QueryUpdate qu = new QueryUpdate("reservations");
                    qu.AddValue("nbrReservation", Integer.toString(rs.getInt("nbrReservation")+nombre));
                    qu.AddWhere("dateReservation=to_date('"+date+"','DD/MM/YYYY')");
                    
                    getBean().Update(qu);
                    getBean().Commit();
                    
                    res = reservation.get(date);
                    if(res !=null)
                    {
                        res.setNbrReservation(res.getNbrReservation()+nombre);
                        reservation.put(date, res);
                    }
                    else
                    {
                        reservation.put(date, new Reservations(date, nombre));
                    }

                    request.getSession().setAttribute("erreur","Ajout au panier effectué");
                    request.getSession().setAttribute("reservations",reservation);
                        
                }
                else
                {
                    request.getSession().setAttribute("erreur", "Il ne reste plus assez de places disponible !");
                    return;
                }
            }
            else
            {
                if(nombre>20)
                {
                    request.getSession().setAttribute("erreur","Impossible de réserver plus de 20 places !");
                    return;
                }
                QueryInsert qi = new QueryInsert("reservations");
                qi.AddValue("dateReservation","to_date('"+date+"','DD/MM/YYYY')");
                qi.AddValue("nbrReservation", "'"+Integer.toString(nombre)+"'");

                getBean().Insert(qi);

                getBean().Commit();
                
                res = reservation.get(date);
                
                if(res != null)
                {
                    res.setNbrReservation(res.getNbrReservation()+nombre);
                    reservation.put(date, res);
                }
                else
                {
                    reservation.put(date, new Reservations(date, nombre));
                }
                request.getSession().setAttribute("erreur","Ajout au panier effectué");
                request.getSession().setAttribute("reservation",reservation);
            }  
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            request.getSession().setAttribute("erreur","Erreur dans les données insérées.");
        }
    }
    
    private void validationProduit(HttpServletRequest request, HttpServletResponse response)
    {
        HashMap<Integer,Produits> caddy = (HashMap<Integer,Produits>)request.getSession().getAttribute("caddy");
        Produits prod;
        
        try
        {
            QuerySelect qs = new QuerySelect();
            qs.AddSelect("*");
            qs.AddFrom("produits");
            qs.AddWhere("idProduit='"+request.getParameter("idProduit")+"'");

            ResultSet rs = getBean().Select(qs);
            if(rs.next())
            {
                if(Integer.parseInt(request.getParameter("nbrProduit")) > 0)
                {
                    QueryUpdate qu = new QueryUpdate("produits");
                    qu.AddValue("quantite","quantite-" + request.getParameter("nbrProduit"));
                    qu.AddWhere("idProduit="+"'"+request.getParameter("idProduit")+"'"); 
                    
                    getBean().Update(qu);
                    
                    getBean().Commit();
                    
                    prod = caddy.get(Integer.parseInt(request.getParameter("idProduit")));
                    
                    if(prod != null)
                    {   
                        prod.setQuantite(prod.getQuantite()+Integer.parseInt(request.getParameter("nbrProduit")));
                        caddy.put(Integer.parseInt(request.getParameter("idProduit")), prod);
                    }
                    else
                    {
                        caddy.put(Integer.parseInt(request.getParameter("idProduit")), new Produits(rs.getInt("idProduit"),rs.getString("nom"),rs.getString("description"),Integer.parseInt(request.getParameter("nbrProduit")),rs.getFloat("prix")));
                    }


                    request.getSession().setAttribute("erreur","Ajout au panier effectué");
                    request.getSession().setAttribute("caddy",caddy);
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            request.getSession().setAttribute("erreur","Erreur dans les données insérées.");
        }
        
    }
    synchronized private void payer(HttpServletRequest request, HttpServletResponse response)
    {
        HashMap<String,Reservations> reservations = (HashMap<String,Reservations>)request.getSession().getAttribute("reservation");
        HashMap<Integer,Produits> caddy = (HashMap<Integer,Produits>) request.getSession().getAttribute("caddy");
        String login = (String)request.getSession().getAttribute("login");
        
        if(login==null)
        {
            request.getSession().setAttribute("erreur", "Erreur d'identification");
            return;
        }
        
        if(reservations.isEmpty() && caddy.isEmpty())
        {
            request.getSession().setAttribute("erreur","Il n'y a rien dans vos paniers");
            return;
           
        }
        
        try
        {
            QuerySelect qs = new QuerySelect();
            qs.AddFrom("dual");
            qs.AddSelect("sequenceCommande.NEXTVAL");
            ResultSet rs = bean.Select(qs);
            
            if(rs.next())
            {
                int idCommande = rs.getInt(1);
                QueryInsert qi = new QueryInsert("commande");
                qi.AddValue("idCommande","'"+idCommande+"'");
                qi.AddValue("dateCommande","CURRENT_DATE");
                qi.AddValue("login","'"+login+"'");
                
                getBean().Insert(qi);
                getBean().Commit();
                
                for(HashMap.Entry<String,Reservations> entry : reservations.entrySet())
                {
                    QueryInsert qiRes = new QueryInsert("commande_res");
                    qiRes.AddValue("idCommande","'"+idCommande+"'");
                    qiRes.AddValue("dateReservation","to_date('"+entry.getValue().getDateReservation()+"','DD/MM/YYYY')");
                    qiRes.AddValue("quantite","'"+entry.getValue().getNbrReservation()+"'");
                    
                    getBean().Insert(qiRes);
                    
                    getBean().Commit();
                }
                
                for(HashMap.Entry<Integer,Produits>entry : caddy.entrySet())
                {
                    QueryInsert qiProd = new QueryInsert("commande_prod");
                    qiProd.AddValue("idCommande", "'"+idCommande+"'");
                    qiProd.AddValue("idProduit", "'"+entry.getValue().getIdProduit()+"'");
                    qiProd.AddValue("quantite", "'"+entry.getValue().getQuantite()+"'");
                    
                    getBean().Insert(qiProd);
                    
                    getBean().Commit();
                }
                request.getSession().setAttribute("reservation", new HashMap<String, Reservations>());
                request.getSession().setAttribute("caddy", new HashMap<Integer, Produits>());
                request.getSession().setAttribute("erreur","Paiement effectué");
            }
        }
        catch(Exception ex)
        {
            request.getSession().setAttribute("erreur","Erreur du paiement");
            return;
        }
    }
    
    private BeanBDAccess getBean() 
    {
        if(bean == null)
        {
            try
            {
                bean = new BeanBDOracle("127.0.0.1", "1521", "xe", "loisirs", "loisirs");
            } catch (SQLException ex)
            {
                Logger.getLogger(MagasinServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return bean;
    }
}
