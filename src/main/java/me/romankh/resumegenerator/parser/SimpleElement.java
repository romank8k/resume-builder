package me.romankh.resumegenerator.parser;

import java.util.ArrayList;
import java.util.List;

public class SimpleElement extends AbstractElement {
  private final List<StringBuilder> stringBuilderList;

  public SimpleElement(String name) {
    super(name);
    this.stringBuilderList = new ArrayList<>();
  }

  public void append(char[] ch, int start, int length) {
    int i = stringBuilderList.size() - 1;
    assert i >= 0;
    StringBuilder sb = stringBuilderList.get(i);
    sb.append(ch, start, length);
  }

  public List<String> getValues() {
    List<String> values = new ArrayList<>();
    for (StringBuilder st : stringBuilderList) {
      values.add(st.toString());
    }
    return values;
  }

  public void addValue() {
    stringBuilderList.add(new StringBuilder());
  }

  public String getValue() {
    return getValue(0);
  }

  public String getValue(int i) {
    if (stringBuilderList.size() > i)
      return stringBuilderList.get(i).toString();

    return null;
  }
}
