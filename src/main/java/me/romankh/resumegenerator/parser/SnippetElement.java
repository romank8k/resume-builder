package me.romankh.resumegenerator.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * @author Roman Khmelichek
 */
public class SnippetElement extends ResumeElement {
  // Maintain a stack to handle nested snippets.
  public Deque<Snippet> activeElementStack = new ArrayDeque<>();

  // List of snippets that are popped off the stack.
  public List<Snippet> snippets = new ArrayList<>();

  public SnippetElement(DefaultHandler parent, XMLReader parser, String elementName) {
    super(parent, parser, elementName, null);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    Snippet.Modifier modifier = Snippet.Modifier.getModifierByName(qName);
    if (modifier == null)
      throw new IllegalStateException("Unexpected tag within snippet: " + qName);

    Snippet currSnippet = new Snippet();
    Snippet top = activeElementStack.peekLast();
    if (top != null) {
      // This is a nested snippet. Need to copy the list of modifiers to the new snippet.
      for (Snippet.Modifier currModifier : top.getModifierList()) {
        currSnippet.addModifier(currModifier);
      }

      // Remove the previous snippet from the stack and add to the list of snippets.
      // Only if we do not have any modifiers.
      if (top.getModifierList().isEmpty())
        snippets.add(activeElementStack.removeLast());
    }

    currSnippet.addModifier(modifier);
    activeElementStack.addLast(currSnippet);
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    Snippet.Modifier modifier = Snippet.Modifier.getModifierByName(qName);

    if (modifier != null || qName.equals(elementName)) {
      snippets.add(activeElementStack.removeLast());
    }

    super.endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    Snippet top = activeElementStack.peekLast();
    if (top != null) {
      top.append(ch, start, length);
    } else {
      top = new Snippet();
      top.append(ch, start, length);
      activeElementStack.addLast(top);
    }
  }

  public List<Snippet> getSnippets() {
    return snippets;
  }

  public static class Snippet {
    private final StringBuilder sb;
    private final List<Modifier> modifierList;

    public enum Modifier {
      BOLD("b", 1), ITALIC("i", 2), HYPERLINK("a", 3);

      private final String tag;
      private final int order;

      Modifier(String tag, int order) {
        this.tag = tag;
        this.order = order;
      }

      public String getTag() {
        return tag;
      }

      public int getOrder() {
        return order;
      }

      public static Modifier getModifierByName(String name) {
        for (Modifier modifier : values()) {
          if (modifier.tag.equals(name))
            return modifier;
        }

        return null;
      }
    }

    public Snippet() {
      this.sb = new StringBuilder();
      this.modifierList = new ArrayList<>();
    }

    public void append(char[] ch, int start, int length) {
      sb.append(ch, start, length);
    }

    public String getText() {
      return sb.toString();
    }

    public void addModifier(Modifier modifier) {
      modifierList.add(modifier);
    }

    public List<Modifier> getModifierList() {
      return modifierList;
    }

    public List<Modifier> getOrderedModifierList() {
      Collections.sort(modifierList, new Comparator<Modifier>() {
        @Override
        public int compare(Modifier lhs, Modifier rhs) {
          return Integer.compare(lhs.order, rhs.order);
        }
      });
      return modifierList;
    }
  }
}
