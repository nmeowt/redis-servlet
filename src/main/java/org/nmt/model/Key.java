package org.nmt.model;

public enum Key {
    USER_ALL("user:all"),
    USER_DATA("user:%s:data"),
    USER_IDS("user:ids");

    private String key;
    Key(String key){
        this.key = key;
    }

    public String key(){
        return key;
    }

    public String formatted(String... value){
        return String.format(key, value);
    }
}
