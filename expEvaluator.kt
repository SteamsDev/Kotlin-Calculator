import kotlin.math.pow

class expEvaluator {
    fun evaluate(tree: lexNode<String>) : Float {
        if (tree is lexNode.exprNode<String>) {
            var treeLeft = tree.getLhs()
            var treeRight = tree.getRhs()
            when (tree.value) {
                "PLUS" -> return evaluate(treeLeft as lexNode<String>) + evaluate(treeRight as lexNode<String>)
                "MINUS" -> return evaluate(treeLeft as lexNode<String>) - evaluate(treeRight as lexNode<String>)
                "TIMES" -> return evaluate(treeLeft as lexNode<String>) * evaluate(treeRight as lexNode<String>)
                "DIVIDES" -> return evaluate(treeLeft as lexNode<String>) / evaluate(treeRight as lexNode<String>)
                "POWER" -> return evaluate(treeLeft as lexNode<String>).pow(evaluate(treeRight as lexNode<String>))
            }
        }
        return tree.value.toFloat()
    }
}