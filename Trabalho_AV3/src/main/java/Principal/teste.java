package Principal;

import org.jocl.*;

public class teste {
    public static void main(String[] args) {
        CL.setExceptionsEnabled(true);

        // Obtém número de plataformas
        int[] numPlatformsArray = new int[1];
        CL.clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        System.out.println("Número de plataformas OpenCL: " + numPlatforms);

        cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
        CL.clGetPlatformIDs(numPlatforms, platforms, null);

        for (int i = 0; i < numPlatforms; i++) {
            cl_platform_id platform = platforms[i];
            String platformName = getString(platform, CL.CL_PLATFORM_NAME);
            System.out.println("Plataforma " + i + ": " + platformName);

            // Obtém número de dispositivos para essa plataforma
            int[] numDevicesArray = new int[1];
            int result = CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];

            if (result != CL.CL_SUCCESS) {
                System.out.println("  Não foi possível obter dispositivos.");
                continue;
            }

            System.out.println("  Número de dispositivos: " + numDevices);

            cl_device_id[] devices = new cl_device_id[numDevices];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);

            for (int j = 0; j < numDevices; j++) {
                cl_device_id device = devices[j];
                String deviceName = getString(device, CL.CL_DEVICE_NAME);
                long deviceType = getLong(device, CL.CL_DEVICE_TYPE);
                String typeString = deviceTypeToString(deviceType);
                System.out.println("    Dispositivo " + j + ": " + deviceName + " (" + typeString + ")");
            }
        }
    }

    private static String getString(cl_platform_id platform, int paramName) {
        // Query string info from platform
        long[] size = new long[1];
        CL.clGetPlatformInfo(platform, paramName, 0, null, size);
        byte[] buffer = new byte[(int)size[0]];
        CL.clGetPlatformInfo(platform, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }

    private static String getString(cl_device_id device, int paramName) {
        // Query string info from device
        long[] size = new long[1];
        CL.clGetDeviceInfo(device, paramName, 0, null, size);
        byte[] buffer = new byte[(int)size[0]];
        CL.clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }

    private static long getLong(cl_device_id device, int paramName) {
        long[] values = new long[1];
        CL.clGetDeviceInfo(device, paramName, Sizeof.cl_long, Pointer.to(values), null);
        return values[0];
    }

    private static String deviceTypeToString(long deviceType) {
        if ((deviceType & CL.CL_DEVICE_TYPE_CPU) != 0) return "CPU";
        if ((deviceType & CL.CL_DEVICE_TYPE_GPU) != 0) return "GPU";
        if ((deviceType & CL.CL_DEVICE_TYPE_ACCELERATOR) != 0) return "ACCELERATOR";
        if ((deviceType & CL.CL_DEVICE_TYPE_DEFAULT) != 0) return "DEFAULT";
        return "UNKNOWN";
    }
}
