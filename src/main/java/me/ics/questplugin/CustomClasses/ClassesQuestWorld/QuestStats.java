package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.function.Function;

public class QuestStats {
    private FileJsonEditor<ListQuestWorldData> editor;
    private String playerName;
    private Map<Integer, String> tasks = new TreeMap<>();

    private Function<Boolean, String> passed = x->{ if(x) return "§2Пройдено"; return "§4Не пройдено"; };
    private String[] disciplines = new String[]{
            "Дискретная математика и Алгоритмы",
            "Дискретная математика и Алгоритмы",
            "Анализ данных и математика",
            "Основы программирования",
            "Операционные системы",
            "Hardware",
            "Тестировка ПО"
    };

    public QuestStats(FileJsonEditor<ListQuestWorldData> editor, String playerName) {
        this.editor = editor;
        this.playerName = playerName;
        makeTasks();
    }

    public ItemStack makeBook(){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§l42");
        meta.setLore(Arrays.asList("§dЗдесь хранится твоя статистика"));
        meta.setAuthor("§cUncle Yura");
        meta.setTitle("Статистика игрока " + playerName);
        for(String s : makeList()) meta.addPage(s);
        book.setItemMeta(meta);
        book.setAmount(1);
        return book;
    }

    private void makeTasks(){
        String s = "Задание с";
        tasks.put(201, s+" таблицей истинности");
        tasks.put(202, s+" графом");
        tasks.put(203, s+"о схемой");
        tasks.put(301, s+" Блок Схемой");
        tasks.put(302, s+" алгоритмом");

        tasks.put(401, s+"о статистикой");
        tasks.put(402, s+" вероятностью");
        tasks.put(403, s+" интегралом");

        tasks.put(501, s+" типами данных");
        tasks.put(502, s+" сортировкой массива");
        tasks.put(503, s+" этапами написания программы");
        tasks.put(504, s+" основами ООП");

        tasks.put(701, s+" общими сведениями");
        tasks.put(702, s+" сервером");

        tasks.put(801, s+"о сбором компьтера");
        tasks.put(802, s+" настройкой робота");
        tasks.put(803, s+" задачами физики");

        tasks.put(901, s+" паркуром");
        tasks.put(902, s+" убийством мобов");
        tasks.put(903, s+" передвижением блоков");
    }

    private List<String> makeList(){
        List<String> list = new ArrayList<>();
        for(QuestWorldData qwd : editor.getData().allQuestWorlds){
            if(qwd.playerName.equals(playerName)){
                int headPage = 0;
                String s = "\n§7___________________§r\n\n";
                String string = "§oОбщая информация\n" + s + "Игровое имя: " +
                        playerName + "\nВремя прохождения: §3" + qwd.ticksPlayedFinal / 1200 +
                        " (мин) " +  qwd.ticksPlayedFinal % 1200 / 20 + " (сек)§r" + s +
                        "Рекомендуемая специальность: §9122§r\n";

                list.add(string);

                for(int num : tasks.keySet()){
                    String page = "";
                    if(num % 10 == 1){
                        page = page.concat(disciplines[headPage] + s +
                                tasks.get(num) + ": " + passed.apply(qwd.num_quests_complete.contains(num)) +
                                s + tasks.get(num + 1) + ": " +
                                passed.apply(qwd.num_quests_complete.contains(num + 1)) + s);
                        headPage++;
                        list.add(page);
                    } else if(num % 10 == 3){
                        page = page.concat(s + tasks.get(num) + ": " +
                                passed.apply(qwd.num_quests_complete.contains(num)) + s);
                        if ((num == 503)) {
                            page = page.concat(tasks.get(num) + ": " +
                                    passed.apply(qwd.num_quests_complete.contains(num)) + s);
                        }
                        list.add(page);
                    }
                }
            }
        }
        return list;
    }
}