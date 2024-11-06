package salko.dinamitas.catalist.user

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import salko.dinamitas.catalist.networking.serialization.AppJson
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class UserSerialization: Serializer<CatalistConfig> {
    private val json: Json = AppJson
    override val defaultValue = CatalistConfig(user = null)

    override suspend fun readFrom(input: InputStream): CatalistConfig {
        val text = String(input.readBytes(), StandardCharsets.UTF_8)
        return try {
            json.decodeFromString(text)
        } catch (error: SerializationException){
            Log.w("serialization", text)
            throw CorruptionException("Unable to desrialize decrypted value.", error)
        }catch (error: IllegalArgumentException){
            Log.w("serialization", text)
            throw CorruptionException("Unable to desrialize decrypted value.", error)
        }
    }

    override suspend fun writeTo(t: CatalistConfig, output: OutputStream) {
        val text = json.encodeToString(t)
        withContext(Dispatchers.IO){
            output.write(text.toByteArray())
        }
    }
}