package com.desmond.android.mytime10.util;

/**
 * Created by gk on 15/9/28.
 */
public enum ProrityEnum {
    IE("0", "重要紧急"),
    INE("1", "重要不紧急"),
    ENI("2", "紧急不重要"),
    NINE("3", "不紧急不重要");
    private String prority;
    private String name;

    public String getPrority() {
        return prority;
    }

    public void setPrority(String prority) {
        this.prority = prority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ProrityEnum(String prority, String name) {
        this.prority = prority;
        this.name = name;
    }

    public static ProrityEnum getByPrority(String prority) {
        for(ProrityEnum p : ProrityEnum.values()) {
            if(p.getPrority().equals(prority)) {
                return p;
            }
        }

        return ProrityEnum.NINE;
    }
}
