package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepo = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepo.cities,
                        onAddCity = { cityRepo.addCity(it) },
                        onDelCity = { cityRepo.delCity(it) },
                        modifier = Modifier.padding (innerPadding)
                    )
                }
            }
        }
    }
}


class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
//        "Sydney", "Berlin", "New Delhi", "Manitoba", "Montreal", "Toronto",
//        "Calgary", "Banff", "Jasper", "Sherwood", "St. Albert", "Windsor", "Detroit"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun delCity(index: Int) {
        if (index < _cities.size) {
            _cities.removeAt(index)
        }
    }
}
@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDelCity: (Int) -> Unit,
    modifier: Modifier
) {
//    var delButtonClicked by remember { mutableStateOf(false) }
    var addButtonClicked by remember { mutableStateOf(false) }
//    var delCityInput by remember { mutableStateOf("") }
    var newCityName by remember { mutableStateOf("") }
//    var invalidInput by remember {mutableStateOf(false)}
    Column(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                addButtonClicked = true
            }
        ) {
           Text("Add City")
        }
//        Button(onClick = {
//            delButtonClicked = true
//        }){
//            Text("Delete City")
//        }
        if (addButtonClicked){
            AlertDialog(
                onDismissRequest = {
                    addButtonClicked = false
                    newCityName = ""
                },
                title = {
                    Text("Add new city")
                },
                text = {
                    OutlinedTextField(
                        value = newCityName,
                        onValueChange = {newCityName = it},
                        label = {
                            Text("Enter city name")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                dismissButton = {
                    TextButton(onClick = { addButtonClicked = false }) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newCityName.isNotBlank()){
                            onAddCity(newCityName.trim())
                            addButtonClicked = false
                            newCityName = ""
                        }
                    }) {
                        Text("Ok")
                    }
                }
            )
        }
//        if (delButtonClicked) {
//            AlertDialog(
//                onDismissRequest = {delButtonClicked = false},
//                title = {
//                    Text("Delete City")
//                },
//                text = {
//                    OutlinedTextField(
//                        value = delCityInput,
//                        onValueChange = {delCityInput = it},
//                        label = {Text("Enter city number")},
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                dismissButton = {
//                    TextButton(onClick = { delButtonClicked = false }) {
//                        Text("Cancel")
//                    }
//                },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//                            val conv = delCityInput.trim().toIntOrNull() ?: -1
//                            if ((conv > 0) and (conv <= cities.size)) {
//                                onDelCity(conv-1)
//                                delButtonClicked = false
//                                delCityInput = ""
//                            }
//                            else {
//                                invalidInput = true
//                            }
//                        }
//                    )
//                {
//                    Text("Ok")
//                }
//                    if (invalidInput) {
//                        AlertDialog(onDismissRequest = {invalidInput = false},
//                            title = {
//                                Text("Invalid input")
//                            },
//                            confirmButton = {
//                                TextButton(onClick = {invalidInput = false}) {
//                                    Text("Ok")
//                                }
//                            }
//                        )
//                    }
//                }
//            )
//        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(
                cities,
            ) {
                index, city -> CityRow(city = city, index+1, onDelCity = {onDelCity(it)})
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CityRow(city: String, index: Int, onDelCity: (Int) -> Unit) {
    var delCity by remember { mutableStateOf(false) }
    Button(onClick = {
        delCity = true
    }){
        Text(
            text = "$index. $city",
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        )
    }
    if (delCity) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Delete city?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onDelCity(index-1)
                    delCity = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    delCity = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
