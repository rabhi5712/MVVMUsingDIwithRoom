package com.example.mvvmroom.utils

import com.example.mvvmroom.room.entity.DeviceData
import com.google.gson.*
import java.lang.reflect.Type

class DeviceDataDeserializer : JsonDeserializer<DeviceData> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DeviceData {
        val jsonObject = json?.asJsonObject
        return DeviceData(
            color = getJsonValue(jsonObject, "color", "Color"),
            capacity = getJsonValue(jsonObject, "capacity", "Capacity", "capacity GB"),
            capacityGB = getJsonValue(jsonObject,  "capacity GB")?.toInt(),
            price = getJsonValue(jsonObject, "price", "Price")?.toDouble(),
            year = getJsonValue(jsonObject, "year", "Year")?.toInt(),
            cpuModel = getJsonValue(jsonObject, "CPU model", "CPU Model"),
            hardDiskSize = getJsonValue(jsonObject, "Hard disk size", "Hard Disk Size"),
            strapColour = getJsonValue(jsonObject, "Strap Colour", "strap colour"),
            caseSize = getJsonValue(jsonObject, "Case Size", "case size"),
            description = getJsonValue(jsonObject, "description", "Description"),
            screenSize = getJsonValue(jsonObject, "Screen Size", "screen size")?.toDouble(),
            generation = getJsonValue(jsonObject, "generation", "Generation")
        )
    }

    private fun getJsonValue(jsonObject: JsonObject?, vararg keys: String): String? {
        keys.forEach { key ->
            if (jsonObject?.has(key) == true) {
                return jsonObject.get(key).asString
            }
        }
        return null
    }
}
