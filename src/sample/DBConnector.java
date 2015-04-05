package sample;

import javafx.event.ActionEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;


/**
 * Created by John Show on 2015/4/5.
 */
public class DBConnector {
    public DBConnector() {

    }

    public void testDB(){
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        System.out.println("start link db");
        String url = "jdbc:mysql://210.28.164.6:3306/car";
        String user = "root";
        String password = "";

        try {
            con = DriverManager.getConnection(url, user, password);

            st = con.createStatement();
            System.out.println("连接数据库");

            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println("数据库版本"+rs.getString(1));
            }

        } catch (SQLException ex) {

            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {

            }
        }
    }



    public static boolean httpDownload(String httpUrl,String saveFile){

        int bytesum = 0;
        int byteread = 0;

        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

