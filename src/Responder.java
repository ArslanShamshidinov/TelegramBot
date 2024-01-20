import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Responder extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String defMess = "I'm sorry, I haven't understood the message you send";

        Long chatId = update.getMessage().getChatId();

        SendMessage botMessage = new SendMessage();
        botMessage.setChatId(chatId);
        botMessage.setText(defMess);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText().trim().toLowerCase();
            if (userMessage.equalsIgnoreCase("hello")) {
                botMessage.setText("Hello " + update.getMessage().getFrom().getFirstName() + "\nHow are you ?");
            }
            if (userMessage.equalsIgnoreCase("how are you ?")) {
                botMessage.setText("I'm fine, thanks for asking");
            }

            if (userMessage.contains("time")) {
                botMessage.setText("Would you like to know the current time?");

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> buttonsBoard = new ArrayList<>();
                List<InlineKeyboardButton> row1 = new ArrayList<>();


                InlineKeyboardButton yesButton = new InlineKeyboardButton();
                yesButton.setText("Yes");
                yesButton.setCallbackData("/YES");

                row1.add(yesButton);
                buttonsBoard.add(row1);

                keyboardMarkup.setKeyboard(buttonsBoard);

                botMessage.setReplyMarkup(keyboardMarkup);

            }
        }


        try {
            sendApiMethod(botMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return Bot.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Bot.USERNAME;
    }
}
