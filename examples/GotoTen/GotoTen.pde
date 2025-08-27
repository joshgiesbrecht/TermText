import org.thoughtlost.TermText.*;

TermText tt;

void setup() {
  size(800,800);
  
  tt = new TermText(this, 0, 0, width, height, 20, this.g);

}

void draw() {
  background(30,10,0);
  
  for (int i=0; i < 10; i++) {
    
    if (random(1) < 0.2) {
      tt.print("#", color(255,140,0));
    } else {
      tt.print(". ", color(255,97,0));
    }
  }
  tt.draw();
}