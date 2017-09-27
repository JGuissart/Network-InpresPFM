/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AccessBD;

/**
 *
 * @author Julien
 */
public class BeanBDMySql extends BeanBDAccess
{
    public BeanBDMySql()
    {
        this.DEFAULT_SELECT = "*";
        this.DEFAULT_FROM = "information_schema.tables";
        this.DEFAULT_WHERE = "";
        this.CONNECT = "jdbc:mysql://";
        this.DRIVER = "com.mysql.jdbc.Driver";
    }
    
    public BeanBDMySql(String AdresseIp, String Port, String BdName)
    {
        this();
        String Connect = AdresseIp + ":" + Port + "/" + BdName;
        this.Connect(Connect);
    }
    
    public BeanBDMySql(String AdresseIp, String Port, String BdName, String Login, String Password)
    {
        this();
        String Connect = AdresseIp + ":" + Port + "/" + BdName;
        this.Connect(Connect, Login, Password);
    }
}