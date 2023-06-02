package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    // Constructor
    public Database() {
        // Initialize the connection object
        connection = null;
    }

    // Method to connect to the database
    public void connect() throws SQLException {
        // Replace the connection details with your own
        String url = "jdbc:postgresql://localhost:5432/gravefinderdb/ssl=required";
        String username = "postgres";
        String password = "mypassword";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to disconnect from the database
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Method to check if the username and password match in the database
    public boolean checkCredentials(String username, String password) {
        // Execute the query task asynchronously
        QueryTask queryTask = new QueryTask();
        queryTask.execute(username, password);

        try {
            return queryTask.get();  // Wait for the task to complete and return the result
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // AsyncTask to handle the database connection on a separate thread
    private class ConnectTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            String username = params[1];
            String password = params[2];

            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    // AsyncTask to handle the query execution on a separate thread
    private class QueryTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            String query = "SELECT * FROM app_customuser WHERE username = ? AND password = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                boolean match = resultSet.next();
                resultSet.close();
                statement.close();
                return match;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
