package info.bcdev.telegramwallet.bot.session;

import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.BotAccess;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.bot.TransactionInstance;
import info.bcdev.telegramwallet.ethereum.WalletsInstance;

import java.util.List;

public class Session {

    public static SessionPage SESSION_PAGE;
    public static SendType SEND_TYPE;
    public static SendStep SEND_STEP;
    public static BotAccess BOT_ACCESS;
    public static List<WalletsInstance> WALLET_INSTANCE_LIST;

    public static TransactionInstance SEND_INSTANCE = new TransactionInstance();

    public static String ACTIVE_WALLET;
    public static String DELETE_STEP;

    public static Settings SETTINGS;
    public static Tbot BOT_INSTANCE;
}
