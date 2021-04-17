package sample.entity;

import sample.Utils;

public class AnekProvider {

    private final static String[] ANEK_START = new String[]{
            "Как-то раз %s решил выбрать себе псевдоним",
            "Нашел %s шляпу",
            "%s так и не научился печь хлеб",
            "Пришли как-то %s и Лупа получать зарплату",
            "Идет %s, видит — подкова на дороге",
            "Идет %s по пустыне",
            "Пошел %s, купил ваксы, лицо натёр, тело натёр"
    };
    private final static String[] ANEK_END = new String[]{
            "с тех под так и подписывал свои книги - %s.",
            "а она ему как раз.",
            "и выкинул, нахуй, c обрыва.",
            "а %s в Щепки.",
            "засунул себе в ухо и оглох.",
            "и ебанулся с лошади насмерть.",
            "схватил пузырь и убежал.",
            "\"понятно\", сказал %s и сломал ему ногу.",
            "%s не того стримера разбудил.",
            "и у него отвалилась жопа."
    };

    private static String getStartAnek() {
        return Utils.getRandomStringFromArray(ANEK_START);
    }

    private static String getEndAnek() {
        return Utils.getRandomStringFromArray(ANEK_END);
    }

    public static String getAnek() {
        return String.format(getStartAnek(), WaifuProvider.getWaifu()) + ", " + String.format(getEndAnek(), WaifuProvider.getWaifu());
    }

}
