package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TelegramBotService {


    NotificationTask create(NotificationTask notificationTask);
    Collection<NotificationTask> findTask(LocalDateTime localDateTime);
    Long count();
    void delete(NotificationTask notificationTask);
}
