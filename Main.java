package homework10;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        String[] nums = {"1, 2, 0","4, 5"};

        names.add("Иванов");
        names.add("Петров");
        names.add("Сидоров");
        names.add("Антонов");
        names.add("Васев");
        names.add("Жиглов");
        names.add("Васнецов");

        // Задание 1
        System.out.println(names);

        String oddNames = getOddNamesList(names);
        String oddNamesAgain = getOddNamesListVersionTwo(names);
        String oddNamesYetAgain = getOddNamesListVersionThree(names);

        System.out.println(oddNames);
        System.out.println(oddNamesAgain);
        System.out.println(oddNamesYetAgain);
        System.out.println();

        // Задание 2

        ArrayList<String> mySortedList = reverseSortAndBringToUpperCase(names);
        System.out.println(mySortedList);
        System.out.println();

        // Задание 3

        System.out.println(convertStringArray(nums));
        System.out.println();

        // Задание 4

        genRandoms(25214903917L, 11, (long)Math.pow(2.0,48.0),1).limit(20).forEach(System.out::println);
        System.out.println();


        // Задание 5

        Stream<Integer> first = Stream.of(1,2,3,4,5);
        Stream<Integer> second = Stream.of(12,22,32,42,53);
        zip(first,second).forEach(n -> System.out.print(n + " "));

    }

    public static String getOddNamesList(List<String> namesList) {      // через конвейер и терминальную в потоке, есть проблема
        StringBuilder output = new StringBuilder();
        final int[] countNames = {0};
        // тут собственно вопрос: идея предложила автоисправление в такой вот массив с единым элементом, зато файнал.
        // Файнал мне нужен для использования в лямбде в потоке внизу. Есть ли хитрвый вариант решения без использования
        // такого кривого массива? Первоначально делал через статик переменную в классе, но хочу через переменную, видную
        // только в блоке кода метода  getOddNamesList

        namesList.stream()
                .filter(name -> {
                    countNames[0]++;
                    return countNames[0] % 2 == 1; })
                .forEach(name -> output.append(countNames[0])
                                       .append(". ")
                                       .append(name)
                                       .append(", "));

       return output
               .deleteCharAt(output.length() - 1)
               .deleteCharAt(output.length() - 1)
               .toString();
    }

    public static String getOddNamesListVersionTwo(List<String> namesList) {      // через итератор
        Iterator<String> myIterator = namesList.stream().iterator();
        StringBuilder result = new StringBuilder("1. " + myIterator.next());

        int i = 2;
        while (myIterator.hasNext()) {
            if (i++ % 2 == 0) {
                myIterator.next();
            } else {
                result
                        .append(", ")
                        .append(i - 1)
                        .append(". ")
                        .append(myIterator.next());
            }
        }
        return result.toString();
    }

    public static String getOddNamesListVersionThree(List<String> namesList) {   // через reduce
        final int[] countMyNames = {0};      // и снова проблема с переменной контроля четности... просит файнал
        return namesList.stream().reduce("",(a, b) -> {
            if (countMyNames[0]++ % 2 == 0) {
                return a + countMyNames[0] + ". " + b + ", ";
            }
            return a;
        });
    }

    public static ArrayList<String> reverseSortAndBringToUpperCase(List<String> namesList) {

        return namesList.stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    }

    public static String convertStringArray(String[] inputArray) {
        StringBuilder input = new StringBuilder();
        StringBuilder output = new StringBuilder();
 
        for (String x  : inputArray) {
            input.append(x);
        }

        IntStream chars = input.toString().chars();

        chars   .filter(n -> n < 58 && n > 47)
                .sorted()
                .forEach(n -> output.append((char)n).append(", "));

        return output
                .insert(0,"\"")
                .deleteCharAt(output.length() - 1)
                .deleteCharAt(output.length() - 1)
                .append("\"")
                .toString();
    }

    public static Stream<Long> genRandoms(long a, int c, long m, long seed) {

        return Stream.iterate(seed, n -> (a * n + c) % m);
    }


    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        return Stream
                .concat(first,second)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        collected -> {
                                        Collections.shuffle(collected);
                                        return collected.stream();
                                     }
                                     ));
    }
}
