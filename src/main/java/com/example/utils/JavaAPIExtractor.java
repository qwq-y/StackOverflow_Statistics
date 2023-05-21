package com.example.utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.List;

public class JavaAPIExtractor {

  public List<String> extractJavaAPIs(List<String> javaCodeSnippets) {
    List<String> javaAPIs = new ArrayList<>();

    for (String codeSnippet : javaCodeSnippets) {
      try {
        CompilationUnit compilationUnit = StaticJavaParser.parse(codeSnippet);
        JavaAPIVisitor visitor = new JavaAPIVisitor();
        visitor.visit(compilationUnit, javaAPIs);
      } catch (Exception e) {
//        System.out.println("not java code");
      }
    }

    return javaAPIs;
  }

  private static class JavaAPIVisitor extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(MethodDeclaration methodDeclaration, List<String> collector) {
      // Collect method-level Java API mentions
      methodDeclaration.findAll(com.github.javaparser.ast.expr.MethodCallExpr.class)
          .forEach(methodCallExpr -> {
            String methodName = methodCallExpr.getName().getIdentifier();
            collector.add(methodName);
          });
      super.visit(methodDeclaration, collector);
    }
  }

}

