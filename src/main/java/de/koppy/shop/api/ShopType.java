package de.koppy.shop.api;

public enum ShopType {
    DIRECTBUY, BULKBUY;

    public static ShopType fromString(String string) {
        for (ShopType st : ShopType.values()) {
            if (st.toString().equals(string)) {
                return st;
            }
        }
        return ShopType.DIRECTBUY;
    }
}
