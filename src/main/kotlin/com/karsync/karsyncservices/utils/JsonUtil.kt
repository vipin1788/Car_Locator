package com.karsync.karsyncservices.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.karsync.karsyncservices.exception.InvalidRequestException

import java.io.IOException

object JsonUtil {

    fun toJson(`object`: Any): String {
        val objectMapper = ObjectMapper()
        try {
            return objectMapper.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            throw InvalidRequestException(e.message!!)
        }

    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        val objectMapper = ObjectMapper()
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            return objectMapper.readValue(json, clazz)
        } catch (e: IOException) {
            throw InvalidRequestException(e.message!!)
        }

    }
}
