class pillar{
  int pos;
  float gap_height;
  
  void p(){
    fill(255);
    rect(pos, 0, 30, height);
    fill(0);
    rect(pos, gap_height, 30, 200);
  }
}