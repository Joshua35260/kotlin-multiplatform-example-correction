package example.imageviewer.service

import example.imageviewer.model.PictureData
import example.imageviewer.model.PictureData.Pokemon
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class PokemonService {
    private val url = "https://api.pokemontcg.io/v1/cards"
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    @Serializable
    private data class PokemonServiceResultItem(
        val id: String, val name: String, val imageUrl: String,
        val supertype: String, val types: List<String> = listOf()
    )

    @Serializable
    private data class PokemonServiceResult(val cards: List<PokemonServiceResultItem>)

    suspend fun getAll(): List<Pokemon> {
        println("PokemonService: will do get on " + url)
        var result: PokemonServiceResult = httpClient.get(url).body()

        return result.cards.map { item -> Pokemon(
            item.id, item.name, item.supertype + " of type " + item.types.joinToString(","), item.imageUrl)
        };
    }
}