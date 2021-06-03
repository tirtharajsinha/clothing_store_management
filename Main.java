package com.company;

import java.sql.Connection;
import java.util.Scanner;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // code starts here
        Scanner scan = new Scanner(System.in);
        auth auth = new auth();
        insert insert = new insert();
        view view = new view();
        sales sales = new sales();
        boolean repeater = true;
        System.out.println("welcome back .");
        while (repeater) {
            if (auth.login()) {
                boolean inner = true;
                while (inner) {
                    System.out.println("------------------------");
                    System.out.println("| 1. add item          |\n| 2. refill stock      |\n| 3. update price      |\n| 4. view on criterias |\n| 5. create bill       |\n| 6. view bill         |" +
                            "\n| 7. Exchange          |\n| 8. register new user |\n| 9. logout            |");
                    System.out.print("------------------------\n\nPleace select your action: ");
                    int option = scan.nextInt();

                    switch (option) {
                        case 1 -> insert.add();
                        case 2 -> insert.refill();
                        case 3 -> insert.update_price();
                        case 4 -> view.select();
                        case 5 -> sales.createbill();
                        case 6 -> sales.viewbill();
                        case 7 -> sales.exchange();
                        case 8 -> auth.register();
                        case 9 -> inner = false;
                        case 92 -> {
                            inner = false;
                            repeater=false;
                        }
                    }
                }
            }
            if(repeater){
                System.out.print("1. login\n2. exit\nSelect your action : ");
                int prog = scan.nextInt();
                if (prog == 2) {
                    repeater = false;
                }
            }

        }
        System.out.println("see you later.");
    }
}

class auth {
    String username, password;


    Boolean login() {
//        connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");

//      login logic starts here
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your username : ");
        username = scan.nextLine();
        System.out.print("Enter your passward : ");
        password = scan.nextLine();
        String pass = jdbc.fetch("select password from admin where username='" + username + "'", con);

        if (pass.equals(password)) {
            System.out.println(" --------------------------\n| loging successful (∩▂∩) |\n --------------------------");
            System.out.println("welcome back, " + jdbc.fetch("select name from admin where username='" + username + "'", con));
            jdbc.closeConnection(con);

            return true;
        } else {
            System.out.println("wrong password ( ˃̥̥̥ ˑ̫ ˂̥̥̥ ) !!");
            return false;
        }

    }

    void register() {
        Connection con = jdbc.getConnection("mydb", "oracle");
        String status;
        status = jdbc.fetch("select status from admin where username='" + username + "'", con);
        if (status.equals("admin")) {
            String newuser, newpass, newstatus, newname;
//          sign-up logic starts here
            System.out.println("registering new user ..........\n");
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter new username : ");
            newuser = scan.nextLine();
            System.out.print("Enter new user full name : ");
            newname = scan.nextLine();
            System.out.print("Enter your new user user-status | admin/user | : ");
            newstatus = scan.nextLine();
            System.out.print("Enter new user passward : ");
            newpass = scan.nextLine();

            String available = jdbc.fetch("select * from admin where username='" + newuser + "'", con);
            System.out.println("requested for ragistration..........");
            if (available.equals("java.sql.SQLException: Invalid column index")) {
                jdbc.manipulate(String.format("insert into admin values('%s','%s','%s','%s')", newuser, newpass, newstatus, newname), con);
                System.out.println("ragistration successful");
            } else {
                System.out.println("request denied !!!!\n< User alrady exists >");
            }


        } else {
            System.out.println("You are not admin,only Administrator can register a new user.");
        }

    }


}

class insert {


    void add() {
        String name, catagory, type, size, dsc, color;
        int id, stock, price;

//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");
        Scanner scan = new Scanner(System.in);
        System.out.println("adding new item to stock............\n");
//      add logic start here
        System.out.print("Enter item name : ");
        name = scan.nextLine();
        System.out.print("Enter item catagory : ");
        catagory = scan.nextLine();
        System.out.print("Enter item type : ");
        type = scan.nextLine();
        System.out.print("Enter item size : ");
        size = scan.nextLine();
        System.out.print("Enter item description : ");
        dsc = scan.nextLine();
        System.out.print("Enter item color : ");
        color = scan.nextLine();
        System.out.print("Enter item stock : ");
        stock = scan.nextInt();
        System.out.print("Enter item price : ");
        price = scan.nextInt();
        String query;
        id = Integer.parseInt(jdbc.fetch("select max(id) from clothes", con)) + 1;
        query = String.format("insert into clothes values(%d,'%s','%s','%s','%s','%s','%s',%d,%d)", id, name, catagory, type, size, dsc, color, stock, price);
        jdbc.manipulate(query, con);
        jdbc.con("select * from clothes where id=" + id, con);
        jdbc.closeConnection(con);
    }

    void refill() {
        Scanner scan = new Scanner(System.in);
//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");

//      variable declaration
        int id, stock;
        System.out.println("Refilling stock......\n");
        System.out.print("Enter item id : ");
        id = scan.nextInt();
        System.out.print("Enter item's stock addition: ");
        stock = scan.nextInt();
        stock = Integer.parseInt(jdbc.fetch("select stock from clothes where id=" + id, con)) + stock;
        jdbc.manipulate("update clothes set stock=" + stock + " where id=" + id, con);
        jdbc.con("select * from clothes where id=" + id, con);

        jdbc.closeConnection(con);


    }

    void update_price() {
        Scanner scan = new Scanner(System.in);
//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");

//      variable declaration
        int id, price;
        System.out.println("Updating price............\n");
        System.out.print("Enter item id : ");
        id = scan.nextInt();
        System.out.print("Enter item's new price: ");
        price = scan.nextInt();
        jdbc.manipulate("update clothes set price=" + price + " where id=" + id, con);
        jdbc.con("select * from clothes where id=" + id, con);

        jdbc.closeConnection(con);


    }
}

class view {
    Scanner scan = new Scanner(System.in);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    void select() throws IOException {

        Connection con = jdbc.getConnection("mydb", "oracle");

        System.out.print("How many criteria you want to give ?:[ int ]: ");
        int n = scan.nextInt();
        StringBuilder query = new StringBuilder("select * from clothes");
        if (n == 0) {
            jdbc.con("select * from clothes", con);
        } else {
            query.append(" where ");
            int i;
            String str;
            for (i = 0; i < n; i++) {
                System.out.println(i);
                System.out.println("name, catagory, type, size, dsc, color, stock, price");
                System.out.print("please select your critaria from list: ");
                str = br.readLine();
                query.append(str);
                if (i != n - 1) {
                    System.out.print("comparing conjunction [and/or] : ");
                    String opr = scan.next();
                    query.append(" ").append(opr).append(" ");
                }

            }
            String q = query.toString();
            System.out.println(query);
            jdbc.con(q, con);
        }
        jdbc.closeConnection(con);
    }
}

class sales {
    void createbill() {
        Scanner scan = new Scanner(System.in);
//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");
        System.out.println("Creating new bill.......\n");
//      Create bill logic
        int quan, total, price;
        String id, got;
        System.out.print("Enter item_id : ");
        id = scan.nextLine();
        System.out.print("Enter quantity of item : ");
        quan = scan.nextInt();
        got = jdbc.fetch("select price from clothes where id=" + id, con);
        if (got.equals("java.sql.SQLException: Invalid column index")) {
            System.out.println("no item for id " + id);
        } else {
            price = Integer.parseInt(got);

            total = price * quan;
            int bill_id = Integer.parseInt(jdbc.fetch("select max(id) from sales", con)) + 1;
            System.out.println();
            System.out.println("cloth_id : " + id + "| Quantity: " + quan + "| Total Price : " + total);
            String q = String.format("insert into sales values(%d,%s,%d,%d,sysdate)", bill_id, id, quan, total);
            jdbc.manipulate(q, con);
            jdbc.manipulate("update clothes set stock=stock-" + quan + " where id=" + id, con);
            System.out.println("your bill id is " + bill_id);
        }
        jdbc.closeConnection(con);


    }

    void exchange() {
        Scanner scan = new Scanner(System.in);
//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");
        System.out.print("Enter old bill id : ");
        int Bill_id = scan.nextInt();
        String ret = jdbc.fetch("select * from sales where id=" + Bill_id, con);
        if (ret.equals("java.sql.SQLException: Invalid column index")) {
            System.out.println("invalid bill id, recheck id");
        } else {
            int quan, total, price, clothes_id;
            String got;
            System.out.print("Enter item_id : ");
            clothes_id = scan.nextInt();
            System.out.print("Enter quantity of item : ");
            quan = scan.nextInt();
            got = jdbc.fetch("select price from clothes where id=" + clothes_id, con);
            if (got.equals("java.sql.SQLException: Invalid column index")) {
                System.out.println("no item for id " + clothes_id);
            } else {
                price = Integer.parseInt(got);

                total = price * quan;

                System.out.println();
                System.out.println("cloth_id : " + clothes_id + " Quantity: " + quan + " Total Price : " + total);
                String q = String.format("update sales set cloth_id=%d,quantity=%d,total=%d where id=%d", clothes_id, quan, total, Bill_id);
                jdbc.manipulate(q, con);

            }
        }

    }

    void viewbill() throws IOException {
        Scanner scan = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//      connecting database

        Connection con = jdbc.getConnection("mydb", "oracle");
        System.out.print("filter bill by ->\n0. no filter \n1. bill_id \n2. cloth_id\n3. date \nselect[int] : ");
        int n = scan.nextInt();
        if (n == 0) {
            jdbc.con("select * from sales", con);
        } else if (n == 1) {
            System.out.print("enter bill_id : ");
            int bid = scan.nextInt();
            jdbc.con("select * from sales where id=" + bid, con);
        } else if (n == 2) {
            System.out.print("enter clothes_id :");
            int sid = scan.nextInt();
            jdbc.con("select * from sales where cloth_id=" + sid, con);
        } else if (n == 3) {
            System.out.print("enter start date [dd-mm-yy] : ");
            String Start_date = br.readLine();
            System.out.print("enter end date [dd-mm-yy] : ");
            String end_date = br.readLine();
            String q = String.format("select * from sales where sale_date>='%s' and sale_date<='%s'", Start_date, end_date);
            System.out.println();
            jdbc.con(q, con);
            jdbc.closeConnection(con);
        }

    }
}
