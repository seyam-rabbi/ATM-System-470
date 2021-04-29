import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ATM {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Bank theBank = new Bank("Brac Bank");
        User aUser = theBank.addUser("Saadman", "Ahmed", "1234");
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        //theBank.addAccount(newAcount);

        User curUser;
        while (true) {
            //stay in login prompt until successful login
            curUser = ATM.userMainMenuPrompt(theBank, sc);

            //stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }

    private static User userMainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User validUser;

//        prompt user for login until correct id and pass is provided
        do {
            System.out.printf("\nWelcome to %s\n", theBank.getName());
            System.out.print("Enter your ID: ");
            userID = sc.nextLine();
            System.out.print("Enter your pin: ");
            pin = sc.nextLine();

//            try to get user object for provided id and pin
            validUser = theBank.userLogin(userID, pin);
            if (validUser == null) {
                System.out.println("credentials error. " + "please enter right id or pin.");
            }
        }
        while (validUser == null) ; /*continue loop until successful login*/

        return validUser;
    }


    public static void printUserMenu(User theUser, Scanner sc) {
        // print a summary of user accounts
        theUser.printsAccountSummary();
        int choice;
        do {
            System.out.printf("Welcome %s, what would you like to do", theUser.getFirstName());
            System.out.println();
            System.out.println(" 1) show transaction history");
            System.out.println(" 2) withdrawl");
            System.out.println(" 3) deposit");
            System.out.println(" 4) transfer");
            System.out.println(" 5) quit");
            System.out.println();
            System.out.println("enter choide");
            choice = sc.nextInt();

            if (choice > 5 || choice < 1) {
                System.out.println("invalid choice.plese insert a number between 1 to 5");

            }

        } while (choice > 5 || choice < 1);

        //process choice
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawlFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }
        //redisplay menu until user quits
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }

    }


    public static void depositFunds(User theUser, Scanner sc) {
        String memo;

        int toAcct;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("invalid account.please try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        do {
            System.out.printf("Enter the amount to deposit (max$%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("amount should be greater than null");
            }

        } while (amount < 0 );
        sc.nextLine();
        System.out.println("enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(toAcct, amount, memo);
    }



    public static void withdrawlFunds(User theUser, Scanner sc) {
        int fromAcct;
        String memo;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: ",theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("invalid account.please try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the amount to withdraw (max$%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("amount should be greater than null");
            } else if (amount > acctBal) {
                System.out.printf("amount should be less than\n" + "balance of $%.02f.\n", acctBal);
            }

        } while (amount < 0 || amount > acctBal);
        sc.nextLine();
        System.out.println("enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(fromAcct, -1 * amount, memo);
    }



    public static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;
        // get account whose transaction history is looking at
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "whose transaction you want to see: ", theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("invalid account.please try again");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        //print transaction history
        theUser.printAcctTransHistory(theAcct);

    }




    public static void transferFunds(User theUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("invalid account.please try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to: ");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("invalid account.please try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        do {
            System.out.printf("Enter the amount to transfer (max$%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("amount should be greater than null");
            } else if (amount > acctBal) {
                System.out.printf("amount should be less than\n" + "balance of $%.02f.\n", acctBal);
            }

        } while (amount < 0 || amount > acctBal);

        // do the transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format("Tansfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format("Tansfer to account %s", theUser.getAcctUUID(fromAcct)));
    }




}



