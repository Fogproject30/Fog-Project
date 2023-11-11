/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fog.project;

import java.sql.*;
import java.util.Random;
public class insert_task {

    public static int No_T = 50;
    public static double[][] tasks = new double[No_T][7];
    public static double Sum_RMIPS;
     public static double Min_RMIPS;
    public static double Avg_RMIPS;


    public static void main(String[] args) {
        int i;
        try {
            String url = "jdbc:mysql://localhost:3306/result";
            String user = "root";
            String pass = "";

            Connection con = DriverManager.getConnection(url,user,pass);
            Statement st = con.createStatement();


            String ins = "truncate table task;";
        


         
            st.execute(ins);

             for (i = 0; i < No_T; i++) {
                // VM ID : 0
                tasks[i][0] = i;
                // task Length : 1
                tasks[i][1] = randnum(5000, 15000, 'i');
                tasks[i][2] = 1;
                // task Memory Size : 3
               tasks[i][3] = randnum(1, 100, 'i');
                
              
                // piriority : 4
                tasks[i][4] =randnum(0, 2, 'i');
                
                ins = "INSERT INTO task VALUES (" + i + "," + tasks[i][1] + "," + tasks[i][2] + "," + tasks[i][3] + "," + tasks[i][4] + ");";
                System.out.println(ins);
                st.execute(ins);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
