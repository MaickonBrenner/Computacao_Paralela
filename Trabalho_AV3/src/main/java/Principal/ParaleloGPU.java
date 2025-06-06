package Principal;

import org.jocl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ParaleloGPU {
    
    private final String kernelCL =
        "__kernel void busca_palavra_linha(__global const char *texto," +
        "                                  __global const int *indices_linhas," +
        "                                  __global int *resultado," +
        "                                  __global const char *palavra," +
        "                                  const int palavra_len) {" +
        "    int linha_id = get_global_id(0);" +
        "    int inicio = indices_linhas[linha_id];" +
        "    int fim = indices_linhas[linha_id + 1] - 1;" +
        "    for (int i = inicio; i <= fim - palavra_len; i++) {" +
        "        if (texto[i] == palavra[0]) {" +
        "            int encontrou = 1;" +
        "            for (int j = 1; j < palavra_len; j++) {" +
        "                if (texto[i + j] != palavra[j]) {" +
        "                    encontrou = 0;" +
        "                    break;" +
        "                }" +
        "            }" +
        "            if (encontrou) {" +
        "                resultado[linha_id] = 1;" +
        "                break;" +
        "            }" +
        "        }" +
        "    }" +
        "}";

    public int buscarPalavra(String arquivo, String palavra) {
        // 1. Carrega o texto do arquivo e define os índices das linhas
        StringBuilder textoCompleto = new StringBuilder();
        List<Integer> indicesLinhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                indicesLinhas.add(textoCompleto.length());
                textoCompleto.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return 0;
        }

        // 2. Converte dados para byte arrays e buffers
        byte[] textoBytes = textoCompleto.toString().getBytes(StandardCharsets.UTF_8);
        byte[] palavraBytes = palavra.getBytes(StandardCharsets.UTF_8);

        // Adiciona fim da última linha
        indicesLinhas.add(textoBytes.length);
        int[] indices = indicesLinhas.stream().mapToInt(Integer::intValue).toArray();
        int[] resultado = new int[indices.length - 1];

        // 3. Configura o OpenCL
        CL.setExceptionsEnabled(true);
        cl_platform_id platform = getPlatform();
        cl_device_id device = getDevice(platform);
        cl_context context = CL.clCreateContext(null, 1, new cl_device_id[]{device}, null, null, null);
        cl_command_queue queue = CL.clCreateCommandQueueWithProperties(context, device, null, null);

        // 4. Criação dos buffers
        cl_mem textoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * textoBytes.length, Pointer.to(textoBytes), null);
        cl_mem indicesBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * indices.length, Pointer.to(indices), null);
        cl_mem resultadoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY,
                Sizeof.cl_int * resultado.length, null, null);
        cl_mem palavraBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * palavraBytes.length, Pointer.to(palavraBytes), null);

        // 5. Compilação e configuração do kernel
        cl_program program = CL.clCreateProgramWithSource(context, 1, new String[]{kernelCL}, null, null);
        CL.clBuildProgram(program, 0, null, null, null, null);
        cl_kernel kernel = CL.clCreateKernel(program, "busca_palavra_linha", null);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(textoBuffer));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(indicesBuffer));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(resultadoBuffer));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(palavraBuffer));
        CL.clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{palavraBytes.length}));

        // 6. Executa o kernel
        long[] globalWorkSize = new long[]{resultado.length};
        CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);

        // 7. Lê o buffer de saída
        CL.clEnqueueReadBuffer(queue, resultadoBuffer, CL.CL_TRUE, 0,
                Sizeof.cl_int * resultado.length, Pointer.to(resultado), 0, null, null);

        int ocorrencias = 0;
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] == 1) {
                ocorrencias++;
            }
        }

        // Libera recursos
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

    private static cl_platform_id getPlatform() {
        int[] numPlatformsArray = new int[1];
        CL.clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        if (numPlatforms == 0) {
            throw new RuntimeException("Nenhuma plataforma OpenCL encontrada.");
        }
        cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
        CL.clGetPlatformIDs(numPlatforms, platforms, null);

        for (cl_platform_id platform : platforms) {
            String name = getPlatformName(platform);
            System.out.println("Plataforma encontrada: " + name);
            if (name.toLowerCase().contains("intel")) {
                System.out.println("Usando plataforma Intel.");
                return platform;
            }
        }
        System.out.println("Plataforma Intel não encontrada, usando a primeira disponível.");
        return platforms[0];
    }

    private static String getPlatformName(cl_platform_id platform) {
        byte[] buffer = new byte[1024];
        CL.clGetPlatformInfo(platform, CL.CL_PLATFORM_NAME, buffer.length, Pointer.to(buffer), null);
        int i = 0;
        while (i < buffer.length && buffer[i] != 0) i++;
        return new String(buffer, 0, i);
    }


    private static cl_device_id getDevice(cl_platform_id platform) {
        cl_device_id[] devices = new cl_device_id[1];
//        int result = CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_GPU, 1, devices, null);
        int result = CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_CPU, 1, devices, null);
        if (result != CL.CL_SUCCESS) {
            System.out.println("GPU não disponível. Tentando usar CPU...");
            result = CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_CPU, 1, devices, null);
            if (result != CL.CL_SUCCESS) {
                throw new RuntimeException("Nenhum dispositivo OpenCL disponível.");
            }
        }
        return devices[0];
    }


}
