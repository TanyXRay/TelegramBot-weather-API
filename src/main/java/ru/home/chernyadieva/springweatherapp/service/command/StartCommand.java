package ru.home.chernyadieva.springweatherapp.service.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.home.chernyadieva.springweatherapp.repository.entity.UserEntity;
import ru.home.chernyadieva.springweatherapp.service.UserService;
import ru.home.chernyadieva.springweatherapp.service.command.interf.Command;

@Component
public class StartCommand implements Command {

    private final UserService userService;
    private final TelegramBot telegramBotSender;

    private final static String START_MESSAGE = "Отправь в ответ в закрепе геопозицию, чтобы узнать текущую погоду.\n";

    public StartCommand(UserService userService, TelegramBot telegramBotSender) {
        this.telegramBotSender = telegramBotSender;
        this.userService = userService;
    }

    /**
     * Метод добавления нового пользователя в БД
     *
     * @param message
     */
    @Override
    public void execute(Message message) {
        Long userId = message.from().id();
        String nickname = message.from().firstName();
        String username = message.from().username();

        userService.findByUserId(userId).orElseGet(() -> {
            UserEntity user = new UserEntity();
            user.setUserId(userId);
            user.setNickname(nickname);
            user.setUsername(username);

            userService.saveUser(user);

            return user;
        });

        telegramBotSender.execute(new SendMessage(userId, START_MESSAGE));
    }
}
