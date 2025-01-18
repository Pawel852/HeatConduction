import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplicitMethod {


    public static double[] Fluent(int controlVolumes) {
        // Parametry fizyczne i geometria
        double k = 0.25; // W/mK
        double Cp = 2000; // J/kgK
        double rho = 1300; // kg/m^3
        double T_p = 180; // °C
        double ts = 130; // °C
        double ti = 18; // °C
        double a = 0.005; // m
        double A = 1; // m^2
        double dtau = 1; // s
        boolean untilLoop = true;


        double dx = 2 * a / controlVolumes;

        // Inicjalizacja objętości kontrolnych
        List<double[]> volumes = new ArrayList<>();
        for (int i = 0; i < controlVolumes; i++) {
            double AW = i == 0 ? k * A / (dx / 2) : k * A / dx;
            double AE = i == controlVolumes - 1 ? k * A / (dx / 2) : k * A / dx;

            volumes.add(new double[]{AW,  AE});
        }

        // Ustawienia początkowe temperatur
        List<Double> temperatures = new ArrayList<>();
        temperatures.add(T_p);
        for (int i = 0; i < controlVolumes; i++) {
            temperatures.add(ti);
        }
        temperatures.add(T_p);
        List<Double> newTemperatures = new ArrayList<>(temperatures);

        double accumulatedTime = dtau;

        // Metoda jawna
        if (!untilLoop) {
            int iterations = 8;
            for (int i = 0; i < iterations; i++) {
                updateTemperatures(volumes, temperatures, newTemperatures, dx, dtau, rho, Cp, A);
                accumulatedTime += dtau;

            }
        } else {
            while (temperatures.get(temperatures.size() / 2) <= ts) {
                updateTemperatures(volumes, temperatures, newTemperatures, dx, dtau, rho, Cp, A);
                accumulatedTime += dtau;


            }
        }

        return new double[]{controlVolumes, accumulatedTime};
    }

    private static void updateTemperatures(List<double[]> volumes, List<Double> temperatures,
                                           List<Double> newTemperatures, double dx, double dtau,
                                           double rho, double Cp, double A) {
        double Z = rho * dx * A * Cp / dtau;



        for (int j = 1; j < temperatures.size() - 1; j++) {
            double AW = volumes.get(j - 1)[0];
            double AE = volumes.get(j - 1)[1];
            double TWest = temperatures.get(j - 1);
            double TEast = temperatures.get(j + 1);
            double TOld = temperatures.get(j);
            double TNew = (AW * TWest + AE * TEast + (Z - AW - AE) * TOld) / Z;
            newTemperatures.set(j, TNew);
        }

        for (int j = 1; j < temperatures.size() - 1; j++) {
            temperatures.set(j, newTemperatures.get(j));
        }
    }
}
