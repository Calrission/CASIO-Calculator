package com.example.calculator.common

enum class Operations(var value: String) {
    PLUS("+"), MINUS("-"), MULTIPLICATION("*"),
    DIVISION("/"), MOD("%"), EMPTY("");

    override fun toString(): String {
        return value
    }

    companion object {
        fun getValuesAllOperations(): List<String> = Operations.values().filter { it != EMPTY }.map { it.value }

        fun getOperationByValue(operation: String?): Operations {
            if (operation.isNullOrBlank())
                return EMPTY
            return Operations.values().single { it.value == operation }
        }
    }
}
