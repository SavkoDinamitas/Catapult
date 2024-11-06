package salko.dinamitas.catalist.networking.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class BraindeadBoolSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "BraindeadBoolSerializer",
        PrimitiveKind.INT
    )

    override fun deserialize(decoder: Decoder): Boolean = decoder.decodeInt() != 0

    override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeInt(
        if (value) 1 else 0
    )
}

class RmaODTSerializer : KSerializer<OffsetDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "FinlabRmaODTSerializer",
        PrimitiveKind.LONG
    )

    override fun deserialize(decoder: Decoder): OffsetDateTime =
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(decoder.decodeLong()), ZoneOffset.UTC)

    override fun serialize(encoder: Encoder, value: OffsetDateTime) =
        encoder.encodeLong(value.toEpochSecond())
}