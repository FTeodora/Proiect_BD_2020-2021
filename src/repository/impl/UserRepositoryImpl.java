package repository.impl;

import model.entity.FisaCurs;
import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import model.enumeration.UserRole;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;

    public JDBConnectionWrapper getJdbConnectionWrapper() {
		return jdbConnectionWrapper;
	}

	public UserRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    @Override
    public User findByCNP(String CNP) {
        Connection connection = jdbConnectionWrapper.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE CNP=?");
            preparedStatement.setString(1, CNP);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
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
                String s=resultSet.getString(12);
                if(s.equals("ADMIN"))
                    user.setTipUser(UserRole.ADMIN);
                else if(s.equals("PROFESOR"))
                    user.setTipUser(UserRole.PROFESOR);
                else if(s.equals("STUDENT"))
                    user.setTipUser(UserRole.STUDENT);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public User loadByUserName(String userName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=?");
            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	return User.parseUser(resultSet, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean create(User user) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(CNP,username,parola,nume,prenume,adresa,telefon,email,nr_contract,IBAN,tip_user) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getCNP());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getParola());
            preparedStatement.setString(4, user.getNume());
            preparedStatement.setString(5, user.getPrenume());
            preparedStatement.setString(6, user.getAdresa());
            preparedStatement.setString(7, user.getTelefon());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getNrContract());
            preparedStatement.setString(10, user.getIBAN());
            preparedStatement.setString(11, (user.getTipUser()).toString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET CNP=?,username=?,parola=?,nume=?,prenume=?,adresa=?,telefon=?,email=?,nr_contract=?,IBAN=? "
            		+ "WHERE id=?");
            
            preparedStatement.setString(1, user.getCNP());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getParola());
            preparedStatement.setString(4, user.getNume());
            preparedStatement.setString(5, user.getPrenume());
            preparedStatement.setString(6, user.getAdresa());
            preparedStatement.setString(7, user.getTelefon());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getNrContract());
            preparedStatement.setString(10, user.getIBAN());
            preparedStatement.setInt(11, user.getId());

            int changedRows = preparedStatement.executeUpdate();
            return changedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public  boolean delete(User user){
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
        
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id=?");
            preparedStatement.setInt(1, user.getId());

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> findUsers(String type_user){
        ArrayList<User> useri=new ArrayList();
        Connection connection = jdbConnectionWrapper.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.nume,users.prenume FROM users WHERE tip_user=? ORDER BY users.nume,users.prenume ASC;");
            preparedStatement.setString(1, type_user);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = new User();
                user.setNume(resultSet.getString(1));
                user.setPrenume(resultSet.getString(2));
                useri.add(user);
            }
            if(useri!=null)
                return useri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> findStudentsbyYear(int an){
        ArrayList<User> useri=new ArrayList();
        Connection connection = jdbConnectionWrapper.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id IN (SELECT student.user_id FROM student  WHERE an=?) ORDER BY users.nume,users.prenume ASC;");
            preparedStatement.setInt(1, an);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = User.parseUser(resultSet,connection);
                useri.add(user);
            }
            if(useri!=null)
                return useri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

	@Override
    public ArrayList<User> findUsersbyName(String lastName, String firstName,int role) {
        ArrayList<User> users=null;
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * "
                        + " FROM users "
                        + " WHERE nume LIKE '"+lastName+"%'"
                        + " AND prenume LIKE '"+firstName+"%'"
                        + " AND tip_user>"+role
                        );
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next())
            {
            	users=new ArrayList<User>();
                User user=new User();
                user=User.parseUser(resultSet,connection);
                users.add(user);
                while (resultSet.next())
                    users.add(User.parseUser(resultSet,connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}