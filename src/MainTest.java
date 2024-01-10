import org.json.*;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an English word:");
        String inputWord = scanner.nextLine();

        try {
            String content = new String(Files.readAllBytes(Paths.get("Resources/jmdict-eng-3.5.0.json")));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObject = wordsArray.getJSONObject(i);
                JSONArray sensesArray = wordObject.getJSONArray("sense");

                for (int j = 0; j < sensesArray.length(); j++) {
                    JSONObject senseObject = sensesArray.getJSONObject(j);
                    JSONArray glossArray = senseObject.getJSONArray("gloss");

                    for (int k = 0; k < glossArray.length(); k++) {
                        String definition = glossArray.getJSONObject(k).getString("text");

                        if (definition.contains(inputWord)) {
                            JSONArray japaneseWords = wordObject.getJSONArray("kana");

                            for (int l = 0; l < japaneseWords.length(); l++) {
                                JSONObject japaneseWord = japaneseWords.getJSONObject(l);
                                String kana = japaneseWord.getString("text");
                                writer.println("Kana: " + kana);
                            }

                            if(wordObject.has("kanji")) {
                                JSONArray kanjiWords = wordObject.getJSONArray("kanji");

                                for (int m = 0; m < kanjiWords.length(); m++) {
                                    JSONObject kanjiWord = kanjiWords.getJSONObject(m);
                                    String kanji = kanjiWord.getString("text");
                                    writer.println("Kanji: " + kanji);
                                }
                            }

                            writer.println("English: " + definition);
                            writer.println();
                        }
                    }
                }
            }

            writer.close();
            System.out.println("Translations written to output.txt");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
