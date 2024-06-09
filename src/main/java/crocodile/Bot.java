package crocodile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private boolean guessed = false;
    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;

    @Override
    public String getBotUsername() {
        return "CS_Crocodile_Bot";
    }

    @Override
    public String getBotToken() {
        return "7227775814:AAEyKsNloqYzlClmqCFuGyuVmz_xp0UHjXg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleCommands(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                                    .chatId(who.toString())
                                    .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Choose an option:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(createButtonRow("see", Commands.SEE.getCommand()));
        rowsInline.add(createButtonRow("next", Commands.NEXT.getCommand()));

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> createButtonRow(String buttonText, String callbackData) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(callbackData);
        rowInline.add(button);
        return rowInline;
    }


    /* individual command handlers */
    private void nextWord(Long id) {
        sendText(id, "Next word");
    }

    private void seeWord(Long id) {
        sendText(id, "See word");
    }

    private void start(Long id, Message msg) {
        if(msg.hasText())
            sendText(id, "Welcome to CS Crocodile Game");
    }

    private void help(Long id, Message msg) {
        if(msg.hasText())
            sendText(id, "We have the following commands to navigate in the game");
    }


    /* callback and command handlers */
    public void handleCommands(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

        if(msg.isCommand()){
            Commands command = Commands.fromString(msg.getText());
            switch(command) {
                case START: start(id, msg); break;
                case HELP: help(id, msg); break;
                case SEE: seeWord(id); break;
                case NEXT: nextWord(id); break;
                case MENU: sendMenu(id); break;
                default: sendText(id, "Sorry, no such command exists"); break;
            }
        }
    }


    private void handleCallbackQuery(Update update) {
        var callbackQuery = update.getCallbackQuery();
        var callbackData = callbackQuery.getData();
        var userId = callbackQuery.getFrom().getId();

        Commands command = Commands.fromString(callbackData);
        if (command != null) {
            switch (command) {
                case SEE: seeWord(userId); break;
                case NEXT: nextWord(userId); break;
                default:
                    sendText(userId, "Unknown callback data");
                    break;
            }
        } else {
            sendText(userId, "Invalid callback data");
        }
    }
}
