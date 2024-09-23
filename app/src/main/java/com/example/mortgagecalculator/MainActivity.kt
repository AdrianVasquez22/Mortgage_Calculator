package com.example.mortgagecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.ui.theme.MortgageCalculatorTheme
import java.text.NumberFormat
import kotlin.math.pow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MortgageCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MortgageCalculatorLayout()
                }
            }
        }
    }
}


@Composable
fun MortgageCalculatorLayout() {
    var loanAmountInput by remember { mutableStateOf("") }
    var interestRateInput by remember { mutableStateOf("") }
    var numberOfYearsInput by remember { mutableStateOf("") }
    var monthlyPayment by remember { mutableStateOf("") }

    val loanAmount = loanAmountInput.toIntOrNull() ?: 0
    val interestRate = interestRateInput.toDoubleOrNull()?.div(100)?.div(12) ?: 0.0
    val numberOfYears = numberOfYearsInput.toIntOrNull()?.times(12) ?: 0

    monthlyPayment = calculateMortgage(loanAmount, interestRate, numberOfYears)

    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.mortgage_calculator),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        MortgageInputField(
            label = R.string.loan_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = loanAmountInput,
            onValueChange = { loanAmountInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        MortgageInputField(
            label = R.string.interest_rate,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = interestRateInput,
            onValueChange = { interestRateInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        MortgageInputField(
            label = R.string.number_of_years,
            leadingIcon = R.drawable.calendar,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = numberOfYearsInput,
            onValueChange = { numberOfYearsInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.monthly_payment, monthlyPayment),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}


@Composable
fun MortgageInputField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}


fun calculateMortgage(p: Int, r: Double, n: Int): String {
    var mortgage = p * ( (r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1) )
    if (mortgage.isNaN() || mortgage.isInfinite()) mortgage = 0.0
    return NumberFormat.getCurrencyInstance().format(mortgage)
}


@Preview(showBackground = true)
@Composable
fun MortgageCalculatorPreview() {
    MortgageCalculatorTheme {
        MortgageCalculatorLayout()
    }
}


