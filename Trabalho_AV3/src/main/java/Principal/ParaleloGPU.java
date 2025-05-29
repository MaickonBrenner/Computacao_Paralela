package Principal;

import org.jocl.*;

public class ParaleloGPU {
    public void processar() {
        System.setProperty("java.library.path", "lib");
        CL.setExceptionsEnabled(true);

        int[] numPlatforms = new int[1];
        CL.clGetPlatformIDs(0, null, numPlatforms);

        System.out.println("Número de plataformas OpenCL disponíveis: " + numPlatforms[0]);
    }
}

