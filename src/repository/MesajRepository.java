package repository;

import model.entity.Mesaj;

import java.util.ArrayList;

public interface MesajRepository {
    ArrayList<Mesaj> getMessages(int groupID);
}
