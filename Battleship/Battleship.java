import java.util.Random;
import java.util.Scanner;
/*
 * Roberto Soto Leites
 * A01023599
 * 2017-08-19
 */
public class Battleship {
    public static void main(String[] args){
        //SETUP
        int[][] sea= new int[10][10]; //y, x
        //0-Empty, 1-friendly, 2-enemy, 3-hit, 4-miss, 5-sunk
        //Change status[2] to show enemies (useful for testing)
        //Change back to '^' when done
        char[] status= {'^','@','^','X','O','#'};
        int ships=0;
        int enemies=0;
        boolean[][] guess= new boolean[10][10];
        boolean sleep=true;
        Scanner kb= new Scanner (System.in);
        Random rand= new Random();

        System.out.println("  ***  WELCOME TO  ****");
        System.out.println("  **** BATTLESHIP *****");
        System.out.println("Rules:");
        System.out.println("You share the grid with your enemies. Don't shoot your own ships!");
        System.out.println("The enemy will never shoot the same place twice.");
        System.out.println("The enemy will never shoot themselves. Read their moves!");
        stop(sleep);
        stop(sleep);
        stop(sleep);
        printOcean(sea, status);
        System.out.println("Your ships: @");
        System.out.println("Hits: X");
        System.out.println("Misses: O");
        System.out.println("Sunk ships: #");
        stop(sleep);
        stop(sleep);
        stop(sleep);
        System.out.println();

        //SET SHIPS
        System.out.println("Send out your fleet!");
        while(ships<5){
            System.out.println();
            System.out.print("Enter x coordinate of ship "+(ships+1)+": ");
            int x= kb.nextInt();
            System.out.print("Enter y coordinate of ship "+(ships+1)+": ");
            int y= kb.nextInt();
            if(((y>9)||(x>9))){
                System.out.println("ERROR: Coordinates must be between 0 and 9!");
            }
            else if (sea[y][x]==0){
                sea[y][x]=1;
                ++ships;
                printOcean(sea, status);
            }
            else{
                System.out.println("There is already a ship there! Please try again.");
            }
        }

        System.out.println("Deploying enemy armada!");
        while(enemies<5){
            int x=rand.nextInt(10);
            int y=rand.nextInt(10);
            if(!guess[y][x]){
                if(sea[y][x]==0){
                    stop(sleep);
                    guess[y][x]=true;
                    sea[y][x]=2;
                    ++enemies;
                    System.out.println("Enemy ship "+enemies+" deployed!");
                }
            }
        }

        //PLAY
        int turn=0;
        while((ships>0)&&(enemies>0)){
            //Player's turn
            if(turn==0){
                //System.out.println("--------------------------------------------------");
                printOcean(sea, status);
                System.out.println("YOUR TURN TO ATTACK!");
                System.out.print("Enter x coordinate: ");
                int x= kb.nextInt();
                System.out.print("Enter y coordinate: ");
                int y= kb.nextInt();
                //Invalid coordinates
                if((y>9)||(x>9)||(x<0)|(y<0)){
                    System.out.println("Coordinates must be between 0 and 9!");
                }
                else{
                    //0-Empty, 1-friendly, 2-enemy, 3-hit, 4-miss
                    //Own ship hit
                    stop(sleep);
                    System.out.println();
                    if (sea[y][x]==1){
                        System.out.println("      You just shot your own ship, captain!");
                        sea[y][x]=5;
                        --ships;
                    }
                    //Enemy hit
                    else if(sea[y][x]==2){
                        System.out.println("      ##########Kaboom! Enemy hit!##########");
                        sea[y][x]=3;
                        --enemies;
                    }
                    //Shot on dead enemy
                    else if(sea[y][x]==3){
                        System.out.println("        You've shot a sunken enemy vessel!");
                    }
                    else if(sea[y][x]==5){
                        System.out.println("      ---------Splash! You missed!---------");
                    }
                    //Miss
                    else{
                        System.out.println("      ---------Splash! You missed!---------");
                        sea[y][x] = 4;
                    }
                    turn=1;
                    stop(sleep);
                }
                System.out.println();
                System.out.println("                  ##################              ");
                System.out.println("Enemies remaining: "+enemies+" | Friendly ships remaining: "+ships);
            }
            //Computer's turn
            else if(turn==1){
                int x=rand.nextInt(10);
                int y=rand.nextInt(10);
                //if guess[y][x] is true, either the enemy has a ship there or enemy already shot there
                if(!guess[y][x]){
                    //System.out.println("                  ##################              ");
                    stop(sleep);
                    System.out.println();
                    System.out.println("ENEMY'S TURN TO ATTACK!");
                    stop(sleep);
                    guess[y][x]=true;
                    System.out.println("Enemy shot on x:"+x+", y:"+y+"!");
                    stop(sleep);
                    //0-Empty, 1-friendly, 2-enemy, 3-hit, 4-miss, 5-sunk
                    //Miss
                    if((sea[y][x]==0)||(sea[y][x]==4)){
                        System.out.println("      ------------Enemy missed!------------");
                    }
                    //Hit
                    else if(sea[y][x]==1){
                        System.out.println("  #####We've been hit! Mayday! Abandon ship!#####");
                        sea[y][x]=5;
                        --ships;
                    }
                    //Delete this, it exists just to check that it doesn't happen
                    else{
                        System.out.println("Error");
                    }
                    turn=0;
                    stop(sleep);
                    System.out.println();
                    System.out.println("--------------------------------------------------");
                    System.out.println("Enemies remaining: "+enemies+" | Friendly ships remaining: "+ships);
                }
            }
            //System.out.println();
            //System.out.println("Enemies remaining: "+enemies+" | Friendly ships remaining: "+ships);
        }

        System.out.println();
        System.out.println("GAME OVER");
        if(ships>enemies){
            System.out.println("================ VICTORY ==================");
            System.out.println("Another victory for the Royal Navy! Hurrah!");
        }
        else{
            System.out.println("=========DEFEAT=========");
            System.out.println("We shall have our revenge!");
            status[2]='E';
            System.out.println("Remaining enemy locations:");
            printOcean(sea, status);
        }
        /*
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                System.out.print(guess[i][j]+" ");
            }
            System.out.println();
        }*/
    }
    private static void printOcean(int[][] sea, char[] status){
        System.out.println();
        System.out.println(" | 0_1_2_3_4_5_6_7_8_9 |");
        for(int y=0; y<10; y++){
            System.out.print(y+"|");
            for(int x=0; x<10; x++){
                System.out.print(' ');
                System.out.print(status[sea[y][x]]);
            }
            System.out.println(" |"+y);
        }
        System.out.println(" | 0_1_2_3_4_5_6_7_8_9 |");
        System.out.println();
    }
    private static void stop(boolean use){
        //The IDE (IntelliJ) auto-filled the try and catch statements when I used Thread.sleep();
        //Therefore, I put it in a method to avoid copy/pasting so many lines of code
        //boolean toggles it on and off
        if(use) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
