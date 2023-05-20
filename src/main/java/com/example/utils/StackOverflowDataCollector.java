package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class StackOverflowDataCollector {

  private static final String API_KEY = "YP8OChnmFH4yoDkDMzJUjQ((";
  private static final String BASE_URL = "https://api.stackexchange.com/2.3";
  private static final int PAGE_SIZE = 100;
  private static final String TAG = "java";
  private static final int MAX_PAGES = 6;
  private static String DB_URL;
  private static String DB_USERNAME;
  private static String DB_PASSWORD;
  private static Connection conn = null;

  public static void main(String[] args) throws Exception {
    File file = new File("C:\\Java2Proj\\src\\main\\resources\\templates\\databaseInfo\\dbInfo");
    Scanner scanner = new Scanner(file);
    DB_URL = scanner.nextLine();
    DB_USERNAME = scanner.nextLine();
    DB_PASSWORD = scanner.nextLine();
    scanner.close();
    conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

    // 会在getAndStoreQuestions()方法内调用其他的“getAndStore方法”
    getAndStoreQuestions();

    conn.close();
  }

  private static void getAndStoreAnswers(long quesitonId) throws Exception {
    String url = "https://api.stackexchange.com/2.3/questions/" + quesitonId
        + "/answers?site=stackoverflow&filter=withbody&key=" + API_KEY;
    String json = getUrlContents(url);
    JSONObject obj = new JSONObject(json);
    JSONArray items = obj.getJSONArray("items");
    for (int i = 0; i < items.length(); i++) {
      JSONObject item = items.getJSONObject(i);
      storeAnswers(item);
    }
  }

  private static void getAndStoreComments(long questionId) throws Exception {
    String url = "https://api.stackexchange.com/2.3/questions/" + questionId
        + "/comments?site=stackoverflow&filter=withbody&key=" + API_KEY;
    String json = getUrlContents(url);
    JSONObject obj = new JSONObject(json);
    JSONArray items = obj.getJSONArray("items");
    for (int i = 0; i < items.length(); i++) {
      JSONObject item = items.getJSONObject(i);
      storeComments(item);
    }
  }

  private static void getAndStoreQuestions() throws Exception {
    int page = 1;
    int count = 0;
    while (page <= MAX_PAGES) {
      String url = BASE_URL + "/questions?page=" + page + "&pagesize=" + PAGE_SIZE
          + "&order=desc&sort=votes&tagged=" + TAG + "&site=stackoverflow&filter=withbody&key=" + API_KEY;
      String json = getUrlContents(url);
      JSONObject obj = new JSONObject(json);
      JSONArray items = obj.getJSONArray("items");
      for (int i = 0; i < items.length(); i++) {
        JSONObject item = items.getJSONObject(i);
        storeQuestions(item);
        count++;
        System.out.println("question #" + count + ": " + item.getString("title"));

        long questionId = item.getLong("question_id");
        getAndStoreAnswers(questionId);
        getAndStoreComments(questionId);
      }
      page++;

      // 添加delay防止rate limit
      Thread.sleep(1000);
    }
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
      sb.append(line);
    }
    reader.close();
    return sb.toString();
  }

  private static void storeUser(JSONObject item) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO user_ VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    try {
      pstmt.setLong(1, item.getLong("account_id"));

      if (item.has("reputation")) {
        pstmt.setLong(2, item.getLong("reputation"));
      } else {
        pstmt.setNull(2, Types.BIGINT);
      }
      if (item.has("user_id")) {
        pstmt.setLong(3, item.getLong("user_id"));
      } else {
        pstmt.setNull(3, Types.BIGINT);
      }
      if (item.has("user_type")) {
        pstmt.setString(4, item.getString("user_type"));
      } else {
        pstmt.setNull(4, Types.VARCHAR);
      }
      if (item.has("accept_rate")) {
        pstmt.setInt(5, item.getInt("accept_rate"));
      } else {
        pstmt.setNull(5, Types.INTEGER);
      }
      if (item.has("profile_image")) {
        pstmt.setString(6, item.getString("profile_image"));
      } else {
        pstmt.setNull(6, Types.VARCHAR);
      }
      if (item.has("display_name")) {
        pstmt.setString(7, item.getString("display_name"));
      } else {
        pstmt.setNull(7, Types.VARCHAR);
      }
      if (item.has("link")) {
        pstmt.setString(8, item.getString("link"));
      } else {
        pstmt.setNull(8, Types.VARCHAR);
      }

      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.out.println("----------------user skipped----------------");
    }
  }

  private static void storeComments(JSONObject item) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO comment VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

    if (item.has("owner")) {
      JSONObject owner = item.getJSONObject("owner");
      if (owner.getString("user_type").equals("does_not_exist")) {
        pstmt.setLong(1, -1);
      } else {
        storeUser(owner);
        pstmt.setLong(1, owner.getLong("account_id"));
      }
    } else {
      pstmt.setNull(1, Types.BIGINT);
    }
    if (item.has("reply_to_user")) {
      JSONObject replyToUser = item.getJSONObject("reply_to_user");
      if (replyToUser.getString("user_type").equals("does_not_exist")) {
        pstmt.setLong(2, -1);
      } else {
        storeUser(replyToUser);
        pstmt.setLong(2, replyToUser.getLong("account_id"));
      }
    } else {
      pstmt.setNull(2, Types.BIGINT);
    }
    if (item.has("edited")) {
      pstmt.setBoolean(3, item.getBoolean("edited"));
    } else {
      pstmt.setNull(3, Types.BOOLEAN);
    }
    if (item.has("score")) {
      pstmt.setLong(4, item.getLong("score"));
    } else {
      pstmt.setNull(4, Types.BIGINT);
    }
    if (item.has("creation_date")) {
      pstmt.setLong(5, item.getLong("creation_date"));
    } else {
      pstmt.setNull(5, Types.BIGINT);
    }
    if (item.has("post_id")) {
      pstmt.setLong(6, item.getLong("post_id"));
    } else {
      pstmt.setNull(6, Types.BIGINT);
    }

    pstmt.setLong(7, item.getLong("comment_id"));

    if (item.has("content_license")) {
      pstmt.setString(8, item.getString("content_license"));
    } else {
      pstmt.setNull(8, Types.VARCHAR);
    }
    if (item.has("body")) {
      pstmt.setString(9, item.getString("body"));
    } else {
      pstmt.setNull(9, Types.VARCHAR);
    }

    pstmt.executeUpdate();
    pstmt.close();
  }

  private static void storeAnswers(JSONObject item) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO answer VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    if (item.has("owner")) {
      JSONObject owner = item.getJSONObject("owner");
      if (owner.getString("user_type").equals("does_not_exist")) {
        pstmt.setLong(1, -1);
      } else {
        storeUser(owner);
        pstmt.setLong(1, owner.getLong("account_id"));
      }
    } else {
      pstmt.setNull(1, Types.BIGINT);
    }
    if (item.has("is_accepted")) {
      pstmt.setBoolean(2, item.getBoolean("is_accepted"));
    } else {
      pstmt.setNull(2, Types.BOOLEAN);
    }
    if (item.has("score")) {
      pstmt.setLong(3, item.getLong("score"));
    } else {
      pstmt.setNull(3, Types.BIGINT);
    }
    if (item.has("last_activity_date")) {
      pstmt.setLong(4, item.getLong("last_activity_date"));
    } else {
      pstmt.setNull(4, Types.BIGINT);
    }
    if (item.has("last_edit_date")) {
      pstmt.setLong(5, item.getLong("last_edit_date"));
    } else {
      pstmt.setNull(5, Types.BIGINT);
    }
    if (item.has("creation_date")) {
      pstmt.setLong(6, item.getLong("creation_date"));
    } else {
      pstmt.setNull(6, Types.BIGINT);
    }

    pstmt.setLong(7, item.getLong("answer_id"));

    if (item.has("question_id")) {
      pstmt.setLong(8, item.getLong("question_id"));
    } else {
      pstmt.setNull(8, Types.BIGINT);
    }
    if (item.has("content_license")) {
      pstmt.setString(9, item.getString("content_license"));
    } else {
      pstmt.setNull(9, Types.VARCHAR);
    }
    if (item.has("body")) {
      pstmt.setString(10, item.getString("body"));
    } else {
      pstmt.setNull(10, Types.VARCHAR);
    }

    pstmt.executeUpdate();
    pstmt.close();
  }

  private static void storeQuestions(JSONObject item) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO question VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    if (item.has("owner")) {
      JSONObject owner = item.getJSONObject("owner");
      if (owner.getString("user_type").equals("does_not_exist")) {
        pstmt.setLong(1, -1);
      } else {
        storeUser(owner);
        System.out.println(owner.toString());
        pstmt.setLong(1, owner.getLong("account_id"));
      }
    } else {
      pstmt.setNull(1, Types.BIGINT);
    }
    if (item.has("is_answered")) {
      pstmt.setBoolean(2, item.getBoolean("is_answered"));
    } else {
      pstmt.setNull(2, Types.BOOLEAN);
    }
    if (item.has("view_count")) {
      pstmt.setLong(3, item.getLong("view_count"));
    } else {
      pstmt.setNull(3, Types.BIGINT);
    }
    if (item.has("protected_date")) {
      pstmt.setLong(4, item.getLong("protected_date"));
    } else {
      pstmt.setNull(4, Types.BIGINT);
    }
    if (item.has("accepted_answer_id")) {
      pstmt.setLong(5, item.getLong("accepted_answer_id"));
    } else {
      pstmt.setNull(5, Types.BIGINT);
    }
    if (item.has("answer_count")) {
      pstmt.setLong(6, item.getLong("answer_count"));
    } else {
      pstmt.setNull(6, Types.BIGINT);
    }
    if (item.has("last_activity_date")) {
      pstmt.setLong(7, item.getLong("last_activity_date"));
    } else {
      pstmt.setNull(7, Types.BIGINT);
    }
    if (item.has("creation_date")) {
      pstmt.setLong(8, item.getLong("creation_date"));
    } else {
      pstmt.setNull(8, Types.BIGINT);
    }
    if (item.has("last_edit_date")) {
      pstmt.setLong(9, item.getLong("last_edit_date"));
    } else {
      pstmt.setNull(9, Types.BIGINT);
    }

    pstmt.setLong(10, item.getLong("question_id"));

    if (item.has("content_license")) {
      pstmt.setString(11, item.getString("content_license"));
    } else {
      pstmt.setNull(11, Types.VARCHAR);
    }
    if (item.has("link")) {
      pstmt.setString(12, item.getString("link"));
    } else {
      pstmt.setNull(12, Types.VARCHAR);
    }
    if (item.has("title")) {
      pstmt.setString(13, item.getString("title"));
    } else {
      pstmt.setNull(13, Types.VARCHAR);
    }
    if (item.has("score")) {
      pstmt.setLong(14, item.getLong("score"));
    } else {
      pstmt.setNull(14, Types.BIGINT);
    }
    if (item.has("body")) {
      pstmt.setString(15, item.getString("body"));
    } else {
      pstmt.setNull(15, Types.VARCHAR);
    }

    pstmt.executeUpdate();
    pstmt.close();

    if (item.has("tags")) {
      JSONArray tags = item.getJSONArray("tags");
      long questionId = item.getLong("question_id");
      for (int i = 0; i < tags.length(); i++) {
        String tag = tags.getString(i);
        String sql = "INSERT INTO question_tag (question_id, tag) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO question_tag (question_id, tag) VALUES (?, ?)");
        ps.setLong(1, questionId);
        ps.setString(2, tag);
        ps.executeUpdate();
        ps.close();
      }
    }
  }
}



