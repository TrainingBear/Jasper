package me.jasper.jasperproject.JMinecraft.Item.Util

import me.jasper.jasperproject.JMinecraft.Item.JItem
import java.io.ObjectStreamClass
import java.io.Serializable

interface Factory : Serializable {
    fun create(): JItem

    fun finish(): JItem {
        val product = create()
        val serialVersionUID = ObjectStreamClass.lookup(this.javaClass).serialVersionUID
        product.version = serialVersionUID
        product.update()
        return product
    }
}
