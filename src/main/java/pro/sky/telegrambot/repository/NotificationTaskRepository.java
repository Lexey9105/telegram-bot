package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask,Long>{
    Collection<NotificationTask> findNotificationTaskByDuration(LocalDateTime localDateTime);
}
