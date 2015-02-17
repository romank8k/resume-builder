package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Roman Khmelichek
 */
public class ResumeMeta {
  @XmlAttribute(name = "margin-left")
  private int marginLeft;

  @XmlAttribute(name = "margin-right")
  private int marginRight;

  @XmlAttribute(name = "margin-top")
  private int marginTop;

  @XmlAttribute(name = "margin-bottom")
  private int marginBottom;

  public int getMarginLeft() {
    return marginLeft;
  }

  public ResumeMeta setMarginLeft(int marginLeft) {
    this.marginLeft = marginLeft;
    return this;
  }

  public int getMarginRight() {
    return marginRight;
  }

  public ResumeMeta setMarginRight(int marginRight) {
    this.marginRight = marginRight;
    return this;
  }

  public int getMarginTop() {
    return marginTop;
  }

  public ResumeMeta setMarginTop(int marginTop) {
    this.marginTop = marginTop;
    return this;
  }

  public int getMarginBottom() {
    return marginBottom;
  }

  public ResumeMeta setMarginBottom(int marginBottom) {
    this.marginBottom = marginBottom;
    return this;
  }
}
