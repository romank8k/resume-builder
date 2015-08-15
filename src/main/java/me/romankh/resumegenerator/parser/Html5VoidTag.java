package me.romankh.resumegenerator.parser;

/**
 * @author Roman Khmelichek
 */
public enum Html5VoidTag {
  AREA("area"),
  BASE("base"),
  BR("br"),
  COL("col"),
  COMMAND("command"),
  EMBED("embed"),
  HR("hr"),
  IMG("img"),
  INPUT("input"),
  KEYGEN("keygen"),
  LINK("link"),
  META("meta"),
  PARAM("param"),
  SOURCE("source"),
  TRACK("track"),
  WBR("wbr");

  private String tag;

  Html5VoidTag(String tag) {
    this.tag = tag;
  }

  String getTag() {
    return tag;
  }

  static Html5VoidTag findTag(String tag) {
    for (Html5VoidTag html5VoidTag : values()) {
      if (html5VoidTag.tag.equals(tag))
        return html5VoidTag;
    }

    return null;
  }
}
