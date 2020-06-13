package me.ics.questplugin;

import me.ics.questplugin.Buttons.Buttons;
import me.ics.questplugin.Buttons.DelButton;
import me.ics.questplugin.Buttons.ListenerButton;
import me.ics.questplugin.Buttons.SetButton;
import me.ics.questplugin.QuestManager.Commands.Answer;
import me.ics.questplugin.QuestManager.Commands.QuestOperator;
import me.ics.questplugin.QuestManager.Commands.SetCheckpoint;
import me.ics.questplugin.QuestManager.Listeners.PlayerClick;
import me.ics.questplugin.QuestManager.Listeners.PlayerMove;
import me.ics.questplugin.QuestManager.Listeners.PlayerOut;
import me.ics.questplugin.TpWarp.DelTpWarp;
import me.ics.questplugin.TpWarp.SetTpWarp;
import me.ics.questplugin.TpWarp.ListenerTp;
import me.ics.questplugin.TpWarp.TpWarps;
import me.ics.questplugin.TxtWarp.DelTxtWarp;
import me.ics.questplugin.TxtWarp.ListenerTxt;
import me.ics.questplugin.TxtWarp.SetTxtWarp;
import me.ics.questplugin.TxtWarp.TxtWarps;
import me.ics.questplugin.VrHelmet.ListenerVR;
import me.ics.questplugin.VrHelmet.SetVrHelmetButton;
import me.ics.questplugin.VrHelmet.SetVrPos;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadImage();
        loadConfig();
        onEnableVR();
        onEnableTxt();
        onEnableTp();
        onEnableButtons();
        onEnableQuestManager();
    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void onEnableQuestManager(){
        getCommand("setcheckpoint").setExecutor(new SetCheckpoint(this, "/quest_worlds_data"));
        getCommand("answer").setExecutor(new Answer());
        getCommand("quest").setExecutor(new QuestOperator(this, "/quest_worlds_data"));

        getServer().getPluginManager().registerEvents(new PlayerOut(this, "/quest_worlds_data"), this);
        getServer().getPluginManager().registerEvents(new PlayerClick(this, "/quest_worlds_data"), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this, "/quest_worlds_data"), this);
<<<<<<< HEAD
        getServer().getPluginManager().registerEvents(new PlayerLogin(this, "/quest_worlds_data"), this);
        getServer().getPluginManager().registerEvents(new PlayerLogin(this, "/quest_worlds_data"), this);
        getServer().getPluginManager().registerEvents(new PlayerThrow(this ), this);
        getServer().getPluginManager().registerEvents(new PlayerPlace(this ), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveItems(this),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleport(this, "/quest_worlds_data"),this);
=======
>>>>>>> 137eea44d356776001d8634419a8937ee766dec7
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN +  "|| QUEST MANAGER ||");
    }

    private void onEnableVR(){
        getCommand("vrhelmet").setExecutor(new SetVrHelmetButton(this));
        getCommand("vrpos").setExecutor(new SetVrPos(this));
        getServer().getPluginManager().registerEvents(new ListenerVR(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN +  "|| VR HELMET ||");
    }

    private void onEnableTxt(){
        getCommand("settxtwarp").setExecutor(new SetTxtWarp(this));
        getCommand("deltxtwarp").setExecutor(new DelTxtWarp(this));
        getCommand("txtwarps").setExecutor(new TxtWarps(this));
        getServer().getPluginManager().registerEvents(new ListenerTxt(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN +  "|| WARP TXT  ||");
    }

    private void onEnableTp(){
        getCommand("settpwarp").setExecutor(new SetTpWarp(this));
        getCommand("deltpwarp").setExecutor(new DelTpWarp(this));
        getCommand("tpwarps").setExecutor(new TpWarps(this));
        getServer().getPluginManager().registerEvents(new ListenerTp(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN +  "|| WARP TP   ||");
    }

    private void onEnableButtons(){
        getCommand("setbutton").setExecutor(new SetButton(this));
        getCommand("delbutton").setExecutor(new DelButton(this));
        getCommand("buttons").setExecutor(new Buttons(this));
        getServer().getPluginManager().registerEvents(new ListenerButton(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN +  "|| BUTTONS   ||");
    }

    private void loadImage(){
        getServer().getConsoleSender().sendMessage(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=+++++%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%++++++++++++=@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=++++++=@@@@%+++++++%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%+++++++%@@@@@@@@@@@+++++*+=@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@=++++++=@@@@@@@@@@@@@@@@@@%+++*+*+%@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@%@@@@@@%+++++++%@@@@@@@@@@@@@@@@@@@@@@@@@+*+****=@@@@%@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@%@@=*++++*=@@@@@@@@@@@@@@@@@+++*%@@@@@@@@@@%*******%@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@+++++++%@@@@@@@@@@@@@@@@@@@@+******+%@@@@@@@@@@+******=@@@@@@%@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@%+++++++@@@@@@@@@@@@@@@@@@@@@@@@***********%@@@@@@@@@@=***:**+%@@@%@@@@@@@@@\n" +
                "@@@@@@@@@@@=+++++*%@@@@@@@@@@@@@@@@@@@@@@@@@@=**************+@@@@@%@@@@@*:***::=@@@@@@@@@@\n" +
                "@@@@@@@%+++*+++%@@@@@@@@@%+***@@=*******************************%@@@%@@@@@@=:::::-*@@@@@@@\n" +
                "@@@@@%*+*++=@@@@@@@@@@+**++**%@*********************************:::*@@%@@@@%@@@::::::@@@@@\n" +
                "@@@@%+++*%@%@@@%@@@****+****%@%**************************::::::::::::::%@%@@@@@@@*::::@@%@\n" +
                "@@@@*+++%@@@@@@=************@@%********************:::::::::::::::::::::::*%@@@@@@*:::@@@@\n" +
                "@@@@*+**@@@@@%*+************@@%****************::::::::::::::::::::::::::::=@@@@@@%::-@@@@\n" +
                "@@@@****@@@@=***************@@%**********:::::::::::::::::::::::::::::::%@@@@@@@@@=:::@@@@\n" +
                "@@@@****@@@@+***************@@%*******:::::::::::::::::::::::::::::::%@@@%@+:-:%@@=::-@@@@\n" +
                "@@@@****@@@@****************@@%*:::::::::::::::::::::::::::::::::*@@@@@@+::::::=@@=:::@@@@\n" +
                "@@@@****@@@@**************::@@@::::::::::::::::::::::::::::::-+@@@@@@*::-::::::=@@=:::@@@@\n" +
                "@@@@****@@@@*********:::::::@@@@@@@@@@@@@@@@@@=::::::::::::=@@%@@%::-::::::::::=@@%:::@@@@\n" +
                "@@@@****@@@@:***::::::::::::@@@@@@@@@@@@@@@@@@=:::::::-:%@@@@@@*:::::::::::::::=@@%:::@@@@\n" +
                "@@@@****@@@@::::::::::::::::@@@@@@@@@@@@@@@@@@=::::::%@@@@@@@@@::::::::::::::::=@@%:::@@@@\n" +
                "@@@@****@@@@::::::::::::::::@@@@@@@@@@@@@@@@@@=::*%@@@@@@@@@@@@*:::::::::::::::%@@%:::@@@@\n" +
                "@@@@::::@@@@::::::::::::::::@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@::::::::::::::::%@@%:::@@@@\n" +
                "@@@@::::@@@@::::::::::::::::@@@@@@@@@@@@@=::*@@@@@@@@@@@@@@@@@@::::::::::::::::%@@%**:@@@@\n" +
                "@@@@::::@@@%::::::::::::::::%@@@@@@@@@+-::::*@@@@@@@@@@@@@@@@@@::::::::::::::::%@@%***@@@@\n" +
                "@@@@::::@@@%::::::::::::::::%@@@@@@+-:::::::*@@@@@@@@@@@@@@@@@@*:::::::::::*:::%@@%***%@@@\n" +
                "@@@@::::@@@@:::::::::::::+@@@@@@*:::::::::::+@@@@@@@@@@@@@%@@@@*:::::**********@@@%***%@@@\n" +
                "@@@@::::@@@%::::::::::=@@@@@%:-:::::::::::::::::::::::::::::%@@****************@@@%***%@@@\n" +
                "@@@@::::@@@@:::::::%@@%@@%::::::::::::::::::::::::::::::::::%@@****************%@@%***%@@@\n" +
                "@@@@::::@@@@:-::=@@@@@+::::::::::::::::::::::::::::::*******%@@****************@@@%***%@@@\n" +
                "@@@@:::-@@@@@@@@@@@+-::::::::::::::::::::::::::::***********%@@+**************+@@@@*+*%@@@\n" +
                "@@@@::::@@@@@@@@*:::::::::::::::::::::::::::****************%@@+*************+@@@@@+**%@@@\n" +
                "@@@@::::%@@@@@@@*:::::::::::::::::::::**********************%@@+*********+**@@@@@@+++*%@@@\n" +
                "@@@@%:::-%@@@@@@@@@=:::::::::::::***************************%@@++****+**+@@%@@@@@++++*@@@@\n" +
                "@@@@@=:::::+@%@%@@@@%@@+:*:****:*************************+**%@+**++**%@@@@@@@@%++++++@@@@@\n" +
                "@@@@@@@=::::::*@@%%@@@%@@@=*******************************+%@+++*=@@@@@%%%@=+++++*=@@@@@@@\n" +
                "@@@@@@@@@@@*::::::=@@@@%@@@@@@+**************+@@@@@@@@@@@@@@@@@@@@@@%%@%+++++++%@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@=:**:*:*%@@@@@@@@@@%***********%@@@@@@@@@@@@@@@@@@@%@@%++++++=@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@*******=@@@@@@@@@@@+*******%@@@@@@@@@@@@@@@@@@@+++++++%@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@=******+@@@@@@@@@@#%*++*%@@@@@@@@@@@@@@@%+++++++%@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@%****+**%@@@@@@@@@@@@@@@@@@@@@@@@@=++++++=@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@=*++++*=@@@%@@@@@@@@@@@@%@%+++++++%@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%++++*++%@@@@@@@@@@@=++++++=@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=*++++++@@@@%+++++++=@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%++++++++++++=@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=+++++=@@@%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                "..........................................................................................\n" +
                ".........---------....................-:+**====**+:-...............-:+**===**+:-..........\n" +
                "........-=#######+...............-*=#################=-........-*=############=###=-......\n" +
                "........-=#######+............-=#####################+.......:=#################=#+.......\n" +
                "........-=#######+..........:=#########=############=.......*=######=############=........\n" +
                "........-=#######+.........*#########=*:...........:-......*=#######*-.........-+:........\n" +
                "........-=#######+.......-=########=:......................=#######*......................\n" +
                "........-=#######+.......=########*........................=#######=:.....................\n" +
                "........-=#######+......+########*.........................*########==*-..................\n" +
                "........-=#######+......=########:..........................*###########=#=+-.............\n" +
                "........-=#######+.....-=#######=............................-=#=#############=+..........\n" +
                "........-=#######+.....-=#######=...............................+=###############=-.......\n" +
                "........-=#######+.....-=########-.................................-+=#=##=######=#+......\n" +
                "........-=#######+......*########*......................................-+=#########:.....\n" +
                "........-=#######+......:#########+........................................+########*.....\n" +
                "........-=#######+.......+#########*.......................................-=######=*.....\n" +
                "........-=#######+........*=#=#####=#=:.....................*+-...........-=########:.....\n" +
                "........-=#######+.........:=#############=========#*......:###=##==***==##########+......\n" +
                "........-=#######+...........+=#####################=-.....==####################=:.......\n" +
                "........-=#######+.............-*=###################:....:##################=#=:.........\n" +
                "........-********:.................-:*==########==*+-........-+*==#######==*:.............\n" +
                "..........................................................................................");
    }
}
