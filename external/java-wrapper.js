const { exec } = require('child_process');

/**
 * Executes JavaScript code via the Rhino Java executor (RhinoExecutor.jar).
 * @param {string} script - JavaScript code to be executed.
 * @param {function} callback - Callback function to handle results or errors.
 */
function executeJavaScript(script, scriptName, callback) {
    // Prepare the Java command with the script passed as an argument
    const command = `./java -cp ./SampleTestRhino.jar:/opt/external_config/* org.example.Main "${script}", "${scriptName}"`;

    // Log the command for debugging (optional)
    console.log(`Executing command: ${command}`);

    // Execute the Java command
    exec(command, (error, stdout, stderr) => {
        if (error) {
            // Log the error from the Java process and invoke callback with the error
            console.error(`Java execution error: ${stderr}`);
            callback(new Error(`JavaScript execution failed: ${stderr}`), null);
            return;
        }

        // If execution is successful, return the result
        console.log(`JavaScript executed successfully. Output: ${stdout}`);
        callback(null, stdout.trim());
    });
}

module.exports = executeJavaScript;
