#!/usr/bin/env node

// Required modules
const JSONStream = require("pixl-json-stream");
const Logger = require("pixl-logger");
const Perf = require("pixl-perf");
const executeJavaScript = require("./java-wrapper");

// Initialize performance measurement
const perf = new Perf();
perf.setScale(1); // seconds
perf.begin();

// Setup stdin and stdout streams
process.stdin.setEncoding("utf8");
process.stdout.setEncoding("utf8");

// Create JSON stream for task communication
const stream = new JSONStream(process.stdin, process.stdout);

stream.on("json", (job) => {
  const columns = [
    "hires_epoch",
    "date",
    "hostname",
    "category",
    "code",
    "msg",
    "data",
  ];
  const logger = new Logger(job.log_file || "rhino_job.log", columns);

  logger.set("hostname", job.hostname || "unknown");
  logger.set("debugLevel", 9);

  try {
    // Validate input parameters
    if (!job.params || !job.params.script) {
      throw new Error('Invalid task parameters: "script" is required.');
    }

    const script = job.params.script,
      scriptName = job.params.script_name;

    logger.debug(9, "Received job:", job);

    // Execute the JavaScript code
    executeJavaScript(script, scriptName, (error, result) => {
      if (error) {
        logger.error("Error executing script:", error.message, error);
        return stream.write({
          complete: 1,
          code: 1,
          description: "ERROR",
          error: error.message,
          perf: perf.summarize(),
        });
      }

      logger.debug(9, "Script executed successfully. Output:", result);

      stream.write({
        complete: 1,
        code: 0,
        description: "SUCCESS",
        output: result,
        perf: perf.summarize(),
      });
    });
  } catch (err) {
    logger.error("Unexpected error:", err.message, err);
    stream.write({
      complete: 1,
      code: 1,
      description: "ERROR",
      error: err.message,
      perf: perf.summarize(),
    });
  } finally {
    perf.end();
  }
});
