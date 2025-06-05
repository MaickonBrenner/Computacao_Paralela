package Principal;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jocl.*;

public class ParaleloGPU {
	private String kernelCL = 
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
	        "            if (encontrou) resultado[i] = 1;" +
	        "        }" +
	        "    }" +
	        "}";

    public void buscarPalavra(String arquivo, String palavra) {
    	List<Integer> indicesLinhas = new ArrayList<>();
    	StringBuilder livro = new StringBuilder();
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
    		String linha;
    		while((linha = br.readLine()) != null) {
    			indicesLinhas.add(livro.length());
    			livro.append(linha).append("/n");
    		}
    	} catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + arquivo + ". " + e.getMessage());
        }
    	
    	byte[] arquivoBytes = livro.toString().getBytes(StandardCharsets.UTF_8);
		byte[] palavraBytes = palavra.getBytes(StandardCharsets.UTF_8); 
		int[] resultado = new int[arquivoBytes.length];
		System.out.println("Arquivo carregado com sucesso!" );
		
    	CL.setExceptionsEnabled(true);
		cl_platform_id platform = getPlatform();
		cl_device_id device = getDevice(platform);
		cl_context context = CL.clCreateContext(null, 1, new cl_device_id[]{device}, null, null, null);
		cl_command_queue queue = CL.clCreateCommandQueueWithProperties(context, device, null, null);
		
		cl_mem arquivoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, 
				arquivoBytes.length, Pointer.to(arquivoBytes), null);
		cl_mem palavraBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, 
				palavraBytes.length, Pointer.to(palavraBytes), null);
        cl_mem resultadoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY, 
                resultado.length * Sizeof.cl_int, null, null);
		
        cl_program programa = CL.clCreateProgramWithSource(context, 1, new String[]{kernelCL}, null, null);
        CL.clBuildProgram(programa, 0, null, null, null, null);
        cl_kernel kernel = CL.clCreateKernel(programa, "busca_palavra_linha", null);
        
        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(arquivoBuffer));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(resultadoBuffer));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{arquivoBytes.length}));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(palavraBuffer));
        CL.clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{palavraBytes.length}));
	
        long[] tamanhoProblema = new long[] {arquivoBytes.length}; // Global Work Size
        CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, tamanhoProblema, null, 0, null, null);
        CL.clEnqueueReadBuffer(queue, resultadoBuffer, CL.CL_TRUE, 0, resultado.length * Sizeof.cl_int, 
                Pointer.to(resultado), 0, null, null);
        
        CL.clReleaseMemObject(arquivoBuffer);
        CL.clReleaseMemObject(palavraBuffer);
        CL.clReleaseMemObject(resultadoBuffer);
        CL.clReleaseKernel(kernel);
        CL.clReleaseProgram(programa);
        CL.clReleaseCommandQueue(queue);
        CL.clReleaseContext(context);
    	
    }
    
    private static cl_platform_id getPlatform() {
        cl_platform_id[] platforms = new cl_platform_id[1];
        CL.clGetPlatformIDs(1, platforms, null);
        return platforms[0];
    }

    private static cl_device_id getDevice(cl_platform_id platform) {
        cl_device_id[] devices = new cl_device_id[1];
        CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_GPU, 1, devices, null);
        return devices[0];
    }
}

