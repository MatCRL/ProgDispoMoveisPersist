package com.univalicc.android.crud.todos.write

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univalicc.android.crud.R
import com.univalicc.android.crud.datasource.local.DbTodoRepository
import com.univalicc.android.crud.datasource.local.Todo
import kotlinx.android.synthetic.main.activity_todo_create_edit.*

class TodoCreateEditActivity : AppCompatActivity() {

    private var todo: Todo? = null
    private var editMode: Boolean = false
    private var todoRepository: DbTodoRepository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_create_edit)

        val todoId = intent.getLongExtra("TODO_ID", -1)
        todoRepository = DbTodoRepository(this.applicationContext)

        if (todoId.toInt() != -1) {
            editMode = true

            todo = todoRepository!!.getById(todoId)

            eTxtNome.setText(todo!!.nome)
            eTxtTelefone.setText(todo!!.telefone)
            eTxtTipo.setText(todo!!.tipo)
            btnSaveTodo.text = "Update"
        } else {
            btnSaveTodo.text = "Criar Contato"
        }
    }

    fun saveTodo(view: View) {
        val nome = eTxtNome!!.text.toString()
        val telefone = eTxtTelefone!!.text.toString()
        val tipo = eTxtTipo!!.text.toString()

        if (editMode) {

            todo!!.nome = nome
            todo!!.telefone = telefone
            todo!!.tipo = tipo
            todoRepository!!.update(todo!!)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Updated!", Toast.LENGTH_LONG).show()
            finish()

        } else {

            val todo = Todo()
            todo.nome = nome
            todo.telefone = telefone
            todo.tipo = tipo

            todoRepository!!.insert(todo)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Created!", Toast.LENGTH_LONG).show()

            intent.putExtra("todo", todo)
            setResult(RESULT_OK, intent)
            finish()

        }


    }
}
