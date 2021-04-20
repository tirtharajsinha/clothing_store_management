package com.company;

import java.sql.*;

public class jdbc {

    private static Connection con;

    public static void main(String[] args) {

//        String  result= fetch("select loc from dept where deptno=20");
//        System.out.println(result);

    }

    public static Connection getConnection(String db_name,String pass) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", db_name, pass);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }
    public static void closeConnection(Connection con){
        try{
            con.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void con(String query, Connection con) {
        try {


            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnnum = rsmd.getColumnCount();
            for (int i = 1; i <= columnnum; i++) {
                String name = rsmd.getColumnName(i);
                System.out.print(name + " | ");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= columnnum; i++) {
                    System.out.print("---------");
                }
                System.out.println();

                for (int i = 1; i <= columnnum; i++) {
                    System.out.print(rs.getString(i) + " | ");
                }
                System.out.println();


            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String fetch(String query, Connection con) {
        String result;
        try {


            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            result = rs.getString(1);




        } catch (Exception e) {
            System.out.println(e);
            result = e.toString();
        }
        return result;
    }

    public static void manipulate(String query, Connection con) {
        try {


            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);


        } catch (Exception e) {
            System.out.println(e);

        }

    }


}
