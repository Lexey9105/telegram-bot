package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramBotService;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {
private NotificationTaskRepository notificationTaskRepository;
private TelegramBot telegramBot;
private  Long idCounter = 1L;

public TelegramBotServiceImpl (NotificationTaskRepository notificationTaskRepository,TelegramBot telegramBot){
    this.notificationTaskRepository=notificationTaskRepository;
    this.telegramBot=telegramBot;
}

    @Override
    public NotificationTask create(NotificationTask notificationTask) {
        idCounter++;
    return notificationTaskRepository.save(notificationTask);
    }
    @Override
    public Long count(){return idCounter;}
    @Override
    public Collection<NotificationTask> findTask(LocalDateTime localDateTime) {
        return  notificationTaskRepository.findNotificationTaskByDuration(localDateTime);
                //.forEach(notificationTask -> {
                        //    SendMessage message = new SendMessage
                              //      (notificationTask.getChatId(), notificationTask.getNotification());
                           // SendResponse response = telegramBot.execute(message);
           // notificationTaskRepository.delete(notificationTask);
       // });
    }
    @Override
    public void delete(NotificationTask notificationTask){
        notificationTaskRepository.delete(notificationTask);
    }
}
