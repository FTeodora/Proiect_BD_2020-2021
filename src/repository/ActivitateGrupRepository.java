package repository;

import java.util.ArrayList;

import model.entity.ActivitateGrup;

public interface ActivitateGrupRepository {
	ArrayList<ActivitateGrup> getActivities(int groupID);
	ArrayList<ActivitateGrup> getActivities(int groupID, int studentID, boolean isEnrolled);
}
