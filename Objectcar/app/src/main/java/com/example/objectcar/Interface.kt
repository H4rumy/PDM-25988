package com.example.objectcar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.objectcar.ui.theme.ObjectCarTheme

data class Car(val name: String, val description: String, val price:Double,val year:Int,val color:String)

object CarList {
    val cars = mutableListOf<Car>()
}

@Composable
fun DetailsScreen(navController: NavController, carIndex: Int) {
    val car = CarList.cars[carIndex]
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nome do Carro: ${car.name}", style = MaterialTheme.typography.headlineSmall)
        Text("Descrição: ${car.description}", style = MaterialTheme.typography.bodyMedium)
        Text("Preço: ${car.price}", style = MaterialTheme.typography.bodyMedium)
        Text("Ano: ${car.year}", style = MaterialTheme.typography.bodyMedium)
        Text("Color: ${car.color}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}

@Composable
fun AddCarScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Carro") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Preço") }
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Ano") }
        )
        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Cor") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val priceValue = price.toDoubleOrNull() ?: 0.0 // Converte para Double ou usa 0.0 se falhar
            val yearValue = year.toIntOrNull() ?: 0 // Converte para Int ou usa 0 se falhar

            CarList.cars.add(Car(name, description, priceValue, yearValue, color))
            navController.popBackStack() // Volta para a tela de lista de carros
        }) {
            Text("Adicionar Carro")
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "carList") {
        composable("carList") { CarListScreen(navController) }
        composable("details/{carIndex}") { backStackEntry ->
            val carIndex = backStackEntry.arguments?.getString("carIndex")?.toInt()
            carIndex?.let { DetailsScreen(navController, it) }
        }
        composable("addCar") { AddCarScreen(navController) }
    }
}

@Composable
fun CarListScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addCar") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Car")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(CarList.cars.size) { index ->
                val car = CarList.cars[index]
                Text(
                    text = car.name,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("details/$index")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewObjectCarApp() {
    ObjectCarTheme {
        SetupNavGraph(navController = rememberNavController())
    }
}
