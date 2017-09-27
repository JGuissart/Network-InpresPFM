/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applic_jchat_pfm;

/**
 *
 * @author Julien
 */
public class Requete
{
    private String Message;
    private int Tag;
    
    public Requete(String Message)
    {
        this.Message = Message;
        this.Tag = 0;
    }
    
    public Requete(String Message, int Tag)
    {
        this.Message = Message;
        this.Tag = Tag;
    }

    /**
     * @return the Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     * @param Message the Message to set
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * @return the Tag
     */
    public int getTag() {
        return Tag;
    }

    /**
     * @param Tag the Tag to set
     */
    public void setTag(int Tag) {
        this.Tag = Tag;
    }

    @Override
    public String toString() 
    {
        return Message;
    }
}
