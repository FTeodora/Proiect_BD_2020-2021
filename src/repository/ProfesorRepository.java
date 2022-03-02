package repository;

import model.entity.Profesor;
import model.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface ProfesorRepository {

        boolean createProfesor(Profesor profesor);

        boolean updateProfesor(Profesor profesor);

        boolean deleteProfesor(String userName);

		ArrayList<Profesor> findAllTeachers();


    }

