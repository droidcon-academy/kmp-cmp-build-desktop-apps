package com.droidcon.notedock.api

import com.droidcon.notedock.model.Joke
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class JokeApi {
    private val client = HttpClient {
        install(ContentNegotiation.Plugin) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val baseUrl = "https://official-joke-api.appspot.com"

    suspend fun getRandomJoke(): Joke? {
        return try {
            client.get("$baseUrl/random_joke").body<Joke>()
        } catch (e: Exception) {
            println("Error fetching joke: ${e.message}")
            null
        }
    }

    fun close() {
        client.close()
    }
}
