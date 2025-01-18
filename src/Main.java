import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ImplicitMethod implicitMethod = new ImplicitMethod();
        ExplicitMethod explicitMethod = new ExplicitMethod();

        List<Integer> listOfControlVolumes = new ArrayList<>();
        List<double[]> results = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert method name (Implicit/Explicit): ");
        String methodName = scanner.nextLine();



        for (int i = 5; i <= 63; i += 2) {
            listOfControlVolumes.add(i);
        }

        for (int mesh : listOfControlVolumes) {
            if (methodName.equals("Implicit")) {
                results.add(implicitMethod.Fluent(mesh));
            } else if(methodName.equals("Explicit")) {
                results.add(explicitMethod.Fluent(mesh));
            }else {
                System.out.println("Invalid method name");
                break;
            }
        }

        for (double[] result : results) {
            System.out.println("| Węzeł: " + (int) (result[0]+2) + " | tau: " + result[1]+" | ");
            System.out.println("-".repeat(25));
        }

    }
}