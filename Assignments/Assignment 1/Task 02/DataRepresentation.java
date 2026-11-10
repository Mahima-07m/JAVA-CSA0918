import java.util.Scanner;
public class DataRepresentation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] slotsPerFloor = {2, 3, 1};
        String[][] slots = new String[slotsPerFloor.length][];
        for (int i = 0; i < slotsPerFloor.length; i++) slots[i] = new String[slotsPerFloor[i]];
        System.out.print("Vehicle number: ");
        String vehicle = sc.nextLine();
        slots[0][0] = vehicle;
        int minutes = 75;
        int billableHours = minutes / 60 + (minutes % 60 == 0 ? 0 : 1);
        double fee = billableHours * 30.0;
        System.out.println("Stored vehicle: " + slots[0][0]);
        System.out.println("Parking fee: Rs " + fee);
        sc.close();
    }
}