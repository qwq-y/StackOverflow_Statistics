package com.example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeSnippetExtractor {

  public static List<String> extractJavaCodeSnippets(List<String> htmlBodies) {
    List<String> javaCodeSnippets = new ArrayList<>();

    for (String htmlBody : htmlBodies) {
      String markdown = convertHtmlToMarkdown(htmlBody);
      String extractedCodeSnippets = extractJavaCodeUsingRegex(markdown);
      javaCodeSnippets.add(extractedCodeSnippets);
    }

    return javaCodeSnippets;
  }

  public static String convertHtmlToMarkdown(String html) {
    Document doc = Jsoup.parse(html);
    StringBuilder markdownBuilder = new StringBuilder();

    Elements elements = doc.body().children();
    for (Element element : elements) {
      convertElementToMarkdown(element, markdownBuilder);
    }

    return markdownBuilder.toString();
  }

  public static void convertElementToMarkdown(Element element, StringBuilder markdownBuilder) {
    String tagName = element.tagName();

    switch (tagName) {
      case "h1":
        markdownBuilder.append("# ").append(element.text()).append("\n\n");
        break;
      case "h2":
        markdownBuilder.append("## ").append(element.text()).append("\n\n");
        break;
      case "h3":
        markdownBuilder.append("### ").append(element.text()).append("\n\n");
        break;
      case "p":
        markdownBuilder.append(element.text()).append("\n\n");
        break;
      case "pre":
        Elements codeBlocks = element.getElementsByTag("code");
        for (Element codeBlock : codeBlocks) {
          markdownBuilder.append("```java\n").append(codeBlock.text()).append("\n```\n\n");
        }
        break;
      case "ul":
        Elements listItems = element.children();
        for (Element listItem : listItems) {
          markdownBuilder.append("- ").append(listItem.text()).append("\n");
        }
        markdownBuilder.append("\n");
        break;
      default:
        markdownBuilder.append(element.text()).append("\n\n");
    }

    Elements children = element.children();
    for (Element child : children) {
      convertElementToMarkdown(child, markdownBuilder);
    }
  }

  public static String extractJavaCodeUsingRegex(String markdown) {
    String pattern = "```java\\s*([\\s\\S]*?)\\s*```";
    Pattern codePattern = Pattern.compile(pattern);
    Matcher matcher = codePattern.matcher(markdown);
    StringBuilder javaCodeBuilder = new StringBuilder();

    while (matcher.find()) {
      String codeBlock = matcher.group(1);
      javaCodeBuilder.append(codeBlock).append("\n");
    }

    return javaCodeBuilder.toString();
  }

}
