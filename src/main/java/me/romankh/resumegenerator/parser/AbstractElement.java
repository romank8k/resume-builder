package me.romankh.resumegenerator.parser;

public class AbstractElement {
  protected final String name;

  public AbstractElement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
