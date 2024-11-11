package com.example.objectstudent

import android.content.Context
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
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

data class Student(val name: String, val observation: String, val anoNascimento:Int,val nacionalidade:String)

@Database(entities=[Student::class], version=1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object{
        @Volatile private var INSTANCE: AppDatabase? =null

        fun getDatabase(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,"app_database")

                    .build()
                INSTANCE=instance
                instance
            }
        }
    }
}

@Dao
interface StudentDao{
    @Insert
    suspend fun insert(student: Student)

    @Query("SELECT * FROM student_table WHERE name = :name")
    fun getByName(name: String): Flow<Student>

    @Delete
    suspend fun delete(student: Student)
}

object StudentList {
    val students = mutableListOf<Student>()
}

@Composable
fun DetailsScreen(navController: NavController, studentIndex: Int) {
    val student = StudentList.students[studentIndex]
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nome do Aluno: ${student.name}", style = MaterialTheme.typography.headlineSmall)
        Text("Observações: ${student.observation}", style = MaterialTheme.typography.bodyMedium)
        Text("Ano de Nascimento: ${student.anoNascimento}", style = MaterialTheme.typography.bodyMedium)
        Text("Nacionalidade: ${student.nacionalidade}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}

@Composable
fun AddStudentScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var observation by remember { mutableStateOf("") }
    var anoNascimento by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Aluno") }
        )
        OutlinedTextField(
            value = observation,
            onValueChange = { observation = it },
            label = { Text("Observação acerca do aluno") }
        )
        OutlinedTextField(
            value = anoNascimento,
            onValueChange = { anoNascimento = it },
            label = { Text("Ano de Nascimento") }
        )
        OutlinedTextField(
            value = nacionalidade,
            onValueChange = { nacionalidade = it },
            label = { Text("Nacionalidade") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val yearAnoNascimento = anoNascimento.toIntOrNull() ?: 0 // Converte para Int ou usa 0 se falhar

            StudentList.students.add(Student(name, observation, yearAnoNascimento, nacionalidade))
            navController.popBackStack() // Volta para a tela de lista de alunos
        }) {
            Text("Adicionar Aluno")
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "studentList") {
        composable("studentList") { StudentListScreen(navController) }
        composable("details/{studentIndex}") { backStackEntry ->
            val studentIndex = backStackEntry.arguments?.getString("studentIndex")?.toInt()
            studentIndex?.let { DetailsScreen(navController, it) }
        }
        composable("addAluno") { AddStudentScreen(navController) }
    }
}

@Composable
fun StudentListScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addAluno") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Aluno")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(StudentList.students.size) { index ->
                val student = StudentList.students[index]
                Text(
                    text = student.name,
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
