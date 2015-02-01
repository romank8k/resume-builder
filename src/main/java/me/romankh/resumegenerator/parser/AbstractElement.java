package me.romankh.resumegenerator.parser;

/**
 * @author Roman Khmelichek
 */
public class AbstractElement {
  protected final String name;

  public AbstractElement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
