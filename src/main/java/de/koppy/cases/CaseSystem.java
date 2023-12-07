package de.koppy.cases;

import de.koppy.cases.api.Case;
import de.koppy.cases.api.CaseItem;
import de.koppy.cases.api.Rarity;
import de.koppy.cases.api.Type;
import de.koppy.cases.commands.CaseCmd;
import de.koppy.cases.listener.CaseEvents;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.mysql.api.Table;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CaseSystem implements SubSystem {

    private static Table table;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new CaseEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("case", new CaseCmd());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();

        Case beta = new Case("Beta", new CaseItem(new ItemStack(Material.GOLD_NUGGET), "§3Small amount of Money", "§egives you 25 Coins", Type.MONEY, 25.0d, Rarity.COMMON), true);
        beta.addItem(new CaseItem(new ItemStack(Material.GOLD_INGOT), "§3Medium amount of Money", "§egives you 100 Coins", Type.MONEY, 100.0d, Rarity.UNCOMMON));
        beta.addItem(new CaseItem(new ItemStack(Material.GOLD_BLOCK), "§3Big amount of Money", "§egives you 1000 Coins", Type.MONEY, 1000.0d, Rarity.EPIC));
        beta.addItem(new CaseItem(new ItemStack(Material.RED_SHULKER_BOX), "§3One more home", "§eGives you one more home", Type.HOME, 1, Rarity.UNCOMMON));
        beta.addItem(new CaseItem(new ItemStack(Material.RED_SHULKER_BOX, 2), "§3two more homes", "§eGives you two more homes", Type.HOME, 2, Rarity.RARE));
        beta.addItem(new CaseItem(new ItemStack(Material.GREEN_BANNER), "§3one more Warptoken", "§eGives you one more warptoken", Type.USERWARP, 2, Rarity.LEGENDARY));
        beta.addItem(new CaseItem(new ItemStack(Material.OAK_HANGING_SIGN), "§3Maxland +1", "§eGives you one more maxland", Type.LAND, 1, Rarity.LEGENDARY));

        Case basic = new Case("Basic", new CaseItem(new ItemStack(Material.GOLD_NUGGET), "§3Small amount of money", "§egives you 100 Coins", Type.MONEY, 100d, Rarity.COMMON), false);
        basic.addItem(new CaseItem(new ItemStack(Material.OAK_LOG, 32), "§3Oak_Logs x32", "§egives you 32 Oak_Logs", Type.ITEM, new ItemStack(Material.OAK_LOG, 32), Rarity.COMMON));
        basic.addItem(new CaseItem(new ItemStack(Material.GOLD_INGOT), "§3Medium amount of money", "§egives you 2.500 Coins", Type.MONEY, 2500d, Rarity.UNCOMMON));
        basic.addItem(new CaseItem(new ItemStack(Material.RAW_GOLD_BLOCK), "§3Big amount of money", "§egives you 5.000 Coins", Type.MONEY, 10000d, Rarity.RARE));
        basic.addItem(new CaseItem(new ItemStack(Material.GOLD_BLOCK), "§3Big amount of money", "§egives you 10.000 Coins", Type.MONEY, 10000d, Rarity.EPIC));
        basic.addItem(new CaseItem(new ItemStack(Material.OAK_HANGING_SIGN), "§3Maxland +1", "§egives you one more land", Type.LAND, 1, Rarity.LEGENDARY));
        basic.addItem(new CaseItem(new ItemStack(Material.CYAN_CANDLE), "§3Warptoken +1", "§egives you one more warptoken", Type.USERWARP, 1, Rarity.LEGENDARY));


        table = new Table("cases", new Column("uuid", ColumnType.VARCHAR, 200));
        for(Case c : Case.cases) {
            if(c.needsKey()) {
                table.addColumn(new Column(c.getName(), ColumnType.INT, 200));
            }
        }
        table.createTable();

    }

    public static String getPrefix() {
        return LunaniaSystem.getServerInstance().getPrefix();
    }

    public static Table getTable() {
        return table;
    }
}
