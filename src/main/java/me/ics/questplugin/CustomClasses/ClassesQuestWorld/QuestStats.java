package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import me.ics.questplugin.CustomClasses.Statistic.ArrayProcessor;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.function.Function;

public class QuestStats {
    private FileJsonEditor<ListQuestWorldData> editor;
    private FileJsonEditor<ListAllStatsData> editorStats;
    private String playerName;
    private Map<Integer, String> tasks = new TreeMap<>();
    private int[] votes;

    private Function<Boolean, String> passed = x -> { if (x) return "§2Пройдено"; return "§4Не пройдено"; };
    private Function<Integer, String> percentColor = x -> {
        if (x <= 55 ) return  "§4" + x + "%§r\n";
        if (x < 75) return "§6" + x + "%§r\n";
        return "§2" + x + "%§r\n";
    };
    private String[] disciplines = new String[]{
            "Дискретная математика и Алгоритмы",
            "Анализ данных и математика",
            "Основы программирования",
            "Операционные системы",
            "Hardware",
            "Тестировка ПО"
    };

    public QuestStats(FileJsonEditor<ListQuestWorldData> editor, String playerName, FileJsonEditor<ListAllStatsData> editorStats, int[] votes) {
        this.editor = editor;
        this.playerName = playerName;
        this.editorStats = editorStats;
        makeTasks();
        this.votes = votes;
    }

    public ItemStack makeBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§l42");
        meta.setLore(Arrays.asList("§dЗдесь хранится твоя статистика"));
        meta.setAuthor("§cUncle Yura");
        meta.setTitle("Статистика игрока " + playerName);
        for (String s : makeList(votes)) meta.addPage(s);
        book.setItemMeta(meta);
        book.setAmount(1);
        return book;
    }

    private void makeTasks() {
        String s = "Задание с";
        tasks.put(201, s + " таблицей истинности");
        tasks.put(202, s + " графом");
        tasks.put(203, s + " алгоритмом кратчайшего пути");

        tasks.put(301, s + "о статистикой");
        tasks.put(302, s + " вероятностью");
        tasks.put(303, s + " интегралом");

        tasks.put(401, s + " сортировкой массива");
        tasks.put(402, s + " типами данных");
        tasks.put(403, s + " этапами написания программы");
        tasks.put(404, s + " основами ООП");

        tasks.put(501, s + " сервером");
        tasks.put(502, s + " общими сведениями");

        tasks.put(601, s + "о сбором компьтера");
        tasks.put(602, s + " настройкой робота");
        tasks.put(603, s + " задачами физики");

        tasks.put(701, s + " паркуром");
        tasks.put(702, s + " убийством мобов");
        tasks.put(703, s + " передвижением блоков");
    }

    private List<String> makeList(int[] votes) {
        List<String> list = new ArrayList<>();
        int[] specialities = new int[]{113, 121, 122, 123, 126, 151};
        for (QuestWorldData qwd : editor.getData().allQuestWorlds) {
            if (qwd.playerName.equals(playerName)) {
                List<Integer> recommend = new ArrayProcessor(editorStats, votes, playerName).percentage();
                String recStr = "";

                int i = 0;
                for (int percent : recommend) {
                    recStr = recStr.concat("§9" + specialities[i] + "§r - " + percentColor.apply(percent));
                    i++;
                }

                int headPage = 0;
                String s = "\n§7___________________§r\n\n";
                String string = "§oОбщая информация\n" + s + "Игровое имя: " +
                        playerName + "\nВремя прохождения:\n§3" + qwd.ticksPlayedFinal / 1200 +
                        " (мин) " + qwd.ticksPlayedFinal % 1200 / 20 + " (сек)§r" + s +
                        "Рекомендуемые специальности смотреть далее.\n";
                list.add(string);
                string = s +  recStr + "§r" + s;
                list.add(string);

                String page = "";
                boolean headAdded;
                for (int num : tasks.keySet()) {
                    if (num % 2 == 1 && (headPage + 2) == num / 100) {
                        headAdded = false;
                        page = page.concat(disciplines[headPage] + s +
                                tasks.get(num) + ": " + passed.apply(qwd.num_quests_complete.contains(num)) + s);
                        if (tasks.containsKey(num + 1)) {
                            page = page.concat(tasks.get(num + 1) + ": " +
                                    passed.apply(qwd.num_quests_complete.contains(num + 1)) + s);

                        } else {
                            headPage++;
                            headAdded = true;
                        }

                        if(!headAdded && !tasks.containsKey(num + 2)){
                            headPage++;
                        }

                        list.add(page);
                        page = "";
                    }
                }
            }
        }
        return list;
    }
}