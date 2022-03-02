package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.User;
import model.enumeration.UserRole;
import model.entity.Student;
import repository.StudentRepository;
import repository.UserRepository;

public class StudentRepositoryImpl implements StudentRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;
    private final UserRepository userRepository;

    public StudentRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper,UserRepository userRepository) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
        this.userRepository=userRepository;
    }

    public List<Student> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Student> students = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setCNP(resultSet.getString(2));
                student.setUsername(resultSet.getString(3));
                student.setParola(resultSet.getString(4));
                student.setNume(resultSet.getString(5));
                student.setPrenume(resultSet.getString(6));
                student.setAdresa(resultSet.getString(7));
                student.setTelefon(resultSet.getString(8));
                student.setEmail(resultSet.getString(9));
                student.setNrContract(resultSet.getString(10));
                student.setIBAN(resultSet.getString(11));
                String s=resultSet.getString(12);
                if(s.equals("ADMIN"))
                    student.setTipUser(UserRole.ADMIN);
                else if(s.equals("PROFESOR"))
                    student.setTipUser(UserRole.PROFESOR);
                else if(s.equals("STUDENT"))
                    student.setTipUser(UserRole.STUDENT);
                student.setAn(resultSet.getInt(13));
                student.setNrOre(resultSet.getInt(14));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }



    @Override
    public boolean createStudent(Student student) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {

           if(userRepository.create(student)) {

               PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student(user_id,an,nr_ore) VALUES (?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
               preparedStatement.setInt(1, student.getId());
               preparedStatement.setInt(2, student.getAn());
               preparedStatement.setInt(3, student.getNrOre());
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
    public boolean updateStudent(Student student) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
          if(userRepository.update(student)) {

              PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student SET an=?,nr_ore=? WHERE user_id=?",
                      Statement.RETURN_GENERATED_KEYS);
              
              preparedStatement.setInt(1, student.getAn());
              preparedStatement.setInt(2, student.getNrOre());
              preparedStatement.setInt(3, student.getId());
              int changedRows = preparedStatement.executeUpdate();
              System.out.println(changedRows);
              return changedRows > 0;
        	  
          }
          else 
        	  return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String userName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        boolean b=false;

        try {
            User student= userRepository.loadByUserName(userName);
            if(student==null)
            	return false;
            int id=student.getId();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM student WHERE user_id=?");
            preparedStatement.setInt(1, id);
            int updatedRows = preparedStatement.executeUpdate();
            if(updatedRows > 0) {
                b = userRepository.delete(student);
                return b;
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

}