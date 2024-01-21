import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Responder extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String defMess = "I'm sorry, I haven't understood the message you send";

        Long chatId = 0L;

        SendMessage botMessage = new SendMessage();
        botMessage.setText(defMess);

        if(update.hasCallbackQuery() && update.getCallbackQuery().getData() != null && !update.getCallbackQuery().getData().isEmpty()){
            chatId = update.getCallbackQuery().getMessage().getChatId();

            String callBackData = update.getCallbackQuery().getData();

            if(callBackData.equalsIgnoreCase(CallBackData.CD_YES.toString())){
                botMessage.setText(String.valueOf(LocalDateTime.now()));

            }

            if (callBackData.equalsIgnoreCase(CallBackData.CD_NO.toString())){
                botMessage.setText("Ok, No problem");
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            String userMessage = update.getMessage().getText().trim().toLowerCase();
            if (userMessage.equalsIgnoreCase("hello")) {
                botMessage.setText("Hello " + update.getMessage().getFrom().getFirstName() + "\nHow are you ?");
            }
            if (userMessage.equalsIgnoreCase("how are you ?")) {
                botMessage.setText("I'm fine, thanks for asking");
            }

            if (userMessage.contains("time")||userMessage.contains("/time")) {
                botMessage.setText("Would you like to know the current time?");

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> buttonsBoard = new ArrayList<>();
                List<InlineKeyboardButton> row1 = new ArrayList<>();


                InlineKeyboardButton yesButton = new InlineKeyboardButton();
                yesButton.setText("Yes");
                yesButton.setCallbackData(CallBackData.CD_YES.toString());

                InlineKeyboardButton noButton = new InlineKeyboardButton();
                noButton.setText("No thanks");
                noButton.setCallbackData(CallBackData.CD_NO.toString());

                row1.add(yesButton);
                row1.add(noButton);
                buttonsBoard.add(row1);

                keyboardMarkup.setKeyboard(buttonsBoard);

                botMessage.setReplyMarkup(keyboardMarkup);

            }

            if(userMessage.equalsIgnoreCase("/day")){
                botMessage.setText(LocalDateTime.now().getDayOfWeek().toString());
            }
        }


        if (chatId == 0){
            throw new IllegalStateException("The chat id couldn't be identified or found");
        }

        botMessage.setChatId(chatId);

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
