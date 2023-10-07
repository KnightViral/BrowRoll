package sample.entity;

public class StyleProvider {

    private static Owners globalOwner = Owners.BROWJEY;

    public enum Owners {
        BROWJEY,
        UNCLEBJORN,
        NUKE
    }

    public static void setOwner(Owners owner) {
        globalOwner = owner;
    }

    public static String getAppIcon() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "bjornFat.jpg";
            case NUKE:
                return "nukeIcon.png";
            case BROWJEY:
            default:
                return "browFat.jpg";
        }
    }

    public static String getSquarePic() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "bjornFat.jpg";
            case NUKE:
                return "nukeFat.jpg";
            case BROWJEY:
            default:
                return "browFat.jpg";
        }
    }

    public static String getRollPic() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "bjornRoll.png";
            case NUKE:
                return "nukeRoll.png";
            case BROWJEY:
            default:
                return "browFatRoll.png";
        }
    }

    public static String getDuelRollPic() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "bjornDuelRoll.png";
            case NUKE:
                return "nukeDuelRoll.png";
            case BROWJEY:
            default:
                return "browBlameRoll.png";
        }
    }

    public static String getTimerEndLabel() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "bjornPaint";
            case NUKE:
                return "nukeStare";
            case BROWJEY:
            default:
                return "BrowFat";
        }
    }

    public static String getStyle() {
        switch (globalOwner) {
            case UNCLEBJORN:
                return "/styles/uncleStyle.css";
            case NUKE:
                return "/styles/nukeStyle.css";
            case BROWJEY:
            default:
                return "/styles/browStyle.css";
        }
    }
}
