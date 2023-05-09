package org.vaadin.example.crud2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    //private static int id;
    private String name;
    private String phone;
    private String email;
    private String street;
    private String city;
    private String country;
    private String time;

    public MyDatabase(String name, String phone, String email, String street, String city, String country) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public MyDatabase() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/phonebook";
    private static final String USER = "root";
    private static final String PASSWORD = "l1nx@3!";
    public static List<MyDatabase> getItems() {
        List<MyDatabase> items = new ArrayList<>();

        try {
            // Load the JDBC driver

            // Create a statement and execute the query
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts");

            // Iterate over the result set and create MyItem objects
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String phone = resultSet.getString("phone_no");
                String email = resultSet.getString("email");
                String street = resultSet.getString("street");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                //int id = resultSet.getInt(1);
                MyDatabase item = new MyDatabase(name, phone, email,street, city,country);
                items.add(item);
            }
            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
    //contact name,phone,email,street,city,country
    public static MyDatabase addContact(String name, String phone, String email, String street, String city, String country) {
        PreparedStatement statement;

        try {
            statement = getConnection().prepareStatement("INSERT INTO contacts (Name, phone_no, email, street, city, country, time) VALUES (?, ?, ?, ?, ?, ?, null)");
            statement.setString(1,name);
            statement.setString(2,phone);
            statement.setString(3,email);
            statement.setString(4,street);
            statement.setString(5,city);
            statement.setString(6,country);
            System.out.println(statement);
            statement.executeUpdate();

            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MyDatabase contact = new MyDatabase(name, phone, email, street, city, country);
        return contact;
    }

//--------------------------------------------------EDIT - CONTACT ----------------------------------------//
    public static MyDatabase editContact(int id,String name, String phone, String email, String street, String city, String country){
        try {
            PreparedStatement statement = getConnection().prepareStatement("update contacts set name = ?, phone_no = ?, email = ?, " +
                    "street = ?, city = ?, country = ?, Time = Now() where id = ?" );
            statement.setString(1,name);
            statement.setString(2,phone);
            statement.setString(3,email);
            statement.setString(4,street);
            statement.setString(5,city);
            statement.setString(6,country);
            statement.setInt(7,id);
            //System.out.println(statement);
            statement.executeUpdate();
            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MyDatabase contact =new MyDatabase(name,phone,email,street,city,country);
        return contact;
    }
    public int getId(String phone){
        PreparedStatement statement;
        ResultSet rs;
        int id = 0;
        try{
            statement = getConnection().prepareStatement("select id from contacts where phone_no = ?");
            statement.setString(1,phone);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
    public boolean deleteContact(String phone) {
        PreparedStatement statement;
        boolean isDeleted;
        try {
            statement = getConnection().prepareStatement("delete from contacts where phone_no= ?");
            statement.setString(1,phone);
           int rowsDeleted =  statement.executeUpdate();
            if (rowsDeleted > 0) {
                isDeleted = true;
            } else {
                isDeleted = false;
            }
            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //rs.close();                       // Close the ResultSet                  5
        ;
        return isDeleted;
    }

    public boolean isTimeNull(String phone){
        boolean isTimeNull = true;
        ResultSet rs;
        try {
            PreparedStatement statement = getConnection().prepareStatement("Select Time from contacts where phone_no = ?");
            statement.setString(1,phone);
            rs = statement.executeQuery();        // Get the result table from the query  3
            while (rs.next()) {
                time = rs.getString(1);

                isTimeNull = false;
            }
            rs.close();                       // Close the ResultSet                  5
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isTimeNull;
    }
    //------------------------------------------Connection with DB------------u----------------------------//
    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            /*if (connection != null && !connection.isClosed()) {
                System.out.println("Connected Successfully");
            } else {
                System.out.println("Not connected.");
            }*/
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        // Connect to the database
        return connection;
    }
}
