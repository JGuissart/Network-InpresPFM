/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balises;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author adrie
 */
public class DateTag extends SimpleTagSupport
{
    private String chDate;
    private String langue;

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
            // TODO: insert code to write html before writing the body content.
            // e.g.:
            //
            // out.println("<strong>" + attribute_1 + "</strong>");
            // out.println("    <blockquote>");

            JspFragment f = getJspBody();
            if (f != null)
            {
                f.invoke(out);
            }
            Date maintenant = new Date();
            if(langue.equals("en_UK"))
            {
                chDate = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,Locale.UK).format(maintenant);
            }
            else 
            {
                if (langue.equals("fr_FR"))
                {
                    chDate = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,Locale.FRANCE).format(maintenant);
                }
                        
            }
            
            out.println(chDate);
            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex)
        {
            throw new JspException("Error in DateTag tag", ex);
        }
    }

    public void setLangue(String langue)
    {
        this.langue = langue;
    }
    
}
