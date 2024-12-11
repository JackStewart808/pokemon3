package stats.pokemon;

import java.util.ArrayList;
import stats.pokemon.cards.*;
import stats.pokemon.cards.energies.*;
import stats.pokemon.cards.pokemon.*;

import java.util.Collections;
import java.util.Scanner;

public class PokemonGame
{
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> prizes = new ArrayList<>();
    private Pokemon currentActivePokemon;

    private ArrayList<Card> enemyDeck = new ArrayList<>();
    private ArrayList<Card> enemyHand = new ArrayList<>();
    private ArrayList<Card> enemyPrizes = new ArrayList<>();
    private Pokemon enemyCurrentActivePokemon;
    Scanner in = new Scanner(System.in);

    public PokemonGame()
    {
        deck = new ArrayList<>();
        hand = new ArrayList<>();
        prizes = new ArrayList<>();

        enemyDeck = new ArrayList<>();
        enemyHand = new ArrayList<>();
        enemyPrizes = new ArrayList<>();
    }


    public void generateDeck(boolean isForPlayer)
    {
        ArrayList<Card> desiredDeckToFill;
        if(isForPlayer)
        {
            desiredDeckToFill = deck;
        }
        else
        {
            desiredDeckToFill = enemyDeck;
        }

        for(int i = 0; i < 5; i++)
        {
            desiredDeckToFill.add(new Blastoise());
            desiredDeckToFill.add(new Charizard());
            desiredDeckToFill.add(new Venusaur());
        }

        while(desiredDeckToFill.size() < 60)
        {
            desiredDeckToFill.add(new WaterEnergy());
            desiredDeckToFill.add(new FireEnergy());
            desiredDeckToFill.add(new GrassEnergy());
        }

        while(desiredDeckToFill.size() > 60)
        {
            desiredDeckToFill.remove(desiredDeckToFill.size() - 1);
        }

        Collections.shuffle(desiredDeckToFill);
    }

    public void generateHands(boolean isForPlayer)
    {
        ArrayList<Card> handToBeGenerated;
        ArrayList<Card> deckToBeUsed;
        if(isForPlayer)
        {
            handToBeGenerated = hand;
            deckToBeUsed = deck;
        }
        else
        {
            handToBeGenerated = enemyHand;
            deckToBeUsed = enemyDeck;
        }
        boolean validHand = false;
        while(!validHand)
        {
            //Take the first 7 cards off the deck and add them to hand
            for(int i = 0; i < 7; i++)
            {
                handToBeGenerated.add(deckToBeUsed.get(0));
                deckToBeUsed.remove(0);
            }
            for(Card card : handToBeGenerated)
            {
                if(card instanceof Pokemon)
                {
                    validHand = true;
                    break;
                }
            }
            if(!validHand)
            {
                for(int i = 0; i < 7; i++)
                {
                    deckToBeUsed.add(handToBeGenerated.get(0));
                    handToBeGenerated.remove(0);
                }
                Collections.shuffle(deckToBeUsed);
            }
        }
    }

    public void generatePrizes(boolean isForPlayer)
    {
        ArrayList<Card> prizesToBeGenerated;
        ArrayList<Card> deckToBeUsed;
        if(isForPlayer)
        {
            prizesToBeGenerated = prizes;
            deckToBeUsed = deck;
        }
        else
        {
            prizesToBeGenerated = enemyPrizes;
            deckToBeUsed = enemyDeck;
        }

        //Take the first 6 cards off the deck and add them to prizes
        for(int i = 0; i < 6; i++)
        {
            prizesToBeGenerated.add(deckToBeUsed.get(0));
            deckToBeUsed.remove(0);
        }
    }


    public void printBoard()
    {
        for(int i = 0; i < 10; i++)
        {
            System.out.println();
        }
        System.out.println("Enemy Has " + enemyPrizes.size() + " Prizes Remaining To Win.");

        System.out.println("Enemy has " + enemyHand.size() + " Cards in their hand currently.");
        if(enemyCurrentActivePokemon != null)
        {
            System.out.println("Enemy Active Pokemon is " + enemyCurrentActivePokemon.getName() + 
            " with " + enemyCurrentActivePokemon.getEnergyCount() + " " + enemyCurrentActivePokemon.getType() + " Energies and " + 
            enemyCurrentActivePokemon.getHp() + " Health Points.");
        }
        else
        {
            System.out.println("The Enemy Active Pokemon Is Currently Dead.");
        }


        System.out.println();
        System.out.println();

        if(currentActivePokemon != null)
        {
            System.out.println("Your Active Pokemon is " + currentActivePokemon.getName() + 
            " with " + currentActivePokemon.getEnergyCount() + " " + currentActivePokemon.getType() + " Energies and " + 
            currentActivePokemon.getHp() + " Health Points.");
        }
        else
        {
            System.out.println("Your Active Pokemon Is Currently Dead.");
        }

        System.out.println("Your Hand Currently Contains");
        for(int i = 0; i < hand.size(); i++)
        {
            System.out.print((i + 1) + ". " + hand.get(i).getName() + ",  ");
        }
        System.out.println();

        System.out.println("You Have " + prizes.size() + " Prizes Remaining To Win.");
    }

    public void drawCard(boolean isForPlayer)
    {
        if(isForPlayer)
        {
            hand.add(deck.get(0));
            deck.remove(0);
        }
        else
        {
            enemyHand.add(enemyDeck.get(0));
            enemyDeck.remove(0);
        }
    }

    public void playerSwapPokemon()
    {
        System.out.println();
        System.out.println();

        boolean handIsSwappable = false;
        for(Card card : hand)
        {
            if(card instanceof Pokemon)
            {
                handIsSwappable = true;
            }
        }
        if(handIsSwappable)
        {
            System.out.println("Would you like to swap out your active pokemon? Type the index of the pokemon you would like to swap or press 0 for no swap.");

            int swapChoice = in.nextInt();
            if(swapChoice == 0 && currentActivePokemon.getHp() > 0)
            {
                return;
            }
            while(swapChoice > hand.size() || swapChoice < 0 || !(hand.get(swapChoice - 1) instanceof Pokemon))
            {
                System.out.println("Invalid Option please try again");
                swapChoice = in.nextInt();

                if(swapChoice == 0)
                {
                    return;
                }
            }

            

            Pokemon tempPokemon = currentActivePokemon;
            currentActivePokemon = (Pokemon) hand.get(swapChoice - 1);
            hand.set(swapChoice - 1, tempPokemon);
        }
        else
        {
            return;
        }
    }

    public void playerApplyEnergies()
    {
        int validEnergyCount = 0;
        Energy tempCard;
        for(Card card : hand)
        {
            if(card instanceof Energy)
            {
                tempCard = (Energy) card;
                if(tempCard.getType().toString().equals(currentActivePokemon.getType().toString()))
                {
                    validEnergyCount++;
                }
            }
        }
        
        if(validEnergyCount > 4)
        {
            validEnergyCount = 4;
        }

        validEnergyCount -= currentActivePokemon.getEnergyCount();

        if(validEnergyCount == 0)
        {
            System.out.println("You cannot add any energy to this pokemon");
            return;
        }

        System.out.println("You can add up to " + validEnergyCount + 
        " additional energy to your Active Pokemon, How many would you like to add?");

        int energyAdded = in.nextInt();
        while(energyAdded < 0 || energyAdded > validEnergyCount)
        {
            energyAdded = in.nextInt();
        }
        currentActivePokemon.setEnergyCount(currentActivePokemon.getEnergyCount() + energyAdded);
        Energy tempCard2;
        for(Card card : hand)
        {
            if(card instanceof Energy && validEnergyCount > 0)
            {
                tempCard2 = (Energy) card;
                if(tempCard2.getType().equals(currentActivePokemon.getType()))
                {
                    validEnergyCount--;
                    hand.remove(tempCard2);             
                }
            }
        }
    }

    public void playerAttack()
    {
        System.out.println("Would you like to use 1. " + 
        currentActivePokemon.getAttack1Name() + "(0 Energy), 2. " + 
        currentActivePokemon.getAttack2Name() + "(1 Energy), \n                      3. " + 
        currentActivePokemon.getAttack3Name() + "(2 Energy), 4. " + 
        currentActivePokemon.getAttack4Name() + "(3 Energy)");
        int enemyHpLoss = 0;
        int playerAttackChoice = in.nextInt();
        while(playerAttackChoice < 1 || playerAttackChoice > 4 || playerAttackChoice > currentActivePokemon.getEnergyCount() + 1)
        {
            System.out.println("That was an invalid move, try again");
            playerAttackChoice = in.nextInt();
        }

        if(playerAttackChoice == 1)
        {
            enemyHpLoss = currentActivePokemon.attack1(enemyCurrentActivePokemon);
        }
        else if(playerAttackChoice == 2)
        {
            enemyHpLoss = currentActivePokemon.attack2(enemyCurrentActivePokemon);
        }
        else if(playerAttackChoice == 3)
        {
            enemyHpLoss = currentActivePokemon.attack3(enemyCurrentActivePokemon);
        }
        else if(playerAttackChoice == 4)
        {
            enemyHpLoss = currentActivePokemon.attack4(enemyCurrentActivePokemon);
        }
        else
        {
            System.out.println("attackError");
        }

        enemyCurrentActivePokemon.damage(enemyHpLoss);

    }

    public void enemySwapPokemon()
    {
        if(enemyCurrentActivePokemon.getHp() == 0)
        {
            for(Card card : enemyHand)
            {
                if(card instanceof Pokemon)
                {
                    enemyCurrentActivePokemon = (Pokemon) card;
                    enemyHand.remove(card);
                    return;
                }
            }
        }
    }

    public void enemyApplyEnergies()
    {
        for(Card card : hand)
        {
            if(card instanceof Energy)
            {
                if(((Energy)card).getType().equals(enemyCurrentActivePokemon.getType()) && enemyCurrentActivePokemon.getEnergyCount() < 3)
                {
                    enemyCurrentActivePokemon.setEnergyCount(enemyCurrentActivePokemon.getEnergyCount() + 1);
                    hand.remove(card);
                }
            }
        }
    }

    public void enemyAttack()
    {
        int enemyEnergyCount = enemyCurrentActivePokemon.getEnergyCount();
        if(enemyEnergyCount == 0)
        {
            currentActivePokemon.damage(enemyCurrentActivePokemon.attack1(currentActivePokemon));
        }
        else if(enemyEnergyCount == 1)
        {
            currentActivePokemon.damage(enemyCurrentActivePokemon.attack2(currentActivePokemon));
        }
        else if(enemyEnergyCount == 2)
        {
            currentActivePokemon.damage(enemyCurrentActivePokemon.attack3(currentActivePokemon));
        }
        else if(enemyEnergyCount == 3)
        {
            currentActivePokemon.damage(enemyCurrentActivePokemon.attack4(currentActivePokemon));
        }
    }

    public void enemyChoosesStarter()
    {
        for(Card card : enemyHand)
        {
            if(card instanceof Pokemon)
            {
                enemyCurrentActivePokemon = (Pokemon) card;
                hand.remove(card);
            }
        }
    }

    public void playerChoosesStarter()
    {
        for(int i = 0; i < 15; i++)
        {
            System.out.println();
        }
        
        System.out.println("Your Hand Currently Contains");
        for(int i = 0; i < hand.size(); i++)
        {
            System.out.print((i + 1) + ". " + hand.get(i).name + ",  ");
        }
        System.out.println();

        System.out.println("Choose Your Starting Pokemon, Must be a Pokemon not a Trainer or an Energy Card");
        int playerChoice = in.nextInt();
        while(playerChoice > 7 || playerChoice < 1 || !(hand.get(playerChoice - 1) instanceof Pokemon))
        {
            System.out.println("Invalid Choice, Try Again!");
            playerChoice = in.nextInt();
        }

        currentActivePokemon = (Pokemon) hand.get(playerChoice - 1); 
        hand.remove(playerChoice - 1);

        System.out.println();
        System.out.println();
    }

    public void removeDeadPokemon()
    {
        if(currentActivePokemon.getHp() == 0)
        {
            currentActivePokemon = null;
            enemyHand.add(enemyPrizes.get(0));
            enemyPrizes.remove(0);
        }
        if(enemyCurrentActivePokemon.getHp() == 0)
        {
            enemyCurrentActivePokemon = null;
            hand.add(prizes.get(0));
            prizes.remove(0);
        }
    }

    public int checkForGameEnd()
    {
        if(currentActivePokemon == null)
        {   
            boolean validPokemonFound = false;
            for(Card card : hand)
            {
                if(card instanceof Pokemon)
                {
                    validPokemonFound = true;
                    break;
                }
            }
            if(!validPokemonFound)
            {
                return -1;
            }
        }

        if(enemyCurrentActivePokemon == null)
        {   
            boolean validPokemonFound = false;
            for(Card card : enemyHand)
            {
                if(card instanceof Pokemon)
                {
                    validPokemonFound = true;
                    break;
                }
            }
            if(!validPokemonFound)
            {
                return 1;
            }
        }

        if(prizes.size() == 0)
        {
            return 1;
        }

        if(enemyPrizes.size() == 0)
        {
            return -1;
        }

        return 0;
    }

    public void gameLoop()
    {
        int gameEndValue = 0; //1 means player Win, -1 means player lose, 0 means game not over yet
        boolean gameLoopActive = true;
        boolean firstLoop = true;
        while(gameLoopActive)
        {
            //Perform the player's actions
            drawCard(true);

            printBoard();

            if(!firstLoop)
            {
                playerSwapPokemon();
            }
            else
            {
                firstLoop = false;
            }

            printBoard();

            playerApplyEnergies();

            printBoard();

            playerAttack();

            removeDeadPokemon();

            //Perform the computer's actions
            drawCard(false);

            enemySwapPokemon();

            enemyApplyEnergies();

            enemyAttack();

            removeDeadPokemon();

            gameEndValue = checkForGameEnd();
            if(gameEndValue == 1)
            {
                System.out.println("Congrats, You Won!");
                return;
            }
            else if(gameEndValue == -1)
            {
                System.out.println("The enemy has defeated you, try again?");
                return;
            }
        
        }
    }

    public void runGame()
    {
        generateDeck(true);
        generateDeck(false);

        generateHands(true);
        generateHands(false);

        generatePrizes(true);
        generatePrizes(false);

        enemyChoosesStarter();

        playerChoosesStarter();

        gameLoop();
    }
}