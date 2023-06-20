package org.vaadin.example.crud2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyDatabase {
    //private static int id;
    private String name;
    private String phone;
    private String email;
    private String street;
    private String city;
    private String country;
    private String time;
    private String id;
    public MyDatabase(String name, String phone, String email, String street, String city, String country,String time) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.city = city;
        this.country = country;
        this.time = time;
    }
    public MyDatabase(String name, String phone, String email, String street, String city, String country,String time,String id) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.city = city;
        this.country = country;
        this.time = time;
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
                String time = resultSet.getString("time");
                String id = resultSet.getString("id");
                //int id = resultSet.getInt(1);
                MyDatabase item = new MyDatabase(name, phone, email,street, city,country,time,id);
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
    public MyDatabase addContact(String name, String phone, String email, String street, String city, String country,String time1) {
            PreparedStatement statement;
            try {
                statement = getConnection().prepareStatement("INSERT INTO contacts (Name, phone_no, email, street, city, country, time) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, phone);
                statement.setString(3, email);
                statement.setString(4, street);
                statement.setString(5, city);
                statement.setString(6, country);
                statement.setString(7,time1);
                //System.out.println(statement);
                statement.executeUpdate();

                statement.close();
                getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        MyDatabase contact = new MyDatabase(name, phone, email, street, city, country,time1);
        return contact;
    }

//--------------------------------------------------EDIT - CONTACT ----------------------------------------//
    public MyDatabase editContact(String id,String name, String phone, String email, String street, String city, String country,String time){

        try {
            PreparedStatement statement = getConnection().prepareStatement("update contacts set name = ?, phone_no = ?, email = ?, " +
                    "street = ?, city = ?, country = ?, Time = ? where id = ?" );
            statement.setString(1,name);
            statement.setString(2,phone);
            statement.setString(3,email);
            statement.setString(4,street);
            statement.setString(5,city);
            statement.setString(6,country);
            statement.setString(7,time);
            statement.setString(8,id);

            statement.executeUpdate();
            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MyDatabase contact =new MyDatabase(name,phone,email,street,city,country,time);
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
    public boolean deleteContact(String id) {
        PreparedStatement statement;
        boolean isDeleted;
        try {
            statement = getConnection().prepareStatement("delete from contacts where id= ?");
            statement.setString(1,id);
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

        return isDeleted;
    }

    public String getTimeBYid(String id) {
        ResultSet rs;
        String time = null;
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT Time FROM contacts WHERE id = ?");
            statement.setString(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                 time = rs.getString("Time");
                /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                isTime = sdf.parse(timeStr);*/
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return time;
    }


    //------------------------------------------Connection with DB----------------------------------------//
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
