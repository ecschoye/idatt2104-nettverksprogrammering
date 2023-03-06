package edu.ntnu.idatt2004.ecschoye.docker.service;

import edu.ntnu.idatt2004.ecschoye.docker.model.Code;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Service class for compiling Python code using Docker containers.
 */
@Service
public class CompilerService {

    private static final Logger logger = Logger.getLogger(CompilerService.class.getName());

    /**
     * Compiles the specified Python code using a Docker container and returns the output.
     *
     * @param code the Python code to compile
     * @return the output from compiling the code
     * @throws IOException if an I/O error occurs
     */
    public String compileCode(Code code) throws IOException {
        String pythonCode = code.getCode();

        String[] dockerCommand = {"docker", "run", "--rm", "python:3.9-alpine", "python", "-c", pythonCode};

        ProcessBuilder pb = new ProcessBuilder(dockerCommand);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        String result = output.toString();

        try {
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                logger.info("Error: " + result);
                return "Error: " + result;
            }
        } catch (InterruptedException e) {
            logger.info("Error: " + e.getMessage());
            e.printStackTrace();
        }
        logger.info("Result: " + result);
        return result;
    }
}
