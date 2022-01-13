bird b;
pillar[] arr;
int s=0, t, t_i, t_n, score=0;

void setup(){
  size(400,600);
  
  arr= new pillar[5];
  b= new bird();
  
  b.y=height/2;
  b.grav=0.65;
  b.vel=1;
  
  for(int i=0; i<5; i++){
    arr[i]=new pillar();
    arr[i].pos=width+(i*300);
    arr[i].gap_height=random(0, height-225);
  }
}

void draw(){
  background(0);
  switch(s){
    case 0:
      fill(255);
      textSize(50);
      text("Flappty Burd!", 50, 150);
      textSize(20);
      text("Hit space to start", 100, height/2);
      if(keyPressed){
        s=1;
        t_i=millis();
      }
      break;
    case 1:
        t_n=millis();
        t=(t_n - t_i)/1000;
      if(3-t>0){
        textSize(50);
        text(3-t, width/2, height/2);
        b.p();
        fill(255);
        textSize(20);
        text("Use space to jump", 50, 50);
        text("Go through the holes!", 50, 80);
      }
      else{
        s=2;
      }
      break;
    case 2:
      for (int p=0;p<5; p++){
        arr[p].p();
        arr[p].pos-=3;
      }
      //print(arr[0].pos);
      //print('\n');
      if(arr[0].pos==1){
        score++;
      }
      if(arr[0].pos<-30){
        arr=shift(arr);
      }
      b.p();
      b.fall();
      fill(0);
      rect(0, height-25, width, 25);
      fill(255);
      text("Score:", 5, height-5);
      text(score, 70, height-5);
      if((b.y<0)||(b.y>height)){
        //GAME OVER
        s=3;
      }
      if((arr[0].pos<65)&&(arr[0].pos>30)){
        if((b.y-20<arr[0].gap_height)||(b.y+20>arr[0].gap_height+200)){
          //GAME OVER
          s=3;
        }
      }
      break;
     case 3:
       fill(255);
       textSize(50);
       text("Game over!", 75, 250);
       text("Score: ", 75, 310);
       text(score, 250, 310);
       fill(250,0,0);
       rect(100, 340, 180, 100);
       fill(255);
       text("Restart", 100, 400);
       if (mousePressed){
         if((mouseX>100)&&(mouseX<280)
           &&(mouseY>340)&&(mouseY<440))
           {
             t_i=millis();
             arr=restart(arr);
             score=0;
             s=1;
           }
       }
       break;
  }
}

pillar[] restart(pillar[] arr){
  b.y=height/2;
  b.vel=1;
  
  for(int i=0; i<5; i++){
    arr[i]=new pillar();
    arr[i].pos=width+(i*300);
    arr[i].gap_height=random(0, height-225);
  }
  
  return arr;
}

pillar[] shift(pillar[] arr){
  for (int i=0; i<4; i++){
    arr[i]=arr[i+1];
  }
  arr[4]=new pillar();
  arr[4].pos=arr[3].pos+300;
  arr[4].gap_height=random(0, height-225);
  return arr;
}

void keyPressed(){
  if(key==' '){
    b.jump();
  }
}