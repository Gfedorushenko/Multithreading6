package org.example.Files;

import com.google.gson.Gson;
import org.example.Logger.Level;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.example.Main.logger;

public class Settings {
    private int port;
    private String host;
    private String role;
    private String user;

    public Settings(int port, String host, String role) {
        this.port = port;
        this.host = host;
        this.role = role;
    }

    public Settings() {
        this.port = 0;
        this.host = "";
        this.role = "";
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getRole() {
        return role;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static Settings getSettings(String fileName) {
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            logger.log("Прочитали настройки из файл конфигурации", Level.DEBUG);
            return gson.fromJson(br, Settings.class);
        } catch (FileNotFoundException e) {
            logger.log("getSettings: " + e.toString(), Level.ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Employee{port=" + port + ", host='" + host + "', role='" + role + "', user='" + user + "'}";
    }

    @Override
    public int hashCode()
    {
        int result = host == null ? 0 : host.hashCode();
        result =31* result + role.hashCode();
        result =31* result + port;
        return result;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings that = (Settings) o;

        if (port != that.port) return false;
        if (!host.equals(that.host)) return false;
        return role.equals(that.role);
    }
}
