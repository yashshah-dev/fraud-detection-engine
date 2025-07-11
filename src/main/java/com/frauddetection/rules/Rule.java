package com.frauddetection.rules;

public class Rule {
  private String id;
  private String name;
  private String expression;
  private boolean active;

  public Rule(String id, String name, String expression, boolean active) {
    this.id = id;
    this.name = name;
    this.expression = expression;
    this.active = active;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
