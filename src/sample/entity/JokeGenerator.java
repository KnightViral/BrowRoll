package sample.entity;

import javafx.scene.control.TableView;
import sample.Utils;

public class JokeGenerator {

    private final static int JOKE_ID = 9999;

    public static WheelPoint getJoke(TableView<WheelPoint> mainTable) {
        int rndNum = 13;
        if (mainTable.getItems().size() > 0)
            rndNum++;
        int rnd = Utils.getRandomTo(rndNum);
        switch (rnd) {
            case 0:
                return new WheelPoint(JOKE_ID, "Анус себе подкрути, пес. Keepo", 0);
            case 1: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Стример - писька.", 0);
                else
                    return new WheelPoint(JOKE_ID, String.format("Я заготовил %s фраз, все не перекрутите. (нет)", Utils.getRandomBetween(1451, 5552)), 0);
            }
            case 2:
                return new WheelPoint(JOKE_ID, "Хватит, голова кружится.", 0);
            case 3: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Анимешники не человеки.", 0);
                else
                    return new WheelPoint(JOKE_ID, "Анимешники - сверхчеловеки.", 0);
            }
            case 4: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Ваше очко уходит в зрительный зал.", 0);
                else
                    return new WheelPoint(JOKE_ID, "Зрительный зал уходит в ваше очко.", 0);
            }
            case 5:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая вайфу. <3", WaifuProvider.getWaifu()), 0);
            case 6:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая игра.", GamesProvider.getGame()), 0);
            case 7:
                return new WheelPoint(JOKE_ID, getRandomGameName(mainTable), 999);
            case 8:
                return new WheelPoint(JOKE_ID, "Осуждаю", 0);
            case 9:
                return new WheelPoint(JOKE_ID, "Кто спросил:\"Что делает подкрутка?\", тот выигрывает таймач.", 0);
            case 10:
                return new WheelPoint(JOKE_ID, "Ндиди", 0);
            case 11:
                return new WheelPoint(JOKE_ID, "+игра на все платформы", 0);
            case 12:
                return new WheelPoint(JOKE_ID, AnekProvider.getAnek(), 0);
            case 13:
                return new WheelPoint(JOKE_ID, AnekProvider.getAnek(), 0);
            case 14:
                return new WheelPoint(JOKE_ID, String.format("%s - ну и говно, кто это заказал?", getRandomNameFromTable(mainTable)), -999);
            default:
                return new WheelPoint(JOKE_ID, getRandomGameName(mainTable), 999);
        }
    }

    public static int getJokeId() {
        return JOKE_ID;
    }

    private static String getRandomNameFromTable(TableView<WheelPoint> mainTable) {
        return mainTable.getItems().get(Utils.getRandomTo(mainTable.getItems().size() - 1)).getName();
    }

    private static String getRandomGameName(TableView<WheelPoint> mainTable) {
        if (mainTable.getItems().size() == 0) {
            return GamesProvider.getGame();
        } else {
            if (Utils.getRandomTo(1) == 0)
                return GamesProvider.getGame();
            else
                return getRandomNameFromTable(mainTable);
        }
    }
}
