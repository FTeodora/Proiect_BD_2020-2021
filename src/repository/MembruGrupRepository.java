package repository;

import model.entity.Student;

import java.util.ArrayList;

public interface MembruGrupRepository {
    boolean joinGroup(int groupID,int studentID);
    boolean leaveGroup(int groupID,int studentID);
    int getNrParticipants(int groupID);
    ArrayList<Student> listParticipants(int groupID);

}
