package me.ics.questplugin;

import me.ics.questplugin.HelpClasses.FoodChange;
import me.ics.questplugin.HelpClasses.MusicSaver;
import me.ics.questplugin.OSclasses.PlayerClickOnComp;
import me.ics.questplugin.ArmorHolo.SetArmorHolo;
import me.ics.questplugin.ArraySorterMine.ListenerArray;
import me.ics.questplugin.Buttons.Buttons;
import me.ics.questplugin.Buttons.DelButton;
import me.ics.questplugin.Buttons.ListenerButton;
import me.ics.questplugin.Buttons.SetButton;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FrameItemChoose.ListenerChestClick;
import me.ics.questplugin.OSclasses.PlayerCloseInventory;
import me.ics.questplugin.OSclasses.PlayerComputerInteract;
import me.ics.questplugin.QuestManager.Commands.*;
import me.ics.questplugin.QuestManager.Listeners.*;
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
    private String fileQuest = "/quest_worlds_data";
    private FileJsonEditor<ListQuestWorldData> editorQuest =
            new FileJsonEditor<>("/quest_worlds_data", new ListQuestWorldData(), this);

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
        extraModules();
        osClasses();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void extraModules(){
        getCommand("setholo").setExecutor(new SetArmorHolo());
        getServer().getPluginManager().registerEvents(new ListenerArray(this, "/buttons_data.json", "/quest_worlds_data"), this);
        getServer().getPluginManager().registerEvents(new ListenerChestClick(editorQuest), this);
        getServer().getPluginManager().registerEvents(new FoodChange(), this);
        getServer().getPluginManager().registerEvents(new ArmorManipulate(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new MusicSaver(this), this);
    }

    private void onEnableQuestManager() {

        String fileAnswers = "/answers.json";

        getCommand("setcheckpoint").setExecutor(new SetCheckpoint(this, fileQuest));
        getCommand("answer").setExecutor(new Answer(this, fileQuest, fileAnswers));
        getCommand("quest").setExecutor(new QuestOperator(this, fileQuest));
        getCommand("addanswer").setExecutor(new AddAnswer(this, fileAnswers));
        getCommand("finish").setExecutor(new FinishCommand(this, fileQuest));

        getServer().getPluginManager().registerEvents(new PlayerTeleport(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerThrow(), this);
        getServer().getPluginManager().registerEvents(new PlayerPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerOut(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerClick(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerLogin(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryInteract(this,fileQuest),this);
        getServer().getPluginManager().registerEvents(new PlayerClickOnComp(this,fileQuest),this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this,
                fileQuest, "/txt_warps_data.json"), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|| QUEST MANAGER ||");
    }

    private void onEnableVR() {
        getCommand("vrhelmet").setExecutor(new SetVrHelmetButton(this));
        getCommand("vrpos").setExecutor(new SetVrPos(this));
        getServer().getPluginManager().registerEvents(new ListenerVR(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|| VR HELMET ||");
    }

    private void onEnableTxt() {
        String fileName = "/txt_warps_data.json";
        getCommand("settxtwarp").setExecutor(new SetTxtWarp(this, fileName));
        getCommand("deltxtwarp").setExecutor(new DelTxtWarp(this, fileName));
        getCommand("txtwarps").setExecutor(new TxtWarps(this, fileName));
        getServer().getPluginManager().registerEvents(new ListenerTxt(this, fileName), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|| WARP TXT  ||");
    }

    private void onEnableTp() {
        getCommand("settpwarp").setExecutor(new SetTpWarp(this));
        getCommand("deltpwarp").setExecutor(new DelTpWarp(this));
        getCommand("tpwarps").setExecutor(new TpWarps(this));
        getServer().getPluginManager().registerEvents(new ListenerTp(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|| WARP TP   ||");
    }

    private void onEnableButtons() {
        String file = "/buttons_data.json";
        getCommand("setbutton").setExecutor(new SetButton(this, file));
        getCommand("delbutton").setExecutor(new DelButton(this));
        getCommand("buttons").setExecutor(new Buttons(this));
        getServer().getPluginManager().registerEvents(new ListenerButton(this, file, fileQuest), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|| BUTTONS   ||");
    }

    private void osClasses() {
        getServer().getPluginManager().registerEvents(new PlayerComputerInteract(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerClickOnComp(this, fileQuest), this);
        getServer().getPluginManager().registerEvents(new PlayerCloseInventory(this, fileQuest), this);
    }

    private void loadImage() {
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
