import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplicitMethod {


    public static double[] Fluent(int controlVolumes) {

        // Dane i założenia
        double k = 0.25; // W/mK
        double Cp = 2000; // J/kgK
        double rho = 1300; // kg/m^3
        double T_p = 180; // C
        double ts = 130; // C
        double ti = 18; // C
        double a = 0.005; // m1
        double A = 1; // m^2
        double dtau = 1; // s
        double errorThreshold = 1e-8;

        // Geometria
        double dx = 2 * a / controlVolumes; // m


        // Siatka kontrolna
        List<double[]> Volumes = new ArrayList<>();
        Volumes.add(new double[]{k * A / (dx / 2), dx / 2, k * A / dx});
        for (int i = 1; i < controlVolumes - 1; i++) {
            Volumes.add(new double[]{k * A / dx, dx / 2 + dx * i, k * A / dx});
        }
        Volumes.add(new double[]{k * A / dx, 2 * a - dx / 2, k * A / (dx / 2)});

        // Warunki początkowe
        double[] Temperatures = new double[controlVolumes + 2];
        Temperatures[0] = Temperatures[Temperatures.length - 1] = T_p;
        for (int i = 1; i < Temperatures.length - 1; i++) {
            Temperatures[i] = ti;
        }

        // Parametry dla metody implicit
        double[][] coefficients = new double[Volumes.size()][3];
        double Z = rho * dx * A * Cp / dtau;

        for (int i = 0; i < Volumes.size(); i++) {
            double[] volume = Volumes.get(i);
            double denominator = volume[0] + Z + volume[2];
            coefficients[i][0] = volume[0] / denominator;
            coefficients[i][1] = volume[2] / denominator;
            coefficients[i][2] = Z / denominator;
        }

        double tau = dtau;
        boolean targetNotReached = true;


        while (targetNotReached) {
            // Inicjalizacja temperatur
            double[] temperatures = Temperatures.clone();
            for (int i = 1; i < temperatures.length - 1; i++) {
                temperatures[i] = (ts + ti) / 2;
            }


            double maxError;

            do {
                maxError = 0;

                for (int i = 1; i < temperatures.length - 1; i++) {
                    double newTemp = coefficients[i - 1][0] * temperatures[i - 1]
                            + coefficients[i - 1][1] * temperatures[i + 1]
                            + coefficients[i - 1][2] * Temperatures[i];
                    maxError = Math.max(maxError, Math.abs(temperatures[i] - newTemp));
                    temperatures[i] = newTemp;
                }


            } while (maxError > errorThreshold);

            // Aktualizacja temperatur
            System.arraycopy(temperatures, 1, Temperatures, 1, Temperatures.length - 2);

            // Sprawdzanie temperatury w środku siatki
            if (temperatures[temperatures.length / 2] >= ts) {
                targetNotReached = false;
            }

            tau += dtau;

        }

        return new double[]{controlVolumes, tau};
    }
}
