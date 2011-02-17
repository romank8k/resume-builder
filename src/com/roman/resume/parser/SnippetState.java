// Copyright (c) 2010, Roman Khmelichek
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
//  1. Redistributions of source code must retain the above copyright notice,
//     this list of conditions and the following disclaimer.
//  2. Redistributions in binary form must reproduce the above copyright notice,
//     this list of conditions and the following disclaimer in the documentation
//     and/or other materials provided with the distribution.
//  3. Neither the name of Roman Khmelichek nor the names of its contributors
//     may be used to endorse or promote products derived from this software
//     without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
// EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.roman.resume.parser;

public class SnippetState {
  // Allows support for modifiers for certain words.
  public static class Snippet {
    public String text = new String();

    public enum Modifier {
      BOLD, HYPERLINK, NONE
    };

    public Modifier modifier;

    public Snippet(String text, Modifier modifier) {
      this.text = text;
      this.modifier = modifier;
    }
  }

  private String currSnippet = new String();
  private boolean hyperlinkP = false;
  private boolean boldP = false;

  public void startSpecialSnippet(String qName) {
    if (qName.equals("a")) {
      System.out.println("START a");
      hyperlinkP = true;
    }

    if (qName.equals("b")) {
      System.out.println("START b");
      boldP = true;
    }
  }

  public void endSpecialSnippet(String qName) {
    if (qName.equals("a")) {
      System.out.println(currSnippet);
      System.out.println("END a");
      hyperlinkP = false;
    }

    if (qName.equals("b")) {
      System.out.println(currSnippet);
      System.out.println("END b");
      boldP = false;
    }
  }

  public Snippet getSnippet(String str) {
    Snippet snippet;
    if (boldP) {
      snippet = new Snippet(str, Snippet.Modifier.BOLD);
    } else if (hyperlinkP) {
      snippet = new Snippet(str, Snippet.Modifier.HYPERLINK);
    } else {
      snippet = new Snippet(str, Snippet.Modifier.NONE);
    }

    return snippet;
  }

  public void setCurrSnippet(String s) {
    currSnippet = s;
  }
}