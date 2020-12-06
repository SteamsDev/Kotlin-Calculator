
class lexParser {
    fun createTree (s: MutableList<String>): lexNode.exprNode<String> {
        var header = lexNode.exprNode<String>("")

        if (s[0] != "LPAREN") {
            var parentNode = lexNode.exprNode<String>(s[1])
            var valNode =
                when (s[0]) {
                    "PI" -> {
                        lexNode.numNode<String>(Math.PI.toString())
                    }
                    "E" -> {
                        lexNode.numNode<String>(Math.E.toString())
                    }
                    else -> {
                        lexNode.numNode<String>(s[0].substring(7, s[0].indexOf(")")))
                    }
                }
            header = parentNode
            header.setLhs(valNode)
        } else {
            var recursiveList = mutableListOf<String>()
            loop@ for (i in 1 until s.size) {
                if (s[i] != "RPAREN") {
                    recursiveList.add(s[i])
                } else {
                    break@loop
                }
            }
            for (k in 0 until recursiveList.size + 2) {
                s[k] = "RPAREN"
            }
            header = createTree(recursiveList)
            if (recursiveList.size + 2 < s.size) {
                var opNode = lexNode.exprNode<String> (s[recursiveList.size + 2])
                var valNode = lexNode.numNode<String> (s[recursiveList.size + 3].substring(7, s[recursiveList.size + 3].indexOf(")")))
                opNode.setLhs (header)
                opNode.setRhs (valNode)
                header.setPar (opNode)
                header = opNode
                s[recursiveList.size + 2] = "RPAREN"
                s[recursiveList.size + 3] = "RPAREN"
            }
        }
        return lexemeSorter (s, header)
    }

    fun lexemeSorter (s: MutableList<String>, h: lexNode.exprNode<String>): lexNode.exprNode<String> {
        var header = h
        var current = h
        for (i in 2 until s.size) {
            when (s[i]) {
                "PLUS" -> {
                    var newNode = lexNode.exprNode<String>(s[i])
                    newNode.setLhs(header)
                    header.setPar (newNode)
                    header = newNode
                    current = newNode
                }
                "MINUS" -> {
                    var newNode = lexNode.exprNode<String>(s[i])
                    newNode.setLhs(header)
                    header.setPar (newNode)
                    header = newNode
                    current = newNode
                }
                "TIMES" -> {
                    var newNode = lexNode.exprNode<String>(s[i])
                    if (current.value == "POWER" || current.value == "TIMES" || current.value == "DIVIDES") {
                        var parentNode = current.getPar ()
                        parentNode?.setRhs (newNode)
                        newNode.setLhs (current)
                        current.setPar (newNode)
                        if (parentNode != null) {
                            newNode.setPar (parentNode)
                        }
                    } else {
                        current.getRhs ()?.let { newNode.setLhs (it) }
                        current.setRhs (newNode)
                        newNode.setPar (current)
                    }
                    current = newNode
                    if (header.parent != null) {
                        header = current
                    }
                }
                "DIVIDES" -> {
                    var newNode = lexNode.exprNode<String>(s[i])
                    if (current.value == "POWER" || current.value == "TIMES" || current.value == "TIMES") {
                        var parentNode = current.getPar ()
                        parentNode?.setRhs (newNode)
                        newNode.setLhs (current)
                        current.setPar (newNode)
                        if (parentNode != null) {
                            newNode.setPar (parentNode)
                        }
                    } else {
                        current.getRhs ()?.let { newNode.setLhs (it) }
                        current.setRhs (newNode)
                        current.getRhs ()?.let { newNode.setLhs (it) }
                        current.setRhs (newNode)
                        newNode.setPar (current)
                    }
                    current = newNode
                    if (header.parent != null) {
                        header = current
                    }
                }
                "POWER" -> {
                    var newNode = lexNode.exprNode<String>(s[i])
                    if (current.value == "POWER") {
                        var parentNode = current.getPar ()
                        parentNode?.setRhs (newNode)
                        newNode.setLhs (current)
                        current.setPar (newNode)
                        if (parentNode != null) {
                            newNode.setPar (parentNode)
                        }
                    } else {
                        current.getRhs ()?.let { newNode.setLhs (it) }
                        current.setRhs (newNode)
                        newNode.setPar (current)
                    }
                    current = newNode
                    if (header.parent != null) {
                        header = current
                    }
                }
                "LPAREN" -> {
                    var recursiveList = mutableListOf<String>()
                    loop@ for (j in (i + 1) until s.size) {
                        if (s[j] != "RPAREN") {
                            recursiveList.add(s[j])
                        } else {
                            break@loop
                        }
                    }
                    for (k in 0 until recursiveList.size + 2) {
                        s[k + i] = "RPAREN"
                    }
                    var newNode = createTree(recursiveList)
                    current.setRhs(newNode)
                    newNode.setPar (current)
                }
                "RPAREN" -> {}
                "PI" -> {
                    var newNode = lexNode.numNode<String>(Math.PI.toString())
                    current.setRhs (newNode)
                }
                "E" -> {
                    var newNode = lexNode.numNode<String>(Math.E.toString())
                    current.setRhs (newNode)
                }
                else -> {
                    var newNode = lexNode.numNode<String>(s[i].substring(7, s[i].indexOf(")")))
                    current.setRhs (newNode)
                }
            }
        }
        return header
    }

    fun printEvaluatorOrder (s: lexNode.exprNode<String>) {
        print (s.getLhs ()?.value)
        print (s.value)
        print (s.getRhs ()?.value)
        /*if (s.getLhs () is lexNode.numNode<String>) {
            print (s.getLhs ()?.value + " ")
            print (s.value + " ")
            if (s.getRhs () is lexNode.numNode<String>) {
                print (s.getRhs ()?.value + " ")
            } else {
                printEvaluatorOrder (s.getRhs () as lexNode.exprNode<String>)
            }
        } else {
            printEvaluatorOrder (s.getLhs () as lexNode.exprNode<String>)
        }*/
    }
}
