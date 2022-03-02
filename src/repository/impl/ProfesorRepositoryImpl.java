package repository.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.Profesor;
import model.entity.User;
import model.enumeration.UserRole;
import repository.ProfesorRepository;
import repository.UserRepository;
public class ProfesorRepositoryImpl implements ProfesorRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;
    private final UserRepository userRepository;
    public ProfesorRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper,UserRepository userRepository) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
        this.userRepository= userRepository;
    }
    @Override
    public ArrayList<Profesor> findAllTeachers() {
        Connection connection = jdbConnectionWrapper.getConnection();
        ArrayList<Profesor> profesori = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.nume,users.prenume,departament,ID "
            		+ "FROM users,profesor "
            		+ "WHERE users.ID=profesor.user_ID;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Profesor user = new Profesor();
                user.setNume(resultSet.getString(1));
                user.setPrenume(resultSet.getString(2));
                user.setDepartament(resultSet.getString(3));
                user.setId(resultSet.getInt(4));
                profesori.add(user);
            }
            if (profesori != null)
                return profesori;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Profesor findByName_p(String firstName, String lastName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Profesor> profesori = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM profesor WHERE lastName=? AND firstName=?");
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Profesor profesor = new Profesor();
                profesor.setId(resultSet.getInt(1));
                profesor.setCNP(resultSet.getString(2));
                profesor.setUsername(resultSet.getString(3));
                profesor.setParola(resultSet.getString(4));
                profesor.setNume(resultSet.getString(5));
                profesor.setPrenume(resultSet.getString(6));
                profesor.setAdresa(resultSet.getString(7));
                profesor.setTelefon(resultSet.getString(8));
                profesor.setEmail(resultSet.getString(9));
                profesor.setNrContract(resultSet.getString(10));
                profesor.setIBAN(resultSet.getString(11));
                String s = resultSet.getString(12);
                if (s.equals("ADMIN"))
                    profesor.setTipUser(UserRole.ADMIN);
                else if (s.equals("PROFESOR"))
                    profesor.setTipUser(UserRole.PROFESOR);
                else if (s.equals("STUDENT"))
                    profesor.setTipUser(UserRole.STUDENT);
                profesor.setMinOre(resultSet.getInt(13));
                profesor.setMaxOre(resultSet.getInt(14));
                profesor.setDepartament(resultSet.getString(15));
                return profesor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean createProfesor(Profesor profesor) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            if(userRepository.create(profesor)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO profesor(user_id,min_ore,max_ore,departament) VALUES(?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, profesor.getId());
                preparedStatement.setInt(2, profesor.getMinOre());
                preparedStatement.setInt(3, profesor.getMaxOre());
                preparedStatement.setString(4, profesor.getDepartament());
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean updateProfesor(Profesor profesor) {
         Connection connection = jdbConnectionWrapper.getConnection();
         try {
             if(userRepository.update(profesor))
             {
            	 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE profesor SET min_ore=?,max_ore=?, departament=? WHERE user_id=?",
                         Statement.RETURN_GENERATED_KEYS);
                 
                 preparedStatement.setInt(1, profesor.getMinOre());
                 preparedStatement.setInt(2, profesor.getMaxOre());
                 preparedStatement.setString(3, profesor.getDepartament());
                 preparedStatement.setInt(4, profesor.getId());
                 int changedRows = preparedStatement.executeUpdate();
                 return changedRows > 0;
             }
             else
            	 return false;
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
     }
    public boolean deleteProfesor(String userName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        boolean b = false;
        try {
            User profesor = userRepository.loadByUserName(userName);
            if(profesor==null)
            	return false;
            int id = profesor.getId();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM profesor WHERE user_id=?");
            preparedStatement.setInt(1, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                b = userRepository.delete(profesor);
                return b;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }
    
}