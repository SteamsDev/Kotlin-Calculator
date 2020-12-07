class lexAnalyzer {

    fun fillLexemeList (str: String): List<String> {
        var newLexemeList = mutableListOf<String> ()
        var intString = ""
        for (i in 0 until str.length) {
            if ((48 <= str [i].toInt () && str [i].toInt () <= 57) || str[i].toInt() == 46) {
                intString += str[i]
            } else {
                val intVal = lexemeChecker(intString)
                val newVal = lexemeChecker(str[i].toString())
                if (intVal != "") {
                    newLexemeList.add (intVal)
                }
                newLexemeList.add (newVal)
                intString = ""
            }
        }
        val intVal = lexemeChecker (intString)
        if (intVal != "") {
            newLexemeList.add (intVal)
        }

        return newLexemeList
    }

    fun lexemeChecker (stringer: String): String {
        when (stringer) {
            "+" -> return "PLUS"
            "-" -> return "MINUS"
            "*" -> return "TIMES"
            "/" -> return "DIVIDES"
            "^" -> return "POWER"
            "(" -> return "LPAREN"
            ")" -> return "RPAREN"
            "p" -> return "PI"
            "e" -> return "E"
            "" -> return ""
        }

        if (stringer.toDoubleOrNull () != null) {
            val num = stringer.toDouble ()
            return "NUMBER(" + num + ")"
        }

        throw IllegalArgumentException ("Invalid syntax: $stringer is not a valid lexeme")
    }

    fun lexicalAnalysis (lexemeList: List <String>) {
        var lParenCount = 0
        var rParenCount = 0

        printList (lexemeList)

        if (!(lexemeList.first ().startsWith("NUM")) && lexemeList.first () != "LPAREN" && lexemeList.first () != "PI"
            && lexemeList.first () != "E") {
            throw ArithmeticException ("Invalid syntax: cannot have an operator or rparen at the beginning of the" +
                    " expression")
        }

        if (!(lexemeList.last ().startsWith("NUM")) && lexemeList.last () != "RPAREN" && lexemeList.last () != "PI"
            && lexemeList.last () != "E") {
            throw ArithmeticException ("Invalid syntax: cannot have an operator or lparen at the end of the expression")
        }

        if (lexemeList.last () == "RPAREN") {
            rParenCount++
        }

        for (i in 0 until lexemeList.size - 1) {
            when (lexemeList [i]) {
                "PLUS", "MINUS", "TIMES", "POWER" ->
                    if (!lexemeList [i + 1].startsWith("NUM") && lexemeList [i + 1] != "LPAREN"
                        && lexemeList [i + 1] != "PI" && lexemeList [i + 1] != "E") {
                        throw ArithmeticException ("Invalid syntax: operator must be followed by a number or lparen")
                    }
                "DIVIDES" ->
                    if (!lexemeList [i + 1].startsWith("NUM") && lexemeList [i + 1] != "LPAREN"
                        && lexemeList [i + 1] != "PI" && lexemeList [i + 1] != "E") {
                        throw ArithmeticException ("Invalid syntax: operator must be followed by a number or lparen")
                    } else if (lexemeList[i + 1] == "NUMBER(0.0)") {
                        throw ArithmeticException ("Invalid syntax: cannot divide by zero")
                    }
                "LPAREN" ->
                    if  ((!lexemeList [i + 1].startsWith("NUM") && lexemeList [i + 1] != "PI"
                                && lexemeList [i + 1] != "E" && lexemeList [i + 1] != "LPAREN")
                        || lexemeList [i + 1] == "RPAREN") {
                        throw ArithmeticException ("Invalid syntax: lparen must be followed by a numeric  value or" +
                                "another lparen")
                    } else if ((lexemeList [i + 1].startsWith("NUM") || lexemeList [i + 1] != "E"
                                || lexemeList [i + 1] == "E" ) && lexemeList [i + 2] == "RPAREN"){
                        throw ArithmeticException ("Invalid syntax: nesting a single number in parentheses is obsolete")
                    } else {
                        lParenCount++
                    }
                "RPAREN" -> if (lexemeList [i + 1].startsWith("NUM") || lexemeList [i + 1] == "PI"
                    || lexemeList [i + 1] == "E" || lexemeList [i + 1] == "LPAREN") {
                    throw ArithmeticException ("Invalid syntax: rparen must be followed by an operator or another " +
                            "rparen")
                } else {
                    rParenCount++
                }
                "PI", "E" ->
                    if (lexemeList [i + 1].startsWith ("NUM") || lexemeList [i + 1] == "PI"
                        || lexemeList [i + 1] == "E" || lexemeList [i + 1] == "LPAREN") {
                        throw ArithmeticException ("Invalid syntax: numeric elements must be followed " +
                                "with an operator or rparen")
                    }
                else ->
                    if (lexemeList [i + 1].startsWith ("NUM") || lexemeList [i + 1] == "PI"
                        || lexemeList [i + 1] == "E" || lexemeList [i + 1] == "LPAREN") {
                        throw ArithmeticException("Invalid syntax: numeric elements must be followed " +
                                "with an operator or rparen")
                    }
            }
        }

        if (lParenCount != rParenCount) {
            throw ArithmeticException ("Invalid syntax: parentheses do not match")
        }
    }

    fun inputStringModifier (stringer: String): String {
        var str = stringer;
        str = str.replace (" ", "")
        str = str.replace ("pi", "p")
        return str
    }

    fun printList (list : List <String>) {
        for (i in 0 until list.size) {
            print(list[i] + " ")
        }
        println()
    }
}
