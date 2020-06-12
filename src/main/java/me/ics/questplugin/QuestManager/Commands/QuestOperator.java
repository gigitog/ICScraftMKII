package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestOperator implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;

    public QuestOperator(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ListQuestWorldData listQuestWorlds = editor.getData();
        Player player = (Player) sender;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = new QuestWorldData(player.getWorld());
        if(args.length == 0) {
            if (listQuestWorlds.allQuestWorlds.size() != 0) {
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                        player.sendMessage(ChatColor.RED + "Вы уже проходили квест! Попробуйте удалить старую запись прохождения!");
                        return true;
                    }
                }
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (!questWorldData.isBusy) {
                        indexOfQuestWorld = listQuestWorlds.allQuestWorlds.indexOf(questWorldData);
                        tempQuestData = questWorldData;
                        World quest = Bukkit.getWorld(questWorldData.questWorldName);
                        questWorldData.isBusy = true;
                        questWorldData.playerName = player.getName();
                        questWorldData.ticksLivedWhenStart = player.getTicksLived();
                        questWorldData.ticksSavedBeforeLeaving = 0;
                        Location loc_spawn = new Location(quest, 193.5, 78, 428.5);
                        Arrow a = (Arrow) quest.spawnEntity(loc_spawn, EntityType.ARROW);
                        quest.setTime(12500);
                        player.teleport(loc_spawn);
                        loc_spawn.setX(0);
                        loc_spawn.setY(1);
                        loc_spawn.setZ(0);
                        loc_spawn.getBlock().setType(Material.REDSTONE_BLOCK);
                        player.setGameMode(GameMode.ADVENTURE);
                        a.addPassenger(player);
                        break;
                    }
                }
                listQuestWorlds.allQuestWorlds.remove(indexOfQuestWorld);
                listQuestWorlds.allQuestWorlds.add(indexOfQuestWorld, tempQuestData);
                editor.setData(listQuestWorlds);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Свободные миры для квеста закончились!");
            return true;
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("status")){
            String name = args[1];
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                //имя хранится как $4namer у админов
                // информация о мире и игроке
                if(questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(name)){
                    player.sendMessage(ChatColor.GREEN + "Квестовый мир - " + questWorldData.questWorldName);
                    player.sendMessage(ChatColor.GREEN + "Игрок - " + questWorldData.playerName);
                    player.sendMessage(ChatColor.GREEN + "Чекпоинт - " + questWorldData.checkpoint);
                    if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(questWorldData.playerName))){
                        if(questWorldData.ticksPlayedFinal==0){
                            player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + (questWorldData.ticksSavedBeforeLeaving + (Bukkit.getPlayer(name).getTicksLived() - questWorldData.ticksLivedWhenStart))/1200 + " мин, " + ((questWorldData.ticksSavedBeforeLeaving + (Bukkit.getPlayer(name).getTicksLived() - questWorldData.ticksLivedWhenStart))-((questWorldData.ticksSavedBeforeLeaving + (Bukkit.getPlayer(name).getTicksLived() - questWorldData.ticksLivedWhenStart))/1200)*1200)/20 + " сек.");
                        }else player.sendMessage(ChatColor.GREEN + "Квест пройден за - " + questWorldData.ticksPlayedFinal /1200 + " мин., " + questWorldData.ticksPlayedFinal%1200/20 + " сек.");
                    }else{
                        if(questWorldData.ticksPlayedFinal == 0){
                            player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + questWorldData.ticksSavedBeforeLeaving/1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving%1200)/20 + " сек.");
                        }else player.sendMessage(ChatColor.GREEN + "Квест пройден за - " + questWorldData.ticksPlayedFinal /1200 + " мин., " + questWorldData.ticksPlayedFinal%1200/20 + " сек.");
                    }
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Игрок не находится/находился в квесте!");
            return true;
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("create")){
            try{
                Integer num = Integer.valueOf(args[1]);
                if(num < 1 || num > 5){
                    player.sendMessage(ChatColor.RED + "Не стоит этого делать");
                    return true;
                }
                // создаем миры с разными названиями
                for(int i = 0;i < num; i++){
                    String name;
                    for(int y = 1; ; y++){
                        name = "quest";
                        name = name + y;
                        boolean was = false;
                        for(World w : Bukkit.getWorlds()){
                            if(w.getName().equals(name)){
                                was = true;
                            }
                        }
                        if(!was){
                            // если такого мира нет
                            player.performCommand("mv clone worldquest " + name);
                            Thread.sleep(500);
                            QuestWorldData questWorldData = new QuestWorldData(Bukkit.getWorld(name));
                            listQuestWorlds.allQuestWorlds.add(questWorldData);
                            // запись в файл
                            editor.setData(listQuestWorlds);
                            break;
                        }
                    }
                }
            }catch (NumberFormatException | InterruptedException e){
                player.sendMessage(ChatColor.RED + "Неправильное значение количества миров!");
            }
            return true;
        }
        // вывод всех миров
        if(args.length == 2 && args[0].equalsIgnoreCase("list")) {
            if(args[1].equalsIgnoreCase("world")) {
                if (listQuestWorlds.allQuestWorlds.size() == 0) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Список миров для квеста пуст!");
                    return true;
                }
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (questWorldData.isBusy) {
                        player.sendMessage(ChatColor.DARK_GREEN + questWorldData.questWorldName + " - занят игроком " + questWorldData.playerName );
                    } else
                        player.sendMessage(ChatColor.GREEN + questWorldData.questWorldName +
                                ChatColor.GREEN + " - свободен.");
                }
                return true;
            }
            else if(args[1].equalsIgnoreCase("player")){
                int playersFound = 0;
                for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                    if(questWorldData.isBusy){
                        if(questWorldData.ticksPlayedFinal==0) {
                            player.sendMessage(ChatColor.GREEN + "Игрок " + questWorldData.playerName + " проходит квест в " + questWorldData.questWorldName);
                        }else player.sendMessage(ChatColor.DARK_GREEN + "Игрок " + questWorldData.playerName + " прошел квест в " + questWorldData.questWorldName);
                        playersFound+=1;
                    }
                }
                if (playersFound==0) {
                    player.sendMessage(ChatColor.RED + "Никто не проходит квест!");
                }
                return true;
            }else return false;
        }
        // перезагрузка
        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            List<QuestWorldData> tempDataToDelete = new ArrayList<>();
            // удаляем лишние миры из массива
            // Находим нулевые миры в файле, записывем во временный массив
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                if(Bukkit.getWorld(questWorldData.questWorldName)==null){
                    tempDataToDelete.add(questWorldData);
                }
            }
            for(QuestWorldData qwd : tempDataToDelete){
                listQuestWorlds.allQuestWorlds.remove(qwd);
            }
            editor.setData(listQuestWorlds);

            player.sendMessage(ChatColor.GREEN + "Список миров перезагружен, данные удалены!");
            return true;
        }
        // удаление записи
        if(args.length == 2 && args[0].equalsIgnoreCase("remove")){
            String playerName = args[1];
            List<String> questPlayers = listQuestWorlds.allQuestWorlds.stream().map(obj->obj.playerName).collect(Collectors.toList());
            if(!questPlayers.contains(playerName)){
                player.sendMessage(ChatColor.RED + "Записи не существует!");
                return true;
            }
            for(QuestWorldData qwi : listQuestWorlds.allQuestWorlds){
                if(qwi.isBusy && qwi.playerName.equalsIgnoreCase(playerName)){
                    //удаление и перезапись
                    listQuestWorlds.allQuestWorlds.remove(qwi);
                    editor.setData(listQuestWorlds);
                    player.sendMessage(ChatColor.GREEN + "Запись о прохождении квеста игроком " +
                            playerName + ChatColor.GREEN +" успешно удалена!");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Ошиб0чка! :(");
            return true;
        }
        return false;
    }
}
