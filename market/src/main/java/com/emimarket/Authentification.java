package com.emimarket;

import java.io.IOException;

public class Authentification {

    private static Utilisateur current_user ;
    
    public static boolean register (Utilisateur u) throws IOException{
        if ( File.is_taken(u.getNom_utilisateur()))return false;
        File.add_user(u);
        return true;
    }

    public static boolean login(String user_name , String password) throws IOException{
        Utilisateur u ;
        if ( (u = File.exist(user_name, password)) != null ){
            current_user = u;
            return true;
        }
        return false;        
    }
    
    public static boolean logout(){
        if ( current_user != null){
            current_user=null;
            return true;
        }
        return false;
    }

}
