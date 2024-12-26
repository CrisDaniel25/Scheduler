#!/usr/bin/env node

// Test Plugin for Cronicle
var fs = require('fs');
var cp = require('child_process');
var JSONStream = require('pixl-json-stream');
var Logger = require('pixl-logger');
var Tools = require('pixl-tools');
var Perf = require('pixl-perf');

var perf = new Perf();
perf.setScale( 1 ); // seconds
perf.begin();

// setup stdin / stdout streams 
process.stdin.setEncoding('utf8');
process.stdout.setEncoding('utf8');

console.warn("Printed this with console.warn, should go to stderr, and thus straight to our logfile.");
console.log("Printed this with console.log, should be ignored as not json, and also end up in our logfile.");

if (process.argv.length > 2) console.log("ARGV: " + JSON.stringify(process.argv));

var stream = new JSONStream( process.stdin, process.stdout );
stream.on('json', function(job) {
    var columns = ['hires_epoch', 'date', 'hostname', 'category', 'code', 'msg', 'data'];
    var logger = new Logger( job.log_file, columns );
    logger.set('hostname', job.hostname);
    logger.set('debugLevel', 9);
    
    logger.debug(1, "This is a test debug log entry");
    logger.debug(9, "Here is our job, delivered via JSONStream:", job);
    logger.debug(9, "The current date/time for our job is: " + (new Date(job.now * 1000)).toString() );
        
    var status = 'SUCCESS';
    var table = {
        title: "Sample Stats",
        header: [
            "Column 1", "Column 2", "Column 3", "Column 4", "Column 5"
        ],
        rows: [
            ["TEST ROW 1", "directing.com", "MaxEvents-ImpsUserHour-DMZ", 138, "0.0032%" ],
            ["TEST ROW 2", "hsd2.nm.comcast.net", "MaxEvents-ImpsUserHour-ILUA", 84, "0.0019%" ],
            ["TEST ROW 3", "grandnetworks.net", "InvalidIP-Basic", 20, "0.00046%" ],
            ["TEST ROW 4", "hsd6.mi.comcast.net", "MaxEvents-ImpsUserHour-NM", 19, "0.00044%" ],
            ["TEST ROW 5", "hsd6.nm.comcast.net", "InvalidCat-Domestic", 17, "0.00039%" ],
            ["TEST ROW 6", "cable.mindsprung.com", "InvalidDog-Exotic", 15, "0.00037%" ],
            ["TEST ROW 7", "cliento.mchsi.com", "MaxEvents-ClicksPer", 14, "0.00035%" ],
            ["TEST ROW 8", "rgv.res.com", "InvalidFrog-Croak", 14, "0.00030%" ],
            ["TEST ROW 9", "dsl.att.com", "Pizza-Hawaiian", 12, "0.00025%" ],
            ["TEST ROW 10", "favoriteisp.com", "Random-Data", 10, "0%" ]
        ],
        caption: "This is an example stats table you can generate from within your Plugin code."
    };
    var html = {
        title: `Sample ${status} Report`,
        content: "<pre>This is a sample text report you can generate from within your Plugin code.</pre>",
        caption: `This is a ${status} caption for the ${status} report.`
    };

        // now need to execute java command (check suggested code with run function)

    logger.debug(9, `Simulating a ${status} response`);
    stream.write({
        complete: 1,
        code: 0,
        description: `${status}`,
        perf: perf.summarize(),
        table: table,
        html: html
    });
    perf.end();
} );