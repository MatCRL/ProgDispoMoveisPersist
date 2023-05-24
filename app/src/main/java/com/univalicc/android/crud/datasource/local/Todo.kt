package com.univalicc.android.crud.datasource.local

import android.os.Parcel
import android.os.Parcelable

class Todo : Parcelable {

    var id: Long? = null

    var nome: String? = null

    var telefone: String? = null

    var tipo: String? = null

    var isCompleted: Boolean = false

    var createdAt: Long? = null

    var updatedAt: Long? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        nome = parcel.readString()
        telefone = parcel.readString()
        tipo = parcel.readString()
        isCompleted = parcel.readByte() != 0.toByte()
        createdAt = parcel.readValue(Long::class.java.classLoader) as? Long
        updatedAt = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    constructor() {}

    constructor(
        id: Long,
        nome: String,
        telefone: String,
        tipo: String,
        completed: Boolean,
        createdAt: Long,
        updatedAt: Long
    ) {
        this.id = id
        this.nome = nome
        this.telefone = telefone
        this.tipo = tipo
        this.isCompleted = completed
        this.createdAt = createdAt
        this.updatedAt = updatedAt

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nome)
        parcel.writeString(telefone)
        parcel.writeString(tipo)
        parcel.writeByte(if (isCompleted) 1 else 0)
        parcel.writeValue(createdAt)
        parcel.writeValue(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}
