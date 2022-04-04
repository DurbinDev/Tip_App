package com.example.tip_app

import android.os.Bundle
import android.util.Log
import android.util.Range
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tip_app.components.InputField
import com.example.tip_app.ui.theme.Tip_AppTheme
import com.example.tip_app.util.calculateTotalPerPerson
import com.example.tip_app.util.calculateTotalTip
import com.example.tip_app.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tip_AppTheme {
                MyApp {
                    //TopHeader()
                    MainContent()

            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    content()
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(20.dp))),
        color = Color(0xFF48a850)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                "Total Per Person",
                style = MaterialTheme.typography.h5
            )
            Text(
                "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){

    val splitByState = remember {
        mutableStateOf(1)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    BillForm(
        splitByState = splitByState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState)
        {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier:
             Modifier = Modifier,
             splitByState: MutableState<Int>,
             tipAmountState: MutableState<Double>,
             totalPerPersonState: MutableState<Double>,
            onValChange: (String) -> Unit = {}
    ){

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val splitRange = Range(1,20)

    Column {
        TopHeader(totalPerPersonState.value)

        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(1.dp, color = Color(0xFF48a850))
        ){
            Column(
                modifier = Modifier
                    .padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Amount",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions{
                        if(!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())

                        keyboardController?.hide()
                    })
                if(validState){
                    Row(modifier = Modifier
                        .padding(4.dp),
                        horizontalArrangement = Arrangement.Start) {
                        Text("Split",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(120.dp))
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value =
                                    if (splitByState.value > 1 ) splitByState.value - 1
                                    else 1

                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),splitByState.value, tipPercentage)
                                })

                            Text(
                                text = "${splitByState.value}",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp))

                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                        if (splitByState.value < splitRange.upper){
                                            splitByState.value += 1

                                            totalPerPersonState.value = calculateTotalPerPerson(
                                                totalBillState.value.toDouble(),splitByState.value, tipPercentage)
                                        }

                                })
                        }
                    }
                    //Tip Row
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .padding(vertical = 12.dp)){
                        Text(
                            text = "Tip",
                            modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(200.dp))

                        Text(
                            text = "$ ${tipAmountState.value}",
                            modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "$tipPercentage%")

                        Spacer(modifier = Modifier.height(14.dp))

                        //Slider
                        Slider(
                            value = sliderPositionState.value,
                            onValueChange = { newVal ->
                                sliderPositionState.value = newVal
                                        tipAmountState.value =
                                            calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)

                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),splitByState.value, tipPercentage)



                                            },
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp))
                    }
                }else{
                    Box() {

                    }
                }
            }
        }
    }
}




    //@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        Tip_AppTheme {
            MyApp {
                Text("Hello")
            }
        }
    }
}