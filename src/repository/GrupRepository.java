package repository;

import java.util.ArrayList;

import model.entity.Grup;

public interface GrupRepository {
	ArrayList<Grup> enrolledGroups(int studentID);
	ArrayList<Grup> viewGroups(int studentID);
	ArrayList<Grup> getGroupsByCourse(int studentID,int courseID,boolean isEnrolled);
	ArrayList<Grup> toateGrupurile();
	ArrayList<Grup> cautaGrupuri(int cursID);

}
