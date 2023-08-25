package com.example.calculator.common

import net.objecthunter.exp4j.ExpressionBuilder

class Expression{
    var first: String = ""
    var operation: Operations = Operations.EMPTY
    var second: String = ""

    fun addNewDigit(digit: Int){
        if (first.isEmpty() || operation == Operations.EMPTY){
            first += digit
        }else{
            second += digit
        }
    }

    fun addDot(){
        if (first.isEmpty() || operation == Operations.EMPTY){
            first += "."
        }else{
            second += "."
        }
    }

    fun remove(){
        if (operation == Operations.EMPTY && second.isEmpty() && first.isNotEmpty()){
            first = first.substring(0, first.lastIndex)
        }else if (second.isNotEmpty()){
            second = second.substring(0, second.lastIndex)
        }else{
            operation = Operations.EMPTY
        }
    }

    fun setOperation(operationValue: String){
        this.operation = Operations.getOperationByValue(operationValue)
    }

    fun clear(){
        first = ""
        second = ""
        operation = Operations.EMPTY
    }

    fun calc(): Double {
        val match = ExpressionBuilder(this.toString()).build()!!
        return match.evaluate()
    }

    fun parse(expressionString: String) {
        if (expressionString.isBlank())
            return
        operation = Operations.getOperationByValue(
            Operations.getValuesAllOperations().singleOrNull { it in expressionString }
        )
        if (operation == Operations.EMPTY){
            first = expressionString.toInt().toString()
        }else{
            val parts = expressionString.split(operation.value)
            first = parts[0]
            second = parts[1]
        }
    }

    override fun toString(): String {
        return "$first$operation$second"
    }
}
