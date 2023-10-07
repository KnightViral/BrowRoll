package sample.entity;

import sample.Utils;

public class WaifuProvider {

    private final static String[] WAIFUS = new String[]{"Спидвагон", "Брови", "Гит", "Нюк", "Мэд", "Бьёрн", "Варан", "Пепе", "Ндиди", "Дилюк"};

    public static String getWaifu() {
        return Utils.getRandomStringFromArray(WAIFUS);
    }

}
