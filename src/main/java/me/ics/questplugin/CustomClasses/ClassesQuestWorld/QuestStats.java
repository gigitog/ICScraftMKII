package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import me.ics.questplugin.CustomClasses.Statistic.ArrayProcessor;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    private String uuid;
    private  Player player;


    private String[] disciplines = new String[]{
            "Дискретная математика и Алгоритмы",
            "Анализ данных и математика",
            "Основы программирования",
            "Операционные системы",
            "Hardware",
            "Тестировка ПО"
    };

    public QuestStats(FileJsonEditor<ListQuestWorldData> editor, Player player, FileJsonEditor<ListAllStatsData> editorStats, int[] votes) {
        this.editor = editor;
        this.playerName = player.getName();
        this.editorStats = editorStats;
        uuid = player.getUniqueId().toString();
        makeTasks();
        this.player = player;
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

        for (String s : makeList(votes, false)) meta.addPage(s);

        book.setItemMeta(meta);
        book.setAmount(1);
        return book;
    }

    public ItemStack makeBookSecret() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§l42");
        meta.setLore(Arrays.asList("§dЖурнал наблюдений"));
        meta.setAuthor("§cЮрий");
        meta.setTitle("Статистика " + playerName);

        for (String s : makeList(votes, true)) meta.addPage(s);

        book.setItemMeta(meta);
        book.setAmount(1);
        return book;
    }

    private void makeTasks() {
        String s = "Задание с";
//        tasks.put(201, s + " таблицей истинности");
        tasks.put(201, s + " графом");
        tasks.put(202, s + " алгоритмом кратчайшего пути");

        tasks.put(301, s + "о статистикой");
        tasks.put(302, s + " вероятностью");
        tasks.put(303, s + " комбинаторикой");

        tasks.put(401, s + " сортировкой массива");
        tasks.put(402, s + " типами данных");
//        tasks.put(403, s + " этапами написания программы");
//        tasks.put(404, s + " основами ООП");

        tasks.put(501, s + " настройкой сервера");
//        tasks.put(502, s + " общими сведениями");

        tasks.put(601, s + " перебором сигналов");
//        tasks.put(602, s + " настройкой робота");
//        tasks.put(603, s + " задачами физики");

        tasks.put(701, s + " паркуром");
//        tasks.put(702, s + " убийством мобов");
//        tasks.put(703, s + " передвижением блоков");
    }

    private List<String> makeList(int[] votes, boolean isSecret) {
        List<String> list = new ArrayList<>();
        int[] specialities = new int[]{113, 121, 122, 123, 126, 151};
        QuestWorldData qwd = editor.getData().getQWDbyPlayer(playerName);
        if (qwd == null){
            list.add("Неполадки. Обратитесь к админам (Сотрудникам)\n Проси прощения!");
            return list;
        }

        List<Integer> recommend = new ArrayProcessor(editorStats, votes, playerName).percentage();
        String recStr = "";

        int i = 0;
        for (int percent : recommend) {
            recStr = recStr.concat("§9" + specialities[i++] + "§r - " + percentColor.apply((double) percent));
        }

        int headPage = 0;
        String s = "\n§7___________________§r\n\n";
        String string;

        if (isSecret) {
            int ticks = player.getTicksLived() + qwd.ticksSavedBeforeLeaving - qwd.ticksLivedWhenStart;
            string = "§oАнализ поведения" + s + "Имя: " + playerName +
                    "\nID:\n" + uuid + "\nВремя прохождения\nзаданий:\n§3" + ticks / 1200 +
                    " (мин) " + ticks % 1200 / 20 + " (сек)§r";
            list.add(string);
            string = "Вероятные предпочтения:\n" + s + recStr + "§r" + s;
            list.add(string);
            string = "Скорость: " + velocity.apply(ticks) +
                    s + "Концентрация внимания: " + concentration.apply(qwd) + s +
                    "Устойчивость к \nстрессу: " + stressTest.apply(qwd) + s + "Погрешность: " + factor.apply(qwd) + s;
            list.add(string);
        } else {
             string = "§oОбщая информация\n" + s + "Игровое имя: " +
                    playerName + "\nВремя прохождения:\n§3" + qwd.ticksPlayedFinal / 1200 +
                    " (мин) " + qwd.ticksPlayedFinal % 1200 / 20 + " (сек)§r" + s +
                    "Рекомендуемые специальности смотреть далее.\n";
            list.add(string);
            string = s +  recStr + "§r" + s;
            list.add(string);
        }

        if (isSecret){
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


        return list;
    }

    private Function<Boolean, String> passed = x -> { if (x) return "§2Пройдено"; return "§4Не пройдено"; };
    private Function<Double, String> percentColor = x -> {
        x = x + Math.round((int) (Math.random() * 50)) / 10.0;
        if (x > 100) x = 100.0 - Math.round((int) (Math.random() * 10)) / 10.0;
        if (x <= 55 ) return  "§4" + x + "%§r\n";
        if (x < 75) return "§6" + x + "%§r\n";
        return "§2" + x + "%§r\n";
    };
    private Function<Integer, String> velocity = x -> {
        x = x / 1200;
        if (x < 20 ) return  "§2Высокая §r";
        if (x < 60) return "§6Средняя §r";
        return "§4Низкая §r";
    };
    private Function<QuestWorldData, String> concentration = x -> {
        int percent = x.num_quests_complete.size();
        if (percent <= 4 ) return  "§4Низкая §r";
        if (percent <= 7) return "§6Средняя §r";
        return "§5Высокая §r";
    };
    private Function<QuestWorldData, String> stressTest = x -> {
        List<Integer> quests = Arrays.asList( 601, 301, 302, 303 );
        int counter = 0;
        for (int i : x.num_quests_complete){
            if (quests.contains(i)) counter++;
        }

        if (counter <= 1 ) return  "§4Низкая §r";
        if (counter <= 3) return "§6Средняя §r";
        return "§2Высокая §r";
    };
    private Function<QuestWorldData, String> factor = x -> {
        int counter_1 = 0;
        int counter_2 = 0;
        int counter_0 = 0;
        for (int i : x.votes){
            if (i == 0) counter_0++;
            if (i == 1) counter_1++;
            if (i == 2) counter_2++;
        }
        List<Integer> list = Arrays.asList(counter_0, counter_1, counter_2);
        if (Collections.max(list) >= 4){
            return "§1" + ((Math.round((int)(Math.random() * 8)) / 10.0) + 14.2) + "%§r";
        }
        return "§1" + ((Math.round((int) (Math.random() * 8)) / 10.0) + 4.2) + "%§r";
    };
}