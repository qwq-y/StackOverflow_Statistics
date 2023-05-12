package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Clawer {

  public static void main(String[] args) {
    try {
      URL url = new URL("https://pokeapi.co/api/v2/pokemon/pikachu/");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();

      InputStream inputStream = conn.getInputStream();
      String jsonText = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();

//      System.out.println(jsonText);

      JSONObject json = new JSONObject(jsonText);
      String name = json.getString("name");
      int height = json.getInt("height");
      int weight = json.getInt("weight");
      JSONArray abilitiesArray = json.getJSONArray("abilities");
      String[] abilities = new String[abilitiesArray.length()];
      for (int i = 0; i < abilitiesArray.length(); i++) {
        abilities[i] = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name");
      }

      System.out.println("Name: " + name);
      System.out.println("Height: " + height);
      System.out.println("Weight: " + weight);
      System.out.println("Abilities: " + String.join(", ", abilities));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
