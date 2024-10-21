
@file:Suppress("FunctionName")
package com.example.calculatorexercice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorExerciceUI(){

    // Variable that will store the number that's on display
    var displayNumber by remember { mutableStateOf("0") }
    // Variable that will store the operator of equation
    var operator by remember { mutableStateOf("") }
    // Variable that will store the first number of the equation
    var firstNumber by remember { mutableStateOf("") }

    //region Logic Functions

    // Function to add a number or a dot to the display
    fun GetNumber(number: String){
        if (displayNumber.length < 8) {
            if (displayNumber == "0"){
                if (number == "."){
                    // If the display is "0", replace it with the number or add a zero before the decimal point
                    displayNumber = "0."
                }
                else{
                    displayNumber = number
                }

            }
            else{
                // Add the number to the display, if it's not a decimal point that already exists
                if (number == "." && !displayNumber.contains(".")){
                    displayNumber += number
                }
                else if (number != "."){
                    displayNumber += number
                }

            }
        }
    }

    // Function to set the operator for the operation
    fun GetOperator(op: String){
        if (firstNumber.isEmpty()){
            firstNumber = displayNumber
            operator = op
            displayNumber = "0" // Reset the display to 0 after setting the operator
        }
    }

    // Function to calculate the result of the operation
    fun Result(){
        if (firstNumber.isNotEmpty() && operator.isNotEmpty()){
            val result = when (operator){
                "+" -> firstNumber.toDouble() + displayNumber.toDouble()
                "-" -> firstNumber.toDouble() - displayNumber.toDouble()
                "÷" -> firstNumber.toDouble() / displayNumber.toDouble()
                "×" -> firstNumber.toDouble() * displayNumber.toDouble()
                else -> 0.0
            }

            // Convert the result to an integer if there is no decimal part
            displayNumber = if (result % 1.0 == 0.0){
                result.toInt().toString()
            }
            else{
                result.toString()
            }
            operator = "" // Reset the operator after the calculation
            firstNumber = "" // Reset the first number

        }
    }

    //endregion

    //region Layout of the calculator

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayNumber,
                color = Color.White,
                fontSize = 80.sp,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.End
            )
        }

        // Modifier for the buttons
        val buttonModifier = Modifier
            .padding(8.dp)
            .size(80.dp)

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Buttons for the first row
                CalculatorButton(symbol = "AC", modifier = buttonModifier, backgroundColor = Color.Gray) {
                    displayNumber = "0"
                    operator = ""
                    firstNumber = ""
                }
                CalculatorButton(symbol = "+/-", modifier = buttonModifier, backgroundColor = Color.Gray) { /* Logic to be implemented */}
                CalculatorButton(symbol = "%", modifier = buttonModifier, backgroundColor = Color.Gray) { /* Logic to be implemented */}
                CalculatorButton(symbol = "÷", modifier = buttonModifier, backgroundColor = Color(0xFFFF9500)) {
                    GetOperator("÷")
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Buttons for the second row
                CalculatorButton(symbol = "7", modifier = buttonModifier) {
                    GetNumber("7")
                }
                CalculatorButton(symbol = "8", modifier = buttonModifier){
                    GetNumber("8")
                }
                CalculatorButton(symbol = "9", modifier = buttonModifier){
                    GetNumber("9")
                }
                CalculatorButton(symbol = "×", modifier = buttonModifier, backgroundColor = Color(0xFFFF9500)){
                    GetOperator("×")
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Buttons for the third row
                CalculatorButton(symbol = "4", modifier = buttonModifier){
                    GetNumber("4")
                }
                CalculatorButton(symbol = "5", modifier = buttonModifier){
                    GetNumber("5")
                }
                CalculatorButton(symbol = "6", modifier = buttonModifier){
                    GetNumber("6")
                }
                CalculatorButton(symbol = "-", modifier = buttonModifier, backgroundColor = Color(0xFFFF9500)){
                    GetOperator("-")
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Buttons for the fourth row
                CalculatorButton(symbol = "1", modifier = buttonModifier){
                    GetNumber("1")
                }
                CalculatorButton(symbol = "2", modifier = buttonModifier){
                    GetNumber("2")
                }
                CalculatorButton(symbol = "3", modifier = buttonModifier){
                    GetNumber("3")
                }
                CalculatorButton(symbol = "+", modifier = buttonModifier, backgroundColor = Color(0xFFFF9500)){
                    GetOperator("+")
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Buttons for the last row
                CalculatorButton(symbol = "0", modifier = Modifier
                    .padding(8.dp)
                    .width(180.dp)
                    .height(80.dp))
                CalculatorButton(symbol = ".", modifier = buttonModifier) {
                    // Add decimal point only if it does not already exist in the display
                    if (!displayNumber.contains(".")){
                        displayNumber += "."
                    }
                }
                CalculatorButton(symbol = "=", modifier = buttonModifier, backgroundColor = Color(0xFFFF9500)){
                    Result()
                }
            }
        }
    }

    //endregion

}

/**
 * A composable button used in the calculator UI, displaying a symbol
 * and performing an action when clicked.
 */
@Composable
fun CalculatorButton(symbol: String, modifier: Modifier = Modifier, backgroundColor: Color = Color.DarkGray, onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Function to preview the interface
@Preview (showBackground = true)
@Composable
fun PreviewCalculatorExercice(){
    CalculatorExerciceUI()
}


