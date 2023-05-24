package com.univalicc.android.crud.todos.write

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univalicc.android.crud.R
import com.univalicc.android.crud.datasource.local.DbTodoRepository
import com.univalicc.android.crud.datasource.local.Todo
import kotlinx.android.synthetic.main.activity_todo_create_edit.*
import android.widget.*

class TodoCreateEditActivity : AppCompatActivity() {

    private var todo: Todo? = null
    private var editMode: Boolean = false
    private var todoRepository: DbTodoRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_create_edit)

        //=============
        val containerTelefones: LinearLayout = findViewById(R.id.containerTelefones)
        val botaoAdicionar: Button = findViewById(R.id.botaoAdicionar)

        botaoAdicionar.setOnClickListener {
            adicionarTelefone(containerTelefones, "", "")
        }
        //=============

        val todoId = intent.getLongExtra("TODO_ID", -1)
        todoRepository = DbTodoRepository(this.applicationContext)

        if (todoId.toInt() != -1) {
            editMode = true

            todo = todoRepository!!.getById(todoId)

            eTxtNome.setText(todo!!.nome)

            //=====
            var telefones = todo!!.telefone
            //=====
            val telefoneParts = telefones?.split(";")
            if (telefoneParts != null) {
                for (part in telefoneParts) {
                    val keyValue = part.split(":")
                    if (keyValue.size == 2) {
                        val tipo = keyValue[0].trim()
                        val numero = keyValue[1].trim()
                        adicionarTelefone(containerTelefones, numero, tipo)
                    }
                }
            }
//            eTxtTelefone.setText(todo!!.telefone)
//            val tipo = todo!!.tipo;
//            if (tipo != null) {
//                definirItemSelecionado(eTxtTipo, tipo)
//            } else {
//                definirItemSelecionado(eTxtTipo, "Casa")
//            }

            //eTxtTipo.setText(todo!!.tipo)
            btnSaveTodo.text = "Update"
        } else {
            btnSaveTodo.text = "Criar Contato"
        }

    }

    fun adicionarTelefone(container: LinearLayout, numero: String, tipo: String) {
        val opcoesTiposTelefone = resources.getStringArray(R.array.opcoes_tipo_telefone)
        // Criação do EditText
        val editText = EditText(container.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.hint = "Telefone"
        editText.inputType = android.text.InputType.TYPE_CLASS_PHONE
        if (numero != "") {
            editText.setText(numero)
        }

        // Criação do Spinner
        val spinner = Spinner(container.context)
        spinner.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //=====================================
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, opcoesTiposTelefone)
            spinner.adapter = adapter
            definirItemSelecionado(spinner, tipo)
        }

        //=====================================

        // Criação do botão de remover
        val botaoRemover = Button(container.context)
        botaoRemover.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        botaoRemover.text = "Remover"

        // Adicionando os elementos ao contêiner
        container.addView(editText)
        container.addView(spinner)
        container.addView(botaoRemover)

        // Lógica para remover o elemento quando o botão for clicado
        botaoRemover.setOnClickListener {
            container.removeView(editText)
            container.removeView(spinner)
            container.removeView(botaoRemover)
        }
    }

    fun definirItemSelecionado(spinner: Spinner, itemSelecionado: String) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == itemSelecionado) {
                spinner.setSelection(i)
                break
            }
        }

    }
    fun obterItemSelecionado(spinner: Spinner): String {
        val posicaoSelecionada = spinner.selectedItemPosition
        val itemSelecionado = spinner.getItemAtPosition(posicaoSelecionada).toString()
        return itemSelecionado
    }

    fun saveTodo(view: View) {
        val nome = eTxtNome!!.text.toString()
        //val telefone = eTxtTelefone!!.text.toString()

        //========================
        var telefoneList = ""
        val containerTelefones: LinearLayout = findViewById(R.id.containerTelefones)
        for (i in 0 until containerTelefones.childCount step 3) {
            val editTextTelefone = containerTelefones.getChildAt(i) as EditText
            val spinnerTipo = containerTelefones.getChildAt(i + 1) as Spinner

            val telefone = editTextTelefone.text.toString()
            val tipo = spinnerTipo.selectedItem.toString()

            telefoneList += "$tipo: $telefone;"
        }
        //========================
        if (editMode) {

            todo!!.nome = nome
            todo!!.telefone = telefoneList
            //todo!!.tipo = tipo
            todoRepository!!.update(todo!!)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Updated!", Toast.LENGTH_LONG).show()
            finish()

        } else {

            val todo = Todo()
            todo.nome = nome
            todo!!.telefone = telefoneList

            todoRepository!!.insert(todo)
            Toast.makeText(this@TodoCreateEditActivity, "Todo Created!", Toast.LENGTH_LONG).show()

            intent.putExtra("todo", todo)
            setResult(RESULT_OK, intent)
            finish()

        }

    }
}
