import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;

public class SortData {
    static int countIntegers = 0;               static int countDoubles = 0;                        static int countStrings = 0;
    static long minIntValue = Long.MAX_VALUE;   static double minDoubleValue = Double.MAX_VALUE;    static int minStringSize = Integer.MAX_VALUE;
    static long maxIntValue = Long.MIN_VALUE;   static double maxDoubleValue = Double.MIN_VALUE;    static int maxStringSize = 0;
    static double avgIntValue;                  static double avgDoubleValue;
    static long sumIntValues = 0;               static double sumDoubleValues = 0;
    static boolean append = false;
    static boolean appendDefault = false;
    static String path = "";
    public static void main(String[] args)
    {
        String[] allKeys = new String[]{"-p", "-o", "-a", "-s", "-f"};

        ArrayList<String> files = getInputFileNames(args);
        path = getPath(args, allKeys);
        appendDefault = getWriteMode(args);

        readFile(files, path);
        showStats(getStatsMode(args));
    }
    //===========================================================Обработка входящих параметров===========================================================
    private  static short getStatsMode(String[] input)
    {
        for(String statsKey: input)
        {
            if(statsKey.equals("-s")) return 1;
            else if (statsKey.equals("-f")) return 2;
        }
        return 0;
    }
    private  static boolean getWriteMode(String[] input)
    {
        for(String appendKey: input)
        {
            if(appendKey.equals("-a")) return true;
        }
        return false;
    }
    private static String getPath(String[] args, String[] allKeys)
    {
        String prefix = getParameter(args, allKeys, "-p");
        path = getParameter(args, allKeys, "-o");
        if(!path.isEmpty())
        {
            path += "\\" + prefix;
            return path;
        }
        return prefix;
    }
    private static String getParameter(String[] input, String[] keys, String paramKey)
    {
        int paramIndex = -1;
        String parameter = "";
        for (int i = 0; i < input.length; i++)
        {
            if(input[i].equals(paramKey)) paramIndex = i + 1;
        }
        if(paramIndex == -1) return "";
        for (int i = paramIndex; i < input.length; i++) {
            boolean stop = false;
            if(input[i].contains(".txt")) break;
            for (String element : keys) {
                if (input[i].equals(element))
                {
                    stop = true;
                    break;
                }
            }
            if(stop) break;
            parameter +=  parameter.isEmpty()? input[i] : " " + input[i];
        }
        return parameter;
    }
    private static ArrayList<String> getInputFileNames(String[] input)
    {
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0; i < input.length; i++)
        {
            if (input[i].contains(".txt"))
            {
                fileNames.add(input[i]);
                input[i] = "";
            }
        }
        return fileNames;
    }
    //============================================================Сбор, отображение статистики===========================================================
    private  static void showStats(short key)
    {
        if(key == 0) return;
        if(key == 1)
        {
            Formatter shortStats = new Formatter();
            shortStats.format("Количество целых чисел: %s, количество вещественных чисел: %s, количество строк: %s",
                    countIntegers, countDoubles, countStrings);
            System.out.println(shortStats);
        }
        else if(key == 2) showFullStats();
    }
    private  static void showFullStats()
    {
        if(countIntegers > 0) showIntStats();
        if(countDoubles > 0) showDoubleStats();
        if(countStrings > 0) showStringStats();

    }
    private  static void showIntStats()
    {
        Formatter intStats = new Formatter();
        intStats.format("Количество целых чисел: %s, Самое большое целое число: %s, Самое малое целое число: %s, сумма целых чисел: %s, Среднее значение : %s",
                countIntegers, maxIntValue, minIntValue, sumIntValues, avgIntValue);
        System.out.println(intStats);
    }
    private  static void showDoubleStats()
    {
        Formatter doubleStats = new Formatter();
        doubleStats.format("Количество вещественных чисел: %s, Самое большое вещественное число: %s, Самое малое вещественное число: %s, Cумма вещественных чисел: %s, Среднее значение : %s",
                countDoubles, maxDoubleValue, minDoubleValue, sumDoubleValues, avgDoubleValue);
        System.out.println(doubleStats);
    }

    private  static void showStringStats()
    {
        Formatter strStats = new Formatter();
        strStats.format("Количество строк: %s, Длина самой короткой строки: %s, длина самой длинной строки: %s",
                countStrings, minStringSize, maxStringSize);
        System.out.println(strStats);
    }

    private static void collectStats(String typeName, String line)
    {
        switch (typeName)
        {
            case "int":
                collectIntStats(line);
                break;
            case "double":
                collectDoubleStats(line);
                break;
            case "string":
                collectStringStats(line);
                break;
        }
    }
    private static void collectIntStats(String line)
    {
        countIntegers++;
        Long iNumber = Long.parseLong(line);
        sumIntValues += iNumber;
        avgIntValue = (double) sumIntValues / countIntegers;
        if(iNumber < minIntValue) minIntValue = iNumber;
        if(iNumber > maxIntValue) maxIntValue = iNumber;
    }

    private static void collectDoubleStats(String line)
    {
        countDoubles++;
        double dNumber = Double.parseDouble(line);
        sumDoubleValues += dNumber;
        avgDoubleValue = sumDoubleValues / countDoubles;
        if(dNumber < minDoubleValue) minDoubleValue = dNumber;
        if(dNumber > maxDoubleValue) maxDoubleValue = dNumber;
    }
    private static void collectStringStats(String line)
    {
        countStrings++;
        if(line.length() < minStringSize) minStringSize = line.length();
        if(line.length() > maxStringSize) maxStringSize = line.length();
    }
    //==========================================================Чтение, сортировка строк из входящих файлов===================================================
    private static void readFile(ArrayList<String> files, String path)
    {
        FileReader[] fileReaders = new FileReader[files.size()];
        BufferedReader[] bufferedReaders = new BufferedReader[files.size()];
        try {
            for (int i = 0; i < files.size(); i++) {
                fileReaders[i] = new FileReader(files.get(i));
                bufferedReaders[i] = new BufferedReader(fileReaders[i]);
            }
            readLines(fileReaders, bufferedReaders);
        }
        catch (IOException exception)
        {
            System.err.println(exception.getMessage() + " выполнение программы будет завершено!");
            System.exit(1);
        }
        finally {
            closeReaders(fileReaders, bufferedReaders);
        }
    }
    private static void readLines(FileReader[] fileReaders, BufferedReader[] bufferedReaders)throws IOException
    {
        String line;
        int check = 0;
        while(check != fileReaders.length)
        {
            check = 0;
            for (int i = 0; i < fileReaders.length; i++) {
                if((line = bufferedReaders[i].readLine()) != null) sortLines(line, path);
                else check++;
            }
        }
    }
    private static void closeReaders(FileReader[] fileReaders, BufferedReader[] bufferedReaders)
    {
        try{
            for (int i = 0; i < fileReaders.length; i++) {
                fileReaders[i].close();
                bufferedReaders[i].close();
            }
        }
        catch (IOException | NullPointerException ioException)
        {
            System.out.println(ioException.getMessage());
        }
    }
    private static void sortLines(String line, String fileNamePrefix)
    {
        if(isInteger(line))
        {
            writeLinesToFile(line, "int", fileNamePrefix);
            collectStats("int", line);
            return;
        }
        if(isDouble(line))
        {
            writeLinesToFile(line, "double", fileNamePrefix);
            collectStats("double", line);
            return;
        }
        writeLinesToFile(line, "str", fileNamePrefix);
        collectStats("string", line);
    }

    private static boolean isInteger(String line)
    {
        try
        {
            Long.parseLong(line);
            return true;
        }
        catch (NumberFormatException numForEx)
        {
            return false;
        }
    }

    private static boolean isDouble(String line)
    {
        try
        {
            Double.parseDouble(line);
            return true;
        }
        catch (NumberFormatException numForEx)
        {
            return false;
        }
    }
    //====================================================================Запись данных в файлы==============================================================
    private static String buildFileName(String typeName, String fileNamePrefix)
    {
        String fileName = fileNamePrefix;
        switch (typeName)
        {
            case "int":
                fileName += "integers.txt";
                append =  countIntegers == 0? appendDefault : true;
                break;
            case "double":
                fileName += "floats.txt";
                append =  countDoubles == 0? appendDefault : true;
                break;
            case "str":
                fileName += "strings.txt";
                append =  countStrings == 0? appendDefault : true;
                break;
        }
        return fileName;
    }
    private static void writeLinesToFile(String input, String typeName, String fileNamePrefix)
    {
        if(input.isEmpty())return;
        String fileName = buildFileName(typeName, fileNamePrefix);
        try(FileWriter writer = new FileWriter(fileName, append);
            BufferedWriter bufferedWriter = new BufferedWriter(writer))
        {
            bufferedWriter.write(input + "\n");
        }
        catch (IOException ioException)
        {
            System.err.println(ioException.getMessage() + "! Выполнение программы будет остановлено!");
            System.exit(1);
        }
    }
}
