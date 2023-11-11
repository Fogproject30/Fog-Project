/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fog.project;

import java.sql.*;
import java.util.Random;
import org.cloudbus.cloudsim.util.MathUtil;
public class category {

    public static int No_T=50;
  
    public static double[][] tasks = new double[No_T][5];
    public static double[][] sort_tasks = new double[No_T][5];
//////////////////////////////// main

    public static void main(String[] args) {

        int i, j;
   
   

  long start = System.currentTimeMillis();

        try {
          String url = "jdbc:mysql://localhost:3306/result";
            String user = "root";
            String pass = "";

            Connection con = DriverManager.getConnection(url,user,pass);

            Statement st = con.createStatement();

//************************** Get Task Attributes

            String ins = "select * from task ;";
            ResultSet rs = st.executeQuery(ins);
            rs.beforeFirst();
            i = 0;
              while (rs.next() && i < No_T) {
                tasks[i][0] = rs.getDouble("id");
                tasks[i][1] = rs.getDouble("lnt");
                tasks[i][2] = rs.getDouble("arrtime");
                tasks[i][3] = rs.getDouble("memsize");
                tasks[i][4] = rs.getDouble("piriority");
                i++;
            } 
             

//*****************************category Tasks**********************

  
    int cnt1=0,cnt2=0, cnt3=0;
 double[] data= new double[No_T];
     for(i=0; i<No_T; i++)
     {
        if(tasks[i][4]==0 ) 
            cnt1++;
        else if(tasks[i][4]==1)
                cnt2++;
        else 
            cnt3++;
                
     }

//****************** Save in Db 
        int id=0;
            ins="truncate table results;";
            st.execute(ins);
                ins = "INSERT INTO results(id,number1,number2,number3) VALUES ("+id+","+(cnt1)+","+(cnt2)+","+(cnt3)+")" ;
                st.execute(ins);
           



        } 


        catch (Exception e)
        {
            System.err.print(e.getMessage());
        }
    }






    public static double randnum(int i, int r, char typ) {
        double rand;
        Random rnd = new Random();
        rand = i + (rnd.nextDouble()) * (r - i + 1);
        if (typ == 'i') {
            rand = (int) rand;
        }
        return rand;
    }
}
