package de.koppy.basics.api;

public enum Language {

    GERMAN, ENGLISH;

    public static boolean isLanguage(String language) {
        for(Language lang : Language.values()) {
            if(lang.toString().equalsIgnoreCase(language)) {
                return true;
            }
        }
        return false;
    }

    public static Language fromString(String language) {
        for(Language lang : Language.values()) {
            if(lang.toString().equalsIgnoreCase(language)) {
                return lang;
            }
        }
        return ENGLISH;
    }

}