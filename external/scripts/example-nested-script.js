include("utils.js");
include("config.js");

var greeting = util.sayHello("Mario");
var sum = message;
var randomQuote = util.getRandomQuote();
var quote = randomQuote.getString("content");
var author = randomQuote.getString("author");

"\n\n\n\n Greeting: " +
  greeting +
  "\n Sum: " +
  sum +
  "\n App Name: " +
  Config.appName +
  "\n Version: " +
  Config.version +
  "\n\n\n Author: " +
  author +
  "\n\n Quote: " +
  quote +
  "\n\n\n\n";
