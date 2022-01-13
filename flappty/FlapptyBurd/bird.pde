class bird {
  float y, vel, grav;
  
  void p(){
    fill(200,0,0);
    arc(50, y, 40, 40, 0, TWO_PI);
  }
  
  void fall(){
    y+=vel*grav;
    vel+=grav;
  }
  
  void jump(){
    vel=-15;
  }
}