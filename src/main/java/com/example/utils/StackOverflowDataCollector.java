package com.example.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

import java.util.zip.GZIPInputStream;
import org.json.*;

public class StackOverflowDataCollector {
//  private static final String API_KEY = "YOUR_API_KEY_HERE";
  private static final String BASE_URL = "https://api.stackexchange.com/2.3";
  private static final int PAGE_SIZE = 100;
  private static final String TAG = "java";
  private static final int MAX_PAGES = 6;
  private static String DB_URL;
  private static String DB_USERNAME;
  private static String DB_PASSWORD;
  private static Connection conn = null;

  public static void main(String[] args) throws Exception {
    // 数据库连接
    File file = new File("C:\\Java2Proj\\src\\main\\resources\\templates\\databaseInfo\\dbInfo");
    Scanner scanner = new Scanner(file);
    DB_URL = scanner.nextLine();
    DB_USERNAME = scanner.nextLine();
    DB_PASSWORD = scanner.nextLine();
    scanner.close();
    conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

    // 存储questions
    int page = 1;
    int count = 0;
    while (page <= MAX_PAGES) {
      JSONArray items = getQuestions(page);
      for (int i = 0; i < items.length(); i++) {
        JSONObject item = items.getJSONObject(i);
        storeQuestion(item);
        count++;
        System.out.println("Question #" + count + ": " + item.getString("title"));
      }
      page++;
      // 添加delay防止rate limit
      Thread.sleep(1000);
    }

    conn.close();
  }

  private static JSONArray getQuestions(int page) throws Exception {
    String url = BASE_URL + "/questions?page=" + page + "&pagesize=" + PAGE_SIZE + "&order=desc&sort=votes&tagged=" + TAG + "&site=stackoverflow" /*+ "&key="  + API_KEY*/;
    System.out.println("url: " + url);
    String json = getUrlContents(url);

    JSONObject obj = new JSONObject(json);
    return obj.getJSONArray("items");
  }

  private static String getUrlContents(String urlString) throws Exception {
    URL url = new URL(urlString);
    URLConnection conn = (URLConnection) url.openConnection();
    conn.setConnectTimeout(5000);
    conn.setReadTimeout(15000);
    conn.connect();

    // 检查是否为gzip压缩格式
    boolean isGzip = false;
    String contentEncoding = conn.getContentEncoding();
    if (contentEncoding != null && contentEncoding.toLowerCase().contains("gzip")) {
      isGzip = true;
    }

    // 读取数据
    InputStream inputStream = conn.getInputStream();
    if (isGzip) {
      inputStream = new GZIPInputStream(inputStream);
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      sb.append(line);
    }
    reader.close();
    return sb.toString();
  }

  private static void storeQuestion(JSONObject item) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO question VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    try {
      if (item.has("tags")) {
        pstmt.setString(1, item.getJSONArray("tags").toString());
      } else {
        pstmt.setNull(1, Types.VARCHAR);
      }

      if (item.has("owner")) {
        pstmt.setString(2, item.getJSONObject("owner").toString());
      } else {
        pstmt.setNull(2, Types.VARCHAR);
      }

      if (item.has("is_answered")) {
        pstmt.setBoolean(3, item.getBoolean("is_answered"));
      } else {
        pstmt.setNull(3, Types.BOOLEAN);
      }

      if (item.has("view_count")) {
        pstmt.setLong(4, item.getLong("view_count"));
      } else {
        pstmt.setNull(4, Types.BIGINT);
      }

      if (item.has("protected_date")) {
        pstmt.setLong(5, item.getLong("protected_date"));
      } else {
        pstmt.setNull(5, Types.BIGINT);
      }

      if (item.has("accepted_answer_id")) {
        pstmt.setLong(6, item.getLong("accepted_answer_id"));
      } else {
        pstmt.setNull(6, Types.BIGINT);
      }

      if (item.has("answer_count")) {
        pstmt.setLong(7, item.getLong("answer_count"));
      } else {
        pstmt.setNull(7, Types.BIGINT);
      };

      if (item.has("last_activity_date")) {
        pstmt.setLong(8, item.getLong("last_activity_date"));
      } else {
        pstmt.setNull(8, Types.BIGINT);
      };

      if (item.has("creation_date")) {
        pstmt.setLong(9, item.getLong("creation_date"));
      } else {
        pstmt.setNull(9, Types.BIGINT);
      }

      if (item.has("last_edit_date")) {
        pstmt.setLong(10, item.getLong("last_edit_date"));
      } else {
        pstmt.setNull(10, Types.BIGINT);
      }

      pstmt.setLong(11, item.getLong("question_id"));

      if (item.has("content_license")) {
        pstmt.setString(12, item.getString("content_license"));
      } else {
        pstmt.setNull(12, Types.VARCHAR);
      }

      if (item.has("link")) {
        pstmt.setString(13, item.getString("link"));
      } else {
        pstmt.setNull(13, Types.VARCHAR);
      }

      if (item.has("title")) {
        pstmt.setString(14, item.getString("title"));
      } else {
        pstmt.setNull(14, Types.VARCHAR);
      }
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println("----------------skipped----------------");
      return;
    }
  }
}

/**
 * 建表语句：
 *
 * create table question
 * (
 *     tags               varchar,
 *     owner              varchar,
 *     is_answered        bool,
 *     view_count         bigint,
 *     protected_date     bigint,
 *     accepted_answer_id bigint,
 *     answer_count       bigint,
 *     last_activity_date bigint,
 *     creation_date      bigint,
 *     last_edit_date     bigint,
 *     question_id        bigint primary key,
 *     content_license    varchar,
 *     link               varchar,
 *     title              varchar
 * );
 */

/**
 * 爬取的questions中的数据示例：
 * https://api.stackexchange.com/2.3/questions?page=1&pagesize=100&order=desc&sort=votes&tagged=java&site=stackoverflow
 *
 * {"tags":["java","c++","performance","cpu-architecture","branch-prediction"],
 * "owner":{"account_id":31692,
 *          "reputation":491761,
 *          "user_id":87234,
 *          "user_type":"registered",
 *          "accept_rate":100,
 *          "profile_image":"https://i.stack.imgur.com/FkjBe.png?s=256&g=1",
 *          "display_name":"GManNickG",
 *          "link":"https://stackoverflow.com/users/87234/gmannickg"},
 * "is_answered":true,
 * "view_count":1810966,
 * "protected_date":1399067470,
 * "accepted_answer_id":11227902,
 * "answer_count":25,"score":26979,
 * "last_activity_date":1683824982,
 * "creation_date":1340805096,
 * "last_edit_date":1683824982,
 * "question_id":11227809,
 * "content_license":"CC BY-SA 4.0",
 * "link":"https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-processing-an-unsorted-array",
 * "title":"Why is processing a sorted array faster than processing an unsorted array?"},
 *
 *
 * 爬取的post的数据示例
 * https://api.stackexchange.com/2.3/posts/12345/comments?site=stackoverflow
 *
 * {"items":[{"owner":{"account_id":31692,
 *            "reputation":491771,
 *            "user_id":87234,
 *            "user_type":"registered",
 *            "accept_rate":100,
 *            "profile_image":"https://i.stack.imgur.com/FkjBe.png?s=256&g=1",
 *            "display_name":"GManNickG",
 *            "link":"https://stackoverflow.com/users/87234/gmannickg"},
 * "score":26980,
 * "last_edit_date":1683824982,
 * "last_activity_date":1683824982,
 * "creation_date":1340805096,
 * "post_type":"question",
 * "post_id":11227809,
 * "content_license":"CC BY-SA 4.0",
 * "link":"https://stackoverflow.com/q/11227809"}],
 * "has_more":false,
 * "quota_max":300,
 * "quota_remaining":299}
 */