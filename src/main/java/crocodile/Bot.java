package crocodile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();

        var see = InlineKeyboardButton.builder()
                .text("See").callbackData("see")
                .build();

        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();
        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(see)).build();

        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        var txt = msg.getText();

        if(msg.isCommand()){
            if(msg.getText().equals("/see"))
                guessed = false;
            else if (msg.getText().equals("/next"))
                guessed = true;
            else if (txt.equals("/menu"))
                sendMenu(id, "<b>Menu 1</b>", keyboardM1);

        }
//
//        if(guessed)
//            sendText(id, "Yeayy, you guessed the word!");
//        else
//            sendText(id, "Try more, bro");
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                                    .chatId(who.toString())
                                    .text(what).build();
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(who.toString()));
        sendPhotoRequest.setPhoto(new InputFile("https://www.google.com/imgres?q=cat&imgurl=https%3A%2F%2Fcdn.britannica.com%2F70%2F234870-050-D4D024BB%2FOrange-colored-cat-yawns-displaying-teeth.jpg&imgrefurl=https%3A%2F%2Fwww.britannica.com%2Fanimal%2Fcat&docid=Bvzzy2OOLWm60M&tbnid=zWdzdPdo-A-wdM&vet=12ahUKEwint7yBx8qGAxVZ_7sIHdOBBS4QM3oECBUQAA..i&w=1600&h=1146&hcb=2&itg=1&ved=2ahUKEwint7yBx8qGAxVZ_7sIHdOBBS4QM3oECBUQAA"));
        sendPhotoRequest.setCaption("hehe");
        try {
            execute(sm);
//            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                                    .parseMode("HTML").text(txt)
                                    .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
