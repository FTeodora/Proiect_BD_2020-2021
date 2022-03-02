package repository;

import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();

    boolean createStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(String userName);
}
