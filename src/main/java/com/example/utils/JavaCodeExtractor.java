package com.example.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class JavaCodeExtractor {

  public List<String> extractJavaCodeSnippets(List<String> jsonBodies) {
    List<String> javaCodeSnippets = new ArrayList<>();

    for (String jsonBody : jsonBodies) {
      try {
        JSONObject jsonObject = new JSONObject(jsonBody);
        String body = jsonObject.getString("body");

        Pattern pattern = Pattern.compile("```java\\s+(.*?)\\s+```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
          javaCodeSnippets.add(matcher.group(1));
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return javaCodeSnippets;
  }

//  public List<String> extractJavaCodeSnippets(List<String> jsonBodies) {
//    List<String> javaCodeSnippets = new ArrayList<>();
//
//    for (String jsonBody : jsonBodies) {
//      try {
//        System.out.println(jsonBody);
//        JSONObject jsonObject = new JSONObject(jsonBody);
//        String body = jsonObject.getString("body");
//
//        // 检查是否被压缩
//        if (isCompressed(body)) {
//          body = decompress(body);
//        }
//
//        Pattern pattern = Pattern.compile("```java\\s+(.*?)\\s+```", Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(body);
//
//        while (matcher.find()) {
//          javaCodeSnippets.add(matcher.group(1));
//        }
//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
//    }
//
//    return javaCodeSnippets;
//  }
//
//  private boolean isCompressed(String body) {
//    // 在这里添加你检查压缩标志或特征的逻辑
//    // 例如，检查是否包含 Gzip 压缩标志
//    return body.startsWith("\u001f\u008b");
//  }
//
//  private String decompress(String compressedBody) {
//    // 在这里添加解压缩逻辑
//    // 例如，使用 GZIPInputStream 解压缩
//    try {
//      ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBody.getBytes());
//      GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
//      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//      byte[] buffer = new byte[1024];
//      int length;
//      while ((length = gzipInputStream.read(buffer)) != -1) {
//        outputStream.write(buffer, 0, length);
//      }
//
//      return outputStream.toString();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    return compressedBody;
//  }

}

