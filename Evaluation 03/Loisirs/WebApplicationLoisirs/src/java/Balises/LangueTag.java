/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balises;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDOracle;
import Queries.QuerySelect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author adrie
 */
public class LangueTag extends SimpleTagSupport
{
    
    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException
    {
        JspWriter out = getJspContext().getOut();
        
        try
        {
            ResultSet rs;
            try
            {
                // TODO: insert code to write html before writing the body content.
                // e.g.:
                //
                // out.println("<strong>" + attribute_1 + "</strong>");
                // out.println("    <blockquote>");
                
                BeanBDAccess bean = new BeanBDOracle("127.0.0.1", "1521", "xe", "loisirs", "loisirs");
                QuerySelect qs = new QuerySelect();
                qs.AddSelect("*");
                qs.AddFrom("langue");

                rs = bean.Select(qs);
                
                JspFragment f = getJspBody();
                if (f != null)
                {
                    f.invoke(out);
                }


                while(rs.next())
                {
                    out.print("<form method=\"post\" action=\"MagasinServlet\">");
                    out.print("<input type=\"hidden\" name=\"type\" value=\"langue\">");
                    out.print("<input type=\"hidden\" name=\"local\" value=\""+rs.getString("idLangue")+"\">");
                    out.print("<input type=\"submit\" value=\""+rs.getString("langue")+"\"/>");

                    out.print("</form>");
                }
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(LangueTag.class.getName()).log(Level.SEVERE, null, ex);
            }
            
 
            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex)
        {
            throw new JspException("Error in LangueTag tag", ex);
        }
    }
    
}
