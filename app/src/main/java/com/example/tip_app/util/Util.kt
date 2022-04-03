package com.example.tip_app.util

 fun calculateTotalTip(totalBillState: Double, tipPercentage: Int): Double {
    return if(totalBillState > 1 && totalBillState.toString().isNotEmpty()){
        (totalBillState * tipPercentage) / 100
    } else 0.0

}