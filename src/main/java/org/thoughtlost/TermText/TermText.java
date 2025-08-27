package org.thoughtlost.TermText;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

import static java.lang.Math.min;


public class TermText {

    // parent is a reference to the parent sketch, and to Processing commands
    PApplet parent;
    float x, y, fontSize, rowheight, charwidth;
    int cols, rows, cursorX, cursorY;
    PFont font;
    boolean loaded;

    PGraphics canvas;

    ArrayList<TermRow> termrows;

    public TermText(PApplet theParent, float x, float y,
                    float w, float h, float size, PGraphics canv) {
        parent = theParent;
        loaded = false;
        font = parent.createFont("Monospaced", size);
        //font = createFont("Noto Serif Hebrew ExtraCondensed Light", 24);
        this.x = x;
        this.y = y;
        canvas = canv;

        cursorX = 0;
        cursorY = 0;
        fontSize = size;
        canvas.textFont(font);
        canvas.textSize(size);
        termrows = new ArrayList<TermRow>();

        charwidth = canvas.textWidth("W") * 1.1f;
        cols = PApplet.floor(w / charwidth);
        rowheight = (canvas.textAscent() + canvas.textDescent()) * 1.1f;
        rows = PApplet.floor(h / rowheight);

        for (int j = 0; j < rows; j++) {
            termrows.add(new TermRow(cols, charwidth, canvas));
        }
        loaded = true;
    }

    public void println(String str, int c) {
        newLine();
        TermRow tr = new TermRow(cols, charwidth, canvas);
        tr.addString(str, c);
        termrows.set(cursorY,tr);
        newLine();
    }

    public void cls() {
        termrows = new ArrayList<TermRow>();

        for (int j=0; j < rows; j++) {
            termrows.add(new TermRow(cols, charwidth, canvas));
        }
        cursorY = 0;
        cursorX = 0;
    }

    public void newLine() {
        cursorY++;
        if (cursorY >= rows) {
            for (int i=0; i<rows-1; i++) {
                termrows.set(i, termrows.get(i+1));
            }
            cursorY = rows-1;
            termrows.set(cursorY, new TermRow(cols, charwidth, canvas));
        }
        cursorX = 0;
    }

    public void print(String str, int c) {
        // do we need to line wrap?
        if (str.length() + cursorX > cols) {
            String a = str.substring(0, cols-cursorX);
            String b = str.substring(cols-cursorX, str.length());
            this.print(a, c);
            newLine();
            this.print(b, c);
        } else {
            termrows.get(cursorY).addString(str, c);
            cursorX += str.length();
        }
    }

    public void draw() {
        if (loaded) {
            canvas.textFont(font);
            canvas.textSize(fontSize);

            for (int j=0; j < rows; j++) {
                termrows.get(j).draw(x, y+j*rowheight);
            }
            if (PApplet.floor(parent.millis()/400.0f) % 2 == 0) {
                canvas.fill(255,100);
                canvas.noStroke();
                canvas.rect(x + cursorX*charwidth, y + cursorY*rowheight,
                        charwidth, -rowheight);
            }
        }
    }
}

class TermRow {
    ArrayList<TermChar> txt;
    int w;
    float charwidth;
    PGraphics canvas;

    TermRow(int w, float cw, PGraphics canv) {
        txt = new ArrayList<TermChar>();
        charwidth = cw;
        canvas = canv;
        this.w = w;
    }

    void add(TermChar tc) {
        txt.add(tc);
    }

    void addString(String str, int c) {
        for (int i=0; i < str.length(); i++) {
            txt.add(new TermChar(str.charAt(i), c, canvas));
        }
    }

    void draw(float x, float y) {
        int eol = min(w, txt.size());
        for (int i=0; i < eol; i++) {
            txt.get(i).draw(x + i*charwidth, y);
        }
    }
}

class TermChar {
    String t;
    int c;
    PGraphics canvas;

    TermChar(String text, int col, PGraphics canv) {
        t = text;
        c = col;
        canvas = canv;
    }

    TermChar(char txt, int col, PGraphics canv) {
        t = "" + txt;
        c = col;
        canvas = canv;
    }

    void draw(float x, float y) {
        canvas.noStroke();
        canvas.fill(c);
        canvas.text(t, x, y);
    }
}