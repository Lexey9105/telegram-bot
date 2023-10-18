package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.impl.TelegramBotServiceImpl;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.LocalDateTime.parse;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private String messageChat = "Привет.Создай напоминание, отправив сообщение в фомате - 01.01.2022 20:00 Сделать домашнюю работу";
    private static final Pattern PATTERN = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    private TelegramBot telegramBot;
    private TelegramBotServiceImpl telegramBotService;
    public TelegramBotUpdatesListener(TelegramBot telegramBot,TelegramBotServiceImpl telegramBotService){
this.telegramBot=telegramBot;
this.telegramBotService=telegramBotService;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
        updates.forEach(update -> {
            String text=update.message().text();
            logger.info("Processing update: {}", update);
            if (update.message().text().equals("/start")) {
                SendMessage message = new SendMessage
                        (update.message().chat().id(), messageChat);
                SendResponse response = telegramBot.execute(message);
            } else {
                Matcher matcher = PATTERN.matcher(text);
                if (matcher.find()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1),FORMATTER);
                    NotificationTask notificationTask =
                            new NotificationTask(telegramBotService.count(), update.message().chat().id(), matcher.group(3), localDateTime.truncatedTo(ChronoUnit.MINUTES));
                    telegramBotService.create(notificationTask);
                    SendMessage message = new SendMessage
                            (update.message().chat().id(), "Напоминание " + update.message().text() + " Создано");
                    SendResponse response = telegramBot.execute(message);
                }else {
                    SendMessage message = new SendMessage
                        (update.message().chat().id(), "Вы допустили ошибку при создании напоминания");
                    SendResponse response = telegramBot.execute(message);}
            }
        });
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void performTask(){
        telegramBotService.findTask(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .forEach(notificationTask -> {
           SendMessage message = new SendMessage
             (notificationTask.getChatId(), notificationTask.getNotification());
         SendResponse response = telegramBot.execute(message);
                    telegramBotService.delete(notificationTask);
         });
    }

}
