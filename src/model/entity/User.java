package model.entity;

import model.enumeration.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private int id;
    private String CNP;
    private String username;
    private String parola;
    private String nume;
    private String prenume;
    private String adresa;
    private String telefon;
    private String email;
    private String nrContract;
    private String IBAN;
    private UserRole tipUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNrContract() {
        return nrContract;
    }

    public void setNrContract(String nrContract) {
        this.nrContract = nrContract;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public UserRole getTipUser() {
        return tipUser;
    }

    public void setTipUser(UserRole tipUser) {
        this.tipUser = tipUser;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", telefon='" + telefon + '\'' +
                ", email='" + email + '\'' +
                ", nr_contract='" + nrContract + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", tip_user='" + tipUser + '\'' +
                '}';
    }
    public static User parseUser(ResultSet resultSet,Connection connection) {
    	User user=null;
    	try {
    		String s=resultSet.getString(12);
        	user=new User();
        	user.setId(resultSet.getInt(1));
            if(s.equals("ADMIN"))
                user.setTipUser(UserRole.ADMIN);
            else if(s.equals("SUPER-ADMIN"))
            	user.setTipUser(UserRole.SUPERADMIN);
            else if(s.equals("PROFESOR"))
            {
           
             PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM profesor WHERE user_id=?");
             preparedStatement2.setInt(1, user.getId());

             ResultSet resultSet2 = preparedStatement2.executeQuery();
             if(resultSet2.next()) {
            	 user=new Profesor();
                 user.setId(resultSet2.getInt(1));
                 
                 ((Profesor)user).setMinOre(resultSet2.getInt(2));
                 ((Profesor)user).setMaxOre(resultSet2.getInt(3));
                 ((Profesor)user).setDepartament(resultSet2.getString(4));
             }
             user.setTipUser(UserRole.PROFESOR);
            }
            else if(s.equals("STUDENT"))
            {
            	
            	 PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM  student WHERE user_id=?");
                 preparedStatement2.setInt(1, user.getId());
                 
                 ResultSet resultSet2 = preparedStatement2.executeQuery();
                 if(resultSet2.next()) {
                	 user=new Student();
                     user.setId(resultSet2.getInt(1));
                     
                    ((Student)user).setAn(resultSet2.getInt(2));
                    ((Student)user).setNrOre(resultSet2.getInt(3));
                 }
                user.setTipUser(UserRole.STUDENT);
            }
            
            
            user.setCNP(resultSet.getString(2));
            user.setUsername(resultSet.getString(3));
            user.setParola(resultSet.getString(4));
            user.setNume(resultSet.getString(5));
            user.setPrenume(resultSet.getString(6));
            user.setAdresa(resultSet.getString(7));
            user.setTelefon(resultSet.getString(8));
            user.setEmail(resultSet.getString(9));
            user.setNrContract(resultSet.getString(10));
            user.setIBAN(resultSet.getString(11));
    	}catch(SQLException e1) {
    		e1.printStackTrace();
    	}
    	return user;
    }
}
