package Principal;

import org.jocl.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ParaleloGPU {

	private final String kernelCL =
			"__kernel void busca_palavra_linha(__global const uchar *texto, " +
					"                                  __global const int *indices_linhas, " +
					"                                  __global int *resultado, " +
					"                                  __global const uchar *palavra, " +
					"                                  const int palavra_len) { " +
					"    int linha_id = get_global_id(0); " +
					"    int inicio = indices_linhas[linha_id]; " +
					"    int fim = indices_linhas[linha_id + 1]; " +
					"    int contador = 0; " +
					"    for (int i = inicio; i <= fim - palavra_len; i++) { " +
					"        int encontrou = 1; " +
					"        for (int j = 0; j < palavra_len; j++) { " +
					"            if (texto[i + j] != palavra[j]) { " +
					"                encontrou = 0; " +
					"                break; " +
					"            } " +
					"        } " +
					"        if (encontrou) { " +
					"            contador++; " +
					"        } " +
					"    } " +
					"    resultado[linha_id] = contador; " +
					"}";

    public int buscarPalavra(List<String> arquivoFormatado, String palavraOriginal) {
        // Pré-processamento: remove pontuação e força minúsculas
        List<String> textoLimpo = new ArrayList<>();
        for (String linha : arquivoFormatado) {
            textoLimpo.add(limparTexto(linha));
        }
        String palavra = limparTexto(palavraOriginal);

        // Concatena o texto completo e define os índices das linhas
        StringBuilder textoCompleto = new StringBuilder();
        List<Integer> indicesLinhas = new ArrayList<>();
        for (String linha : textoLimpo) {
            indicesLinhas.add(textoCompleto.length());
            textoCompleto.append(linha).append(" ");
        }
        indicesLinhas.add(textoCompleto.length()); 

        // Prepara buffers
        byte[] textoBytes = textoCompleto.toString().getBytes(StandardCharsets.UTF_8);
        byte[] palavraBytes = palavra.getBytes(StandardCharsets.UTF_8);
        int[] indices = indicesLinhas.stream().mapToInt(Integer::intValue).toArray();
        int[] resultado = new int[indices.length - 1];

        CL.setExceptionsEnabled(true);

        cl_platform_id platform;
        cl_device_id device;

        try {
            platform = getPlatform(CL.CL_DEVICE_TYPE_GPU);
            device = getDevice(platform, CL.CL_DEVICE_TYPE_GPU);
            System.out.println("Usando plataforma com GPU.");
        } catch (RuntimeException e) {
            System.out.println("GPU não encontrada, tentando CPU...");
            platform = getPlatform(CL.CL_DEVICE_TYPE_CPU);
            device = getDevice(platform, CL.CL_DEVICE_TYPE_CPU);
            System.out.println("Usando plataforma com CPU.");
        }

        cl_context context = CL.clCreateContext(null, 1, new cl_device_id[]{device}, null, null, null);
        cl_command_queue queue = CL.clCreateCommandQueueWithProperties(context, device, null, null);
        
        System.out.println("Palavra: " + palavra + " | Tamanho (bytes): " + palavraBytes.length);


        // Criação dos buffers
        cl_mem textoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * textoBytes.length, Pointer.to(textoBytes), null);
        cl_mem indicesBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * indices.length, Pointer.to(indices), null);
        cl_mem resultadoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY,
                Sizeof.cl_int * resultado.length, null, null);
        cl_mem palavraBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * palavraBytes.length, Pointer.to(palavraBytes), null);

        cl_program program = CL.clCreateProgramWithSource(context, 1, new String[]{kernelCL}, null, null);
        CL.clBuildProgram(program, 0, null, null, null, null);
        cl_kernel kernel = CL.clCreateKernel(program, "busca_palavra_linha", null);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(textoBuffer));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(indicesBuffer));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(resultadoBuffer));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(palavraBuffer));
        CL.clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{palavraBytes.length}));

        long[] globalWorkSize = new long[]{resultado.length};
        CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);

        CL.clEnqueueReadBuffer(queue, resultadoBuffer, CL.CL_TRUE, 0,
                Sizeof.cl_int * resultado.length, Pointer.to(resultado), 0, null, null);

        int ocorrencias = Arrays.stream(resultado).sum();

        // Liberação dos recursos
        CL.clReleaseMemObject(textoBuffer);
        CL.clReleaseMemObject(indicesBuffer);
        CL.clReleaseMemObject(resultadoBuffer);
        CL.clReleaseMemObject(palavraBuffer);
        CL.clReleaseKernel(kernel);
        CL.clReleaseProgram(program);
        CL.clReleaseCommandQueue(queue);
        CL.clReleaseContext(context);

        return ocorrencias;
    }

    private static String limparTexto(String linha) {
        return linha.toLowerCase()
        			.replaceAll("[.,!?:;\"'()\\[\\]¿¡—–-]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
    }

    private static cl_platform_id getPlatform(long deviceType) {
        int[] numPlatformsArray = new int[1];
        CL.clGetPlatformIDs(0, null, numPlatformsArray);
        cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
        CL.clGetPlatformIDs(platforms.length, platforms, null);

        for (cl_platform_id platform : platforms) {
            int[] numDevicesArray = new int[1];
            int res = CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
            if (res == CL.CL_SUCCESS && numDevicesArray[0] > 0) {
                return platform;
            }
        }
        throw new RuntimeException("Nenhuma plataforma com dispositivo do tipo " +
                (deviceType == CL.CL_DEVICE_TYPE_GPU ? "GPU" : "CPU") + " encontrada.");
    }

    private static cl_device_id getDevice(cl_platform_id platform, long deviceType) {
        int[] numDevicesArray = new int[1];
        CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        cl_device_id[] devices = new cl_device_id[numDevicesArray[0]];
        CL.clGetDeviceIDs(platform, deviceType, devices.length, devices, null);
        return devices[0];
    }
}
