package com.emimarket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




public class File {
    
    private static String file_name = "Users.txt"; 

    public static void add_user(Utilisateur utilisateur) throws IOException {
        if (utilisateur == null) throw new IllegalArgumentException("User cannot be null");

        try (FileWriter writer = new FileWriter(file_name, true)) {
            writer.write(utilisateur.toString() + System.lineSeparator());
        }
    }

    
    public static boolean is_taken(String user_name) throws IOException{
        try (FileReader reader = new FileReader(file_name);
            BufferedReader br = new  BufferedReader(reader)) {
            String line;
            while ( (line  = br.readLine()) != null) { 
                if (line.isBlank()) continue;
                String split[] = line.split(";");
                if(split[0].equals(user_name))return true;
            }
            return false;
        }
    }

    public static Utilisateur exist(String user_name , String password) throws IOException{
        try (FileReader reader = new FileReader(file_name);
            BufferedReader br = new  BufferedReader(reader)){
                String line;
            while ( (line  = br.readLine()) != null) { 
                if (line.isBlank()) continue;
                String split[] = line.split(";");
                if(split[0].equals(user_name) && split[1].equals(password))
                    return new Utilisateur(user_name,password,split[2],Double.parseDouble(split[3]));
            }
            return null;
            }
    }


    

}
