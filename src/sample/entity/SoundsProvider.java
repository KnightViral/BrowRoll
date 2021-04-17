package sample.entity;

import sample.Utils;

public class SoundsProvider {
    private final static String[] END_SOUNDS = new String[]{"300.wav", "Deep dark fantasies.wav", "fuck you.wav", "Iam an artist.wav", "NANI.wav", "Omae wa mou shindeiru.wav", "Spank.wav", "WOO.wav", "YES I AM.wav", "YES YES YES YES YES.wav", "Oh no!.wav"};

    public static String getSound() {
        return Utils.getRandomStringFromArray(END_SOUNDS);
    }
}
