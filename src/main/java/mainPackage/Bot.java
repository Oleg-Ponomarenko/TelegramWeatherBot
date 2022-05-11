package mainPackage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.myChatId}")
    private int myChatId;

    @Value("${weatherApiKey}")
    private String key;

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    private String getWeatherData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.weatherapi.com/v1/current.json?key=" + key + "&q=auto:ip&aqi=no", String.class);
        return response.getBody();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(myChatId));
            if (update.getMessage().getText().equals("Погода")) {
                message.setText(getWeatherData());
            }
            else {
                message.setText(update.getMessage().getText());
            }
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
