package com.example.listcity

import androidx.compose.material3.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listcity.ui.theme.ListCityTheme

// "->" This marks output return
//  "{}" This marks a lamada function (Single use function)

// Compose: This is reusable UI function
// Text("") <- This will display text on the screen
// Bottom response through onClick
// Modifier controls size, padding and placement
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Create the repository
        val citiesRepository = CityRepository()

        // SetContent ?
        setContent {
            //ListCity Theme
            ListCityTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    CityListScreen(
                        cities = citiesRepository.cities,
                        onAddCity = { citiesRepository.addCity(it) },
                        onDeleteCity = { citiesRepository.removeCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/*
    To View UI Composable code
    @Preview On top of your function.
 */

/*
Equivalent
Modifier
    .padding(16.dp)
    .background(Color.Red)
vs:
Modifier.padding(16.dp)
Modifier.background(Color.Red)
* */


@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit = {},
    onDeleteCity: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {

        OutlinedTextField(
            value = newCityName,
            onValueChange = { newCityName = it },
            label = { Text("City name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    var alreadyExists = false
                    for (existingCity in cities) {
                        if (existingCity.equals(newCityName, ignoreCase = true)) {
                            alreadyExists = true
                        }
                    }

                    if (newCityName.isNotBlank() && !alreadyExists) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Confirm")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val cityToDelete = selectedCity
                    if (cityToDelete != null) {
                        onDeleteCity(cityToDelete)
                        selectedCity = null
                    }
                },
                enabled = selectedCity != null
            ) {
                Text("DELETE CITY")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    var alreadyExists = false
                    for (existingCity in cities) {
                        if (existingCity.equals(newCityName, ignoreCase = true)) {
                            alreadyExists = true
                        }
                    }

                    if (newCityName.isNotBlank() && !alreadyExists) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            // This is a function that
            // takes two parameters. List and then a lamada function to look at what
            // going to print.
            // items(cities) { city -> CityRow(city = city) }
            //   ↑      ↑        ↑            ↑
            //   |      |        |            |
            //function  list   loop var    what to do
            //  call   to loop  (like the   with each
            //         through  "city" in   element
            //                  a for loop)
            items(cities) { city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        selectedCity = if (selectedCity == city) null else city
                    }
                )
            }
        }
    }
}

@Composable
fun CityRow(city: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .padding(
                horizontal = 18.dp,
                vertical = 14.dp
            )
    )
}
class CityRepository {

    // Keep mutable app data private so other classes cannot change it directly
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    // Get a read-only list for the UI to display
    val cities: List<String>
        get() = _cities

    /*
        Purpose:    To remove a specific City from the list
        Parameters: String: A city string of desired string to remove
        Return:     Unit: This would be similar to void

     */
    fun removeCity(city: String ){
        _cities.remove(city)

    }

    fun addCity(city: String) {
        _cities.add(city)
    }
}




@Preview(showBackground = true)
@Composable
fun CityListPreview() {
    ListCityTheme {
        CityListScreen(
            cities = listOf(
                "Edmonton",
                "Vancouver",
                "Tokyo",
                "Fakez"
            )
        )
    }
}