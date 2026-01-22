package com.example.calculadora

object ExpressionEvaluator {
    private enum class TokenType {
        NUMBER,
        OPERATOR,
        LEFT_PAREN,
        RIGHT_PAREN
    }

    private data class Token(
        val type: TokenType,
        val text: String,
        val number: Double? = null
    )

    fun evaluate(expression: String, ansValue: Double): Double {
        val tokens = tokenize(expression, ansValue)
        val rpn = toRpn(tokens)
        return evaluateRpn(rpn)
    }

    private fun tokenize(expression: String, ansValue: Double): List<Token> {
        val tokens = mutableListOf<Token>()
        var index = 0
        var previousType: TokenType? = null

        while (index < expression.length) {
            val char = expression[index]
            when {
                char.isWhitespace() -> index++
                char.isDigit() || char == '.' -> {
                    val start = index
                    var hasDot = char == '.'
                    index++
                    while (index < expression.length) {
                        val next = expression[index]
                        if (next.isDigit()) {
                            index++
                        } else if (next == '.' && !hasDot) {
                            hasDot = true
                            index++
                        } else {
                            break
                        }
                    }
                    val numberText = expression.substring(start, index)
                    tokens.add(Token(TokenType.NUMBER, numberText, numberText.toDouble()))
                    previousType = TokenType.NUMBER
                }
                char.isLetter() -> {
                    val start = index
                    index++
                    while (index < expression.length && expression[index].isLetter()) {
                        index++
                    }
                    val word = expression.substring(start, index)
                    if (word.equals("Ans", ignoreCase = true)) {
                        tokens.add(Token(TokenType.NUMBER, word, ansValue))
                        previousType = TokenType.NUMBER
                    } else {
                        throw IllegalArgumentException("Unknown token: $word")
                    }
                }
                char == '(' -> {
                    tokens.add(Token(TokenType.LEFT_PAREN, "("))
                    previousType = TokenType.LEFT_PAREN
                    index++
                }
                char == ')' -> {
                    tokens.add(Token(TokenType.RIGHT_PAREN, ")"))
                    previousType = TokenType.RIGHT_PAREN
                    index++
                }
                isOperatorChar(char) -> {
                    val operator = normalizeOperator(char)
                    val isUnary = operator == "-" && (previousType == null || previousType == TokenType.OPERATOR || previousType == TokenType.LEFT_PAREN)
                    val opText = if (isUnary) "u-" else operator
                    tokens.add(Token(TokenType.OPERATOR, opText))
                    previousType = TokenType.OPERATOR
                    index++
                }
                else -> throw IllegalArgumentException("Invalid character: $char")
            }
        }
        return tokens
    }

    private fun toRpn(tokens: List<Token>): List<Token> {
        val output = mutableListOf<Token>()
        val operators = ArrayDeque<Token>()

        for (token in tokens) {
            when (token.type) {
                TokenType.NUMBER -> output.add(token)
                TokenType.OPERATOR -> {
                    while (operators.isNotEmpty()) {
                        val top = operators.last()
                        if (top.type != TokenType.OPERATOR) {
                            break
                        }
                        val tokenPrec = precedence(token.text)
                        val topPrec = precedence(top.text)
                        val isLeftAssoc = isLeftAssociative(token.text)
                        if ((isLeftAssoc && tokenPrec <= topPrec) || (!isLeftAssoc && tokenPrec < topPrec)) {
                            output.add(operators.removeLast())
                        } else {
                            break
                        }
                    }
                    operators.addLast(token)
                }
                TokenType.LEFT_PAREN -> operators.addLast(token)
                TokenType.RIGHT_PAREN -> {
                    while (operators.isNotEmpty() && operators.last().type != TokenType.LEFT_PAREN) {
                        output.add(operators.removeLast())
                    }
                    if (operators.isEmpty() || operators.last().type != TokenType.LEFT_PAREN) {
                        throw IllegalArgumentException("Mismatched parentheses")
                    }
                    operators.removeLast()
                }
            }
        }

        while (operators.isNotEmpty()) {
            val token = operators.removeLast()
            if (token.type == TokenType.LEFT_PAREN || token.type == TokenType.RIGHT_PAREN) {
                throw IllegalArgumentException("Mismatched parentheses")
            }
            output.add(token)
        }

        return output
    }

    private fun evaluateRpn(tokens: List<Token>): Double {
        val stack = ArrayDeque<Double>()

        for (token in tokens) {
            when (token.type) {
                TokenType.NUMBER -> stack.addLast(token.number ?: 0.0)
                TokenType.OPERATOR -> {
                    if (token.text == "u-") {
                        val value = popNumber(stack)
                        stack.addLast(-value)
                    } else {
                        val right = popNumber(stack)
                        val left = popNumber(stack)
                        val result = when (token.text) {
                            "+" -> left + right
                            "-" -> left - right
                            "*" -> left * right
                            "/" -> left / right
                            else -> throw IllegalArgumentException("Unknown operator")
                        }
                        stack.addLast(result)
                    }
                }
                else -> throw IllegalArgumentException("Unexpected token")
            }
        }

        return if (stack.isEmpty()) {
            throw IllegalArgumentException("Empty expression")
        } else {
            stack.removeLast()
        }
    }

    private fun precedence(operator: String): Int = when (operator) {
        "u-" -> 3
        "*", "/" -> 2
        "+", "-" -> 1
        else -> 0
    }

    private fun isLeftAssociative(operator: String): Boolean = operator != "u-"

    private fun isOperatorChar(char: Char): Boolean = char == '+' || char == '-' || char == '*' || char == '/' || char == '×' || char == '÷'

    private fun normalizeOperator(char: Char): String = when (char) {
        '×', '*' -> "*"
        '÷', '/' -> "/"
        else -> char.toString()
    }

    private fun popNumber(stack: ArrayDeque<Double>): Double {
        if (stack.isEmpty()) {
            throw IllegalArgumentException("Missing operand")
        }
        return stack.removeLast()
    }
}
