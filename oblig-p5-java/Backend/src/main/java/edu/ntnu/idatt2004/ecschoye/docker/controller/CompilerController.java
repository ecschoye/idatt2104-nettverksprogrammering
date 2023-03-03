package edu.ntnu.idatt2004.ecschoye.docker.controller;

import edu.ntnu.idatt2004.ecschoye.docker.model.Code;
import edu.ntnu.idatt2004.ecschoye.docker.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Controller class for handling HTTP requests to compile Python code using Docker containers.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class CompilerController {

    private static final Logger logger = Logger.getLogger(CompilerController.class.getName());

    @Autowired
    private CompilerService compilerService;

    /**
     * Handles a POST request to compile the specified Python code and returns the output.
     *
     * @param code the Python code to compile
     * @return the output from compiling the code
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/compile")
    public String recieveAndCompile(@RequestBody Code code) throws IOException {
        logger.info("Received request to compile code: " + code.getCode());
        return compilerService.compileCode(code);
    }
}