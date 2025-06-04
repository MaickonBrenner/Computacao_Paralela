package Principal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import org.jocl.*;

public class ParaleloGPU {
	private String kernelCL = 
		    "__kernel void busca_palavra(__global const char *texto," +
		    "                            __global int *resultado," +
		    "                            const int texto_len," +
		    "                            __global const char *palavra," +
		    "                            const int palavra_len) {" +
		    "    int i = get_global_id(0);" +
		    "    if (i > texto_len - palavra_len) return;" +
		    "    int encontrou = 1;" +
		    "    for (int j = 0; j < palavra_len; j++) {" +
		    "        if (texto[i + j] != palavra[j]) {" +
		    "            encontrou = 0;" +
		    "            break;" +
		    "        }" +
		    "    }" +
		    "    if (encontrou) resultado[i] = 1;" +
		    "}"; 

    public void buscarPalavra(String arquivo, String palavra) {
    	try {
    		String livro = new String(Files.readAllBytes(Paths.get(arquivo)));
    		byte[] arquivoBytes = livro.getBytes(StandardCharsets.UTF_8);
    		byte[] padraoBytes = palavra.getBytes(StandardCharsets.UTF_8); // Palavra
    		int[] resultado = new int[arquivoBytes.length];
    		System.out.println("Arquivo carregado com sucesso!" );
    		
    		CL.setExceptionsEnabled(true);
    		cl_platform_id platform = getPlatform();
    		cl_device_id device = getDevice(platform);
    		cl_context context = CL.clCreateContext(null, 1, new cl_device_id[]{device}, null, null, null);
    		cl_command_queue queue = CL.clCreateCommandQueueWithProperties(context, device, null, null);
    		
    		cl_mem arquivoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, 
    				arquivoBytes.length, Pointer.to(arquivoBytes), null);
    		cl_mem padraoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, 
    				padraoBytes.length, Pointer.to(padraoBytes), null);
            cl_mem resultadoBuffer = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY, 
                    resultado.length * Sizeof.cl_int, null, null);
    		
            cl_program programa = CL.clCreateProgramWithSource(context, 1, new String[]{kernelCL}, null, null);
            CL.clBuildProgram(programa, 0, null, null, null, null);
            cl_kernel kernel = CL.clCreateKernel(programa, "busca_palavra", null);
            
            CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(arquivoBuffer));
            CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(resultadoBuffer));
            CL.clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{arquivoBytes.length}));
            CL.clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(padraoBuffer));
            CL.clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{padraoBytes.length}));
    	
            long[] tamanhoProblema = new long[] {arquivoBytes.length}; // Global Work Size
            CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, tamanhoProblema, null, 0, null, null);
            CL.clEnqueueReadBuffer(queue, resultadoBuffer, CL.CL_TRUE, 0, resultado.length * Sizeof.cl_int, 
                    Pointer.to(resultado), 0, null, null);
            
            CL.clReleaseMemObject(arquivoBuffer);
            CL.clReleaseMemObject(padraoBuffer);
            CL.clReleaseMemObject(resultadoBuffer);
            CL.clReleaseKernel(kernel);
            CL.clReleaseProgram(programa);
            CL.clReleaseCommandQueue(queue);
            CL.clReleaseContext(context);

    	} catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + arquivo + ". " + e.getMessage());
        }
    	
    	
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

