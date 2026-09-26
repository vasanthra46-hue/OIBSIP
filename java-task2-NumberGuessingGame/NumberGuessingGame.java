import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int round = 1;
        char playAgain;

        do {
            int number = random.nextInt(100) + 1;
            int attempts = 0;
            boolean correct = false;

            System.out.println("\n--- Round " + round + " ---");
            System.out.println("Guess a number between 1 and 100");
            System.out.println("You have 4 attempts.");

            while (attempts < 4) {
                System.out.print("Enter your guess: ");
                int guess = sc.nextInt();
                attempts++;

                if (guess == number) {
                    System.out.println("Correct! 🎉");
                    System.out.println("You guessed it in " + attempts + " attempts.");
                    correct = true;
                    break;
                } else if (guess > number) {
                    System.out.println("Too High!");
                } 
				else {
                    System.out.println("Too Low!");
                }
            }

            if (!correct) {
                System.out.println("You Lost!");
                System.out.println("The number was: " + number);
            }

            System.out.print("Play Again? (Y/N): ");
            playAgain = sc.next().charAt(0);
            round++;

        } while (playAgain == 'Y' || playAgain == 'y');

        System.out.println("Thanks for playing!");
        sc.close();
    }
}