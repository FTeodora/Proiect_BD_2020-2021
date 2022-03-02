package repository;

import java.util.ArrayList;

import model.entity.Notificare;

public interface NotificareRepository {
    boolean sendNotification(Notificare notificare);
    ArrayList<Notificare> getNotifications(int userID);
}
