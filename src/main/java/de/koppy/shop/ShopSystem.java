package de.koppy.shop;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.SubSystem;
import de.koppy.shop.commands.Shop;
import de.koppy.shop.listener.ShopListener;

public class ShopSystem implements SubSystem {
    //TODO: Implementing UserShop-System
    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new ShopListener());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("shop", new Shop());
        LunaniaSystem.registerCommand("store", new Shop());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
    }
}
