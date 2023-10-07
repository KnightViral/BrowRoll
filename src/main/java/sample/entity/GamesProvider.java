package sample.entity;

import sample.Utils;

public class GamesProvider {

    private final static String[] GAMES = new String[]{"DOTA 2", "World of Tanks", "World of Warcraft", "Stormlord", "Chakan: The Forever Man", "The Lust of Ass 2", "Fortnite", "PUBG", "COD:Warzone", "Пук среньк Fallout 76", "Kappa в чат", "Шахматы", "Крестики-нолики", "Сапер", "Косынка", "Пить Йод", "Крутить подкрутку"};

    public static String getGame() {
        return Utils.getRandomStringFromArray(GAMES);
    }
}
