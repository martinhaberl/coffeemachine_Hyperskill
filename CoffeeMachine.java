package machine;

import java.util.Scanner;

public class CoffeeMachine {

    private int[] fillingLevels; //water, milk, coffee, money, cups
    private final int[] REQUIRED_WATER = {0, 250, 350, 200}; //0- dummy, 1 - Espresso, 2 - Latte, 3 - Cappuccino
    private final int[] REQUIRED_MILK = {0, 0, 75, 100}; //0- dummy, 1 - Espresso, 2 - Latte, 3 - Cappuccino
    private final int[] REQUIRED_COFFEE_BEANS = {0, 16, 20, 12}; //0- dummy, 1 - Espresso, 2 - Latte, 3 - Cappuccino
    private final int[] PRICES = {0, 4, 7, 6}; //0- dummy, 1 - Espresso, 2 - Latte, 3 - Cappuccino
    private boolean shutdown = false;
    CoffeeMachineStatus currentStatus;
    private int coffeeChoice;

    private final CoffeeMachineStatus choosingAction = CoffeeMachineStatus.CHOOSING_AN_ACTION_TO_START;
    private final CoffeeMachineStatus filling = CoffeeMachineStatus.MAINTENANCE_FILLING;
    private final CoffeeMachineStatus buyingVariant = CoffeeMachineStatus.CHOOSING_VARIANT_OF_COFFEE;

    public static void main(String[] args) {
        CoffeeMachine myCoffeeMachine = new CoffeeMachine();
    }

    public CoffeeMachine() {
        fillingLevels = new int[]{400, 540, 120, 550, 9}; //water, milk, coffee, money, cups
        operateCoffeeMachine();
    }

    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public void operateCoffeeMachine() {
        currentStatus = CoffeeMachineStatus.CHOOSING_AN_ACTION_TO_START;
        do {
            switch (currentStatus) {
                case CHOOSING_AN_ACTION_TO_START:
                    System.out.println("Write one action: buy, fill, remaining, take or exit:");
                    String input = getInput();
                    switch (input) {
                        case "fill":
                            currentStatus = filling;
                            break;
                        case "remaining":
                            printLevels();
                            break;
                        case "take":
                            takeMoney();
                            break;
                        case "buy":
                            currentStatus = buyingVariant;
                            break;
                        case "exit":
                            shutdownCoffeeMachine();
                            break;
                        default:
                            break;
                    }
                    break;
                case MAINTENANCE_FILLING:
                    fillCoffeeMachine();
                    break;

                case CHOOSING_VARIANT_OF_COFFEE:
                    buyCoffee();
                    break;
            }
        } while (!shutdown);
    }

    private void shutdownCoffeeMachine() {
        shutdown = true;
        System.exit(0);
    }

    private void buyCoffee() {
        getCoffeeChoice();
        calculateIngredients();
        currentStatus = choosingAction;
    }

    private void fillCoffeeMachine() {

        System.out.println("Write how many ml of water to fill:");
        String additionalWater = getInput();
        fillingLevels[0] += Integer.parseUnsignedInt(additionalWater);

        System.out.println("Write how many ml of milk to fill:");
        String additionalMilk = getInput();
        fillingLevels[1] += Integer.parseUnsignedInt(additionalMilk);

        System.out.println("Write how many grams of coffee beans to fill:");
        String additionalCoffee = getInput();
        fillingLevels[2] += Integer.parseUnsignedInt(additionalCoffee);

        System.out.println("Write how many disposable cups to fill:");
        String additionalCups = getInput();
        fillingLevels[4] += Integer.parseUnsignedInt(additionalCups);

        currentStatus = CoffeeMachineStatus.CHOOSING_AN_ACTION_TO_START;
    }

    private void takeMoney() {
        int moneyToTake = fillingLevels[3];
        if (fillingLevels[3] > 0) {
            fillingLevels[3] = 0;
            System.out.println("I gave you $" + moneyToTake + "\n");
        }
    }

    private void getCoffeeChoice() {

        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String input = getInput();
        switch (input) {
            case "1":
            case "2":
            case "3":
                coffeeChoice = Integer.parseUnsignedInt(input); //Integer.getInteger(selection);
                break;
            case "back":
                operateCoffeeMachine();
                break;
        }
    }

    private void printLevels() {
        System.out.print("The coffee machine has:\n" +
                fillingLevels[0] + " ml of water\n" +
                fillingLevels[1] + " ml of milk\n" +
                fillingLevels[2] + " g of coffee beans\n" +
                fillingLevels[4] + " disposable cups\n" +
                "$" + fillingLevels[3] + " of money\n\n");
    }

    private void trackLevels() {
        fillingLevels[0] -= REQUIRED_WATER[coffeeChoice];
        fillingLevels[1] -= REQUIRED_MILK[coffeeChoice];
        fillingLevels[2] -= REQUIRED_COFFEE_BEANS[coffeeChoice];
        fillingLevels[3] += PRICES[coffeeChoice];
        fillingLevels[4]--;
    }

    private void calculateIngredients() {

        boolean hasEnoughWater = (fillingLevels[0] - REQUIRED_WATER[coffeeChoice]) >= 0;
        boolean hasEnoughMilk = (fillingLevels[1] - REQUIRED_MILK[coffeeChoice]) >= 0;
        boolean hasEnoughCoffeeBeans = (fillingLevels[2] - REQUIRED_COFFEE_BEANS[coffeeChoice]) >= 0;
        boolean hasEnoughCups = fillingLevels[4] > 0;

        if (hasEnoughWater && hasEnoughMilk && hasEnoughCoffeeBeans) {
            System.out.println("I have enough resources, making you a coffee!\n");
            trackLevels();
        } else if (!hasEnoughWater) {
            System.out.println("Sorry, not enough water!\n");
        } else if (!hasEnoughMilk) {
            System.out.println("Sorry, not enough milk!\n");
        } else if (!hasEnoughCoffeeBeans) {
            System.out.println("Sorry, not enough coffee beans!\n");
        } else if (!hasEnoughCups) {
            System.out.println("Sorry, not enough cups!\n");
        } else {
            System.out.println("Sorry, not enough ingredients!\n");
        }
    }
}
