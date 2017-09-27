/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommunicationBasNiveau;

import java.io.DataInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class MessageCPP
{
    public static byte[] ConvertirChaineEnBytes(String message)
    {
        byte[] bytes = message.getBytes();
        
        // On agrandit notre tableau pour y ajouter un \0
        byte[] bytesRetour = new byte[bytes.length + 1];
        
        // On copie le premier tableau dans le second
        System.arraycopy(bytes, 0, bytesRetour, 0, bytes.length);
        
        // Et on ajoute le \0 !
        bytesRetour[bytesRetour.length - 1] = (byte)'\0';
        
        return bytesRetour;
    }
    
    public static String BoucleReception(DataInputStream dis, char charDebutChaine, char charFinChaine)
    {
        byte b;
        StringBuilder sb = new StringBuilder();

        try
        {
            while((b = dis.readByte()) != (byte) charFinChaine)
            {
                if(b == (byte) charDebutChaine)
                {
                    while((b = dis.readByte()) != (byte) charFinChaine)
                    {
                        System.out.println((char) b);
                        sb.append((char) b);
                    }
                    break;
                }
            }
            
            return sb.toString().trim();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Erreur lors de la réception d'un message en C++.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
