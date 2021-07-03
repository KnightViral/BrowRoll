package sample.entity;

import sample.Utils;

public class SoundsProvider {
    private final static String[] END_SOUNDS = new String[]{"300.wav", "Deep dark fantasies.wav", "fuck you.wav", "Iam an artist.wav", "NANI.wav", "Omae wa mou shindeiru.wav", "Spank.wav", "WOO.wav", "YES I AM.wav", "YES YES YES YES YES.wav", "Oh no!.wav"};

    private final static String[] MK_EAT_SOUNDS = new String[] {
            "eat1.wav",
            "eat2.wav",
            "eat3.wav",
            "eat4.wav",
            "eat5.wav",
            "eat6.wav"
    };

    private final static String[] MK_HIT_SOUNDS = new String[] {
            "hit1.wav",
            "hit2.wav",
            "hit3.wav",
            "hit4.wav",
            "hit5.wav",
    };

    public static String getSound() {
        return Utils.getRandomStringFromArray(END_SOUNDS);
    }

    public static String getSlap() {
        return "Spank.wav";
    }

    public static String getMKEat() {
        return "mksounds/" + Utils.getRandomStringFromArray(MK_EAT_SOUNDS);
    }

    public static String getMKHit() {
        return "mksounds/" + Utils.getRandomStringFromArray(MK_HIT_SOUNDS);
    }

    public static String getMKIWin () {
        return "mksounds/win.wav";
    }

    public static String getMKChoose () {
        return "mksounds/choose.wav";
    }

    public static String getMKMusic () {
        return "mksounds/music.wav";
    }
}
