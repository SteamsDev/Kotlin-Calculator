import lexAnalyzer;
import lexParser;
import expEvaluator;

fun main () {
    var exp = expEvaluator();
    var tree = lexParser();
    var analyzer = lexAnalyzer();

    var continueExpr = "1"
    while (continueExpr != "0") {
        try {
            print("Please enter a mathematical equation: ")
            var str = readLine().toString()

            str = analyzer.inputStringModifier(str)
            var lexemeList = analyzer.fillLexemeList(str)
            analyzer.lexicalAnalysis(lexemeList)

            var lexTree = tree.lexemeTree(lexemeList as MutableList<String>)
            println(exp.evaluate(lexTree))

            print("Continue? (Enter 0 to stop): ")
            continueExpr = readLine().toString()
        }
        catch (e : NoSuchElementException) {
            println("No equation detected. Please specify a mathematical equation")
        }
    }

    println("Thank you!")
}