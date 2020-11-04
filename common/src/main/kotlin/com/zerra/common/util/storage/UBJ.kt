package com.zerra.common.util.storage

import bvanseg.kotlincommons.any.getLogger
import com.devsmart.ubjson.*
import com.zerra.common.util.toArray
import org.joml.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.util.*


/**
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class UBJ(initUBO: UBObject? = null) {

    companion object {
        val logger = getLogger()
    }

    private val ubo: UBObject = initUBO ?: UBValueFactory.createObject()

    val entries: Set<Map.Entry<String, UBValue>>
        get() = ubo.entries

    val keys: Set<String>
        get() = ubo.keys

    val values: Collection<UBValue>
        get() = ubo.values

    operator fun set(key: String, value: Any?) {
        if (value == null) {
            ubo[key] = UBValueFactory.createNull()
            return
        }

        ubo[key] = when(value::class) {
            Char::class -> UBValueFactory.createChar(value as Char)
            Boolean::class -> UBValueFactory.createBool(value as Boolean)
            Byte::class,
            Short::class,
            Int::class,
            Long::class -> UBValueFactory.createInt(value as Long)
            UByte::class -> UBValueFactory.createInt((value as UByte).toLong())
            UShort::class -> UBValueFactory.createInt((value as UShort).toLong())
            UInt::class -> UBValueFactory.createInt((value as UInt).toLong())
            ULong::class -> UBValueFactory.createInt((value as ULong).toLong())
            Float::class -> UBValueFactory.createFloat32(value as Float)
            Double::class -> UBValueFactory.createFloat64(value as Double)
            String::class -> UBValueFactory.createString(value as String)

            BooleanArray::class -> UBValueFactory.createArray(value as BooleanArray)
            ByteArray::class -> UBValueFactory.createArray(value as ByteArray)
            ShortArray::class -> UBValueFactory.createArray(value as ShortArray)
            IntArray::class -> UBValueFactory.createArray(value as IntArray)
            LongArray::class -> UBValueFactory.createArray(value as LongArray)
            FloatArray::class -> UBValueFactory.createArray(value as FloatArray)
            DoubleArray::class -> UBValueFactory.createArray(value as DoubleArray)
            UBObject::class -> value as UBObject
            UBJ::class -> (value as UBJ).ubo
            BigInteger::class -> UBValueFactory.createArray((value as BigInteger).toByteArray())
            UUID::class -> {
                val uuid = value as UUID
                UBValueFactory.createArray(longArrayOf(uuid.mostSignificantBits, uuid.leastSignificantBits))
            }
            Vector2ic::class -> UBValueFactory.createArray((value as Vector2ic).toArray())
            Vector2fc::class -> UBValueFactory.createArray((value as Vector2fc).toArray())
            Vector2dc::class -> UBValueFactory.createArray((value as Vector2dc).toArray())
            Vector3ic::class -> UBValueFactory.createArray((value as Vector3ic).toArray())
            Vector3fc::class -> UBValueFactory.createArray((value as Vector3fc).toArray())
            Vector3dc::class -> UBValueFactory.createArray((value as Vector3dc).toArray())
            else -> {
                if(value is Array<*> && value.isArrayOf<String>()) {
                    UBValueFactory.createArray(value as Array<String>)
                } else {
                    logger.warn("Unknown type ${value::class} received, inserting null for key '$key' instead.")
                    UBValueFactory.createNull()
                }
            }
        }
    }

    fun put(key: String, value: Any?) = set(key, value)

    /** WRITING **/

    fun putChar(key: String, value: Char) = ubo.put(key, UBValueFactory.createChar(value))
    fun putBoolean(key: String, value: Boolean) = ubo.put(key, UBValueFactory.createBool(value))

    fun putByte(key: String, value: Byte) = ubo.put(key, UBValueFactory.createInt(value.toLong()))
    fun putShort(key: String, value: Short) = ubo.put(key, UBValueFactory.createInt(value.toLong()))
    fun putInt(key: String, value: Int) = ubo.put(key, UBValueFactory.createInt(value.toLong()))
    fun putLong(key: String, value: Long) = ubo.put(key, UBValueFactory.createInt(value))

    @ExperimentalUnsignedTypes
    fun putUnsignedByte(key: String, value: UByte) = ubo.put(key, UBValueFactory.createInt(value.toLong()))

    @ExperimentalUnsignedTypes
    fun putUnsignedShort(key: String, value: UShort) = ubo.put(key, UBValueFactory.createInt(value.toLong()))

    @ExperimentalUnsignedTypes
    fun putUnsignedInt(key: String, value: UInt) = ubo.put(key, UBValueFactory.createInt(value.toLong()))

    @ExperimentalUnsignedTypes
    fun putUnsignedLong(key: String, value: ULong) = ubo.put(key, UBValueFactory.createInt(value.toLong()))

    fun putBigInteger(key: String, value: BigInteger) = ubo.put(key, UBValueFactory.createArray(value.toByteArray()))

    fun putFloat(key: String, value: Float) = ubo.put(key, UBValueFactory.createFloat32(value))
    fun putDouble(key: String, value: Double) = ubo.put(key, UBValueFactory.createFloat64(value))

    fun putUUID(key: String, value: UUID) = ubo.put(key, UBValueFactory.createArray(longArrayOf(value.mostSignificantBits, value.leastSignificantBits)))

    fun putString(key: String, value: String) = ubo.put(key, UBValueFactory.createString(value))

    fun putBooleanArray(key: String, value: BooleanArray) = ubo.put(key, UBValueFactory.createArray(value))

    fun putByteArray(key: String, value: ByteArray) = ubo.put(key, UBValueFactory.createArray(value))
    fun putShortArray(key: String, value: ShortArray) = ubo.put(key, UBValueFactory.createArray(value))
    fun putIntArray(key: String, value: IntArray) = ubo.put(key, UBValueFactory.createArray(value))
    fun putLongArray(key: String, value: LongArray) = ubo.put(key, UBValueFactory.createArray(value))

    fun putFloatArray(key: String, value: FloatArray) = ubo.put(key, UBValueFactory.createArray(value))
    fun putDoubleArray(key: String, value: DoubleArray) = ubo.put(key, UBValueFactory.createArray(value))

    fun putStringArray(key: String, value: Array<String>) = ubo.put(key, UBValueFactory.createArray(value))

    fun putVector2i(key: String, value: Vector2ic) = ubo.put(key, UBValueFactory.createArray(value.toArray()))
    fun putVector2f(key: String, value: Vector2fc) = ubo.put(key, UBValueFactory.createArray(value.toArray()))
    fun putVector2d(key: String, value: Vector2dc) = ubo.put(key, UBValueFactory.createArray(value.toArray()))

    fun putVector3i(key: String, value: Vector3ic) = ubo.put(key, UBValueFactory.createArray(value.toArray()))
    fun putVector3f(key: String, value: Vector3fc) = ubo.put(key, UBValueFactory.createArray(value.toArray()))
    fun putVector3d(key: String, value: Vector3dc) = ubo.put(key, UBValueFactory.createArray(value.toArray()))

    /** READING **/

    fun readBoolean(key: String): Boolean = ubo[key]!!.asBool()
    fun readBooleanNullable(key: String): Boolean? = ubo[key]?.asBool()
    fun readBooleanOrDefault(key: String, default: Boolean): Boolean = ubo[key]?.asBool() ?: default

    fun readChar(key: String): Char = ubo[key]!!.asChar()
    fun readCharNullable(key: String): Char? = ubo[key]?.asChar()
    fun readCharOrDefault(key: String, default: Char): Char = ubo[key]?.asChar() ?: default

    fun readByte(key: String): Byte = ubo[key]!!.asByte()
    fun readByteNullable(key: String): Byte? = ubo[key]?.asByte()
    fun readByteOrDefault(key: String, default: Byte): Byte = ubo[key]?.asByte() ?: default

    fun readShort(key: String): Short = ubo[key]!!.asShort()
    fun readShortNullable(key: String): Short? = ubo[key]?.asShort()
    fun readShortOrDefault(key: String, default: Short): Short = ubo[key]?.asShort() ?: default

    fun readInt(key: String): Int = ubo[key]!!.asInt()
    fun readIntNullable(key: String): Int? = ubo[key]?.asInt()
    fun readIntOrDefault(key: String, default: Int): Int = ubo[key]?.asInt() ?: default

    fun readLong(key: String): Long = ubo[key]!!.asLong()
    fun readLongNullable(key: String): Long? = ubo[key]?.asLong()
    fun readLongOrDefault(key: String, default: Long): Long = ubo[key]?.asLong() ?: default

    @ExperimentalUnsignedTypes
    fun readUnsignedByte(key: String): UByte = ubo[key]!!.asByte().toUByte()
    @ExperimentalUnsignedTypes
    fun readUnsignedByteNullable(key: String): UByte? = ubo[key]?.asByte()?.toUByte()
    @ExperimentalUnsignedTypes
    fun readUnsignedByteOrDefault(key: String, default: UByte): UByte = ubo[key]?.asByte()?.toUByte() ?: default

    @ExperimentalUnsignedTypes
    fun readUnsignedShort(key: String): UShort = ubo[key]!!.asShort().toUShort()
    @ExperimentalUnsignedTypes
    fun readUnsignedShortNullable(key: String): UShort? = ubo[key]?.asShort()?.toUShort()
    @ExperimentalUnsignedTypes
    fun readUnsignedShortOrDefault(key: String, default: UShort): UShort = ubo[key]?.asShort()?.toUShort() ?: default

    @ExperimentalUnsignedTypes
    fun readUnsignedInt(key: String): UInt = ubo[key]!!.asInt().toUInt()
    @ExperimentalUnsignedTypes
    fun readUnsignedIntNullable(key: String): UInt? = ubo[key]?.asInt()?.toUInt()
    @ExperimentalUnsignedTypes
    fun readUnsignedIntOrDefault(key: String, default: UInt): UInt = ubo[key]?.asInt()?.toUInt() ?: default

    @ExperimentalUnsignedTypes
    fun readUnsignedLong(key: String): ULong = ubo[key]!!.asLong().toULong()
    @ExperimentalUnsignedTypes
    fun readUnsignedLongNullable(key: String): ULong? = ubo[key]?.asLong()?.toULong()
    @ExperimentalUnsignedTypes
    fun readUnsignedLongOrDefault(key: String, default: ULong): ULong = ubo[key]?.asLong()?.toULong() ?: default

    fun readBigInteger(key: String): BigInteger = BigInteger(ubo[key]!!.asByteArray())
    fun readBigIntegerNullable(key: String): BigInteger? = ubo[key]?.asByteArray()?.let { BigInteger(it) }
    fun readBigIntegerOrDefault(key: String, default: BigInteger): BigInteger = ubo[key]?.asByteArray()?.let { BigInteger(it) } ?: default

    fun readFloat(key: String): Float = ubo[key]!!.asFloat32()
    fun readFloatNullable(key: String): Float? = ubo[key]?.asFloat32()
    fun readFloatOrDefault(key: String, default: Float): Float = ubo[key]?.asFloat32() ?: default

    fun readDouble(key: String): Double = ubo[key]!!.asFloat64()
    fun readDoubleNullable(key: String): Double? = ubo[key]?.asFloat64()
    fun readDoubleOrDefault(key: String, default: Double): Double = ubo[key]?.asFloat64() ?: default

    fun readString(key: String): String = ubo[key]!!.asString()
    fun readStringNullable(key: String): String? = ubo[key]?.asString()
    fun readStringOrDefault(key: String, default: String): String = ubo[key]?.asString() ?: default

    fun readUUID(key: String): UUID = ubo[key]!!.asInt64Array().let { UUID(it[0], it[1]) }
    fun readUUIDNullable(key: String): UUID? = ubo[key]?.asInt64Array()?.let { UUID(it[0], it[1]) }
    fun readUUIDOrDefault(key: String, default: UUID): UUID = ubo[key]?.asInt64Array()?.let { UUID(it[0], it[1]) } ?: default

    fun readBooleanArray(key: String): BooleanArray = ubo[key]!!.asBoolArray()
    fun readBooleanArrayNullable(key: String): BooleanArray? = ubo[key]?.asBoolArray()
    fun readBooleanArrayOrDefault(key: String, default: BooleanArray = booleanArrayOf()): BooleanArray = ubo[key]?.asBoolArray() ?: default

    fun readByteArray(key: String): ByteArray = ubo[key]!!.asByteArray()
    fun readByteArrayNullable(key: String): ByteArray? = ubo[key]?.asByteArray()
    fun readByteArrayOrDefault(key: String, default: ByteArray = byteArrayOf()): ByteArray = ubo[key]?.asByteArray() ?: default

    fun readShortArray(key: String): ShortArray = ubo[key]!!.asShortArray()
    fun readShortArrayNullable(key: String): ShortArray? = ubo[key]?.asShortArray()
    fun readShortArrayOrDefault(key: String, default: ShortArray = shortArrayOf()): ShortArray = ubo[key]?.asShortArray() ?: default

    fun readIntArray(key: String): IntArray = ubo[key]!!.asInt32Array()
    fun readIntArrayNullable(key: String): IntArray? = ubo[key]?.asInt32Array()
    fun readIntArrayOrDefault(key: String, default: IntArray = intArrayOf()): IntArray = ubo[key]?.asInt32Array() ?: default

    fun readLongArray(key: String): LongArray = ubo[key]!!.asInt64Array()
    fun readLongArrayNullable(key: String): LongArray? = ubo[key]?.asInt64Array()
    fun readLongArrayOrDefault(key: String, default: LongArray = longArrayOf()): LongArray = ubo[key]?.asInt64Array() ?: default

    fun readFloatArray(key: String): FloatArray = ubo[key]!!.asFloat32Array()
    fun readFloatArrayNullable(key: String): FloatArray? = ubo[key]?.asFloat32Array()
    fun readFloatArrayOrDefault(key: String, default: FloatArray = floatArrayOf()): FloatArray = ubo[key]?.asFloat32Array() ?: default

    fun readDoubleArray(key: String): DoubleArray = ubo[key]!!.asFloat64Array()
    fun readDoubleArrayNullable(key: String): DoubleArray? = ubo[key]?.asFloat64Array()
    fun readDoubleArrayOrDefault(key: String, default: DoubleArray = doubleArrayOf()): DoubleArray = ubo[key]?.asFloat64Array() ?: default

    fun readStringArray(key: String): Array<String> = ubo[key]!!.asStringArray()
    fun readStringArrayNullable(key: String): Array<String>? = ubo[key]?.asStringArray()
    fun readStringArrayOrDefault(key: String, default: Array<String> = emptyArray()): Array<String> = ubo[key]?.asStringArray() ?: default

    fun readVector2i(key: String): Vector2i = ubo[key]!!.asInt32Array().let { Vector2i(it[0], it[1]) }
    fun readVector2iNullable(key: String): Vector2i? = ubo[key]?.asInt32Array()?.let { Vector2i(it[0], it[1]) }
    fun readVector2iOrDefault(key: String, default: Vector2i = Vector2i(0, 0)): Vector2i = ubo[key]?.asInt32Array()?.let { Vector2i(it[0], it[1]) } ?: default

    fun readVector2f(key: String): Vector2f = ubo[key]!!.asFloat32Array().let { Vector2f(it[0], it[1]) }
    fun readVector2fNullable(key: String): Vector2f? = ubo[key]?.asFloat32Array()?.let { Vector2f(it[0], it[1]) }
    fun readVector2fOrDefault(key: String, default: Vector2f = Vector2f(0f, 0f)): Vector2f = ubo[key]?.asFloat32Array()?.let { Vector2f(it[0], it[1]) } ?: default

    fun readVector2d(key: String): Vector2d = ubo[key]!!.asFloat64Array().let { Vector2d(it[0], it[1]) }
    fun readVector2dNullable(key: String): Vector2d? = ubo[key]?.asFloat64Array()?.let { Vector2d(it[0], it[1]) }
    fun readVector2dOrDefault(key: String, default: Vector2d = Vector2d(0.0, 0.0)): Vector2d = ubo[key]?.asFloat64Array()?.let { Vector2d(it[0], it[1]) } ?: default

    fun readVector3i(key: String): Vector3i = ubo[key]!!.asInt32Array().let { Vector3i(it[0], it[1], it[2]) }
    fun readVector3iNullable(key: String): Vector3i? = ubo[key]?.asInt32Array()?.let { Vector3i(it[0], it[1], it[2]) }
    fun readVector3iOrDefault(key: String, default: Vector3i = Vector3i(0, 0, 0)): Vector3i = ubo[key]?.asInt32Array()?.let { Vector3i(it[0], it[1], it[2]) } ?: default

    fun readVector3f(key: String): Vector3f = ubo[key]!!.asFloat32Array().let { Vector3f(it[0], it[1], it[2]) }
    fun readVector3fNullable(key: String): Vector3f? = ubo[key]?.asFloat32Array()?.let { Vector3f(it[0], it[1], it[2]) }
    fun readVector3fOrDefault(key: String, default: Vector3f = Vector3f(0f, 0f, 0f)): Vector3f = ubo[key]?.asFloat32Array()?.let { Vector3f(it[0], it[1], it[2]) } ?: default

    fun readVector3d(key: String): Vector3d = ubo[key]!!.asFloat64Array().let { Vector3d(it[0], it[1], it[2]) }
    fun readVector3dNullable(key: String): Vector3d? = ubo[key]?.asFloat64Array()?.let { Vector3d(it[0], it[1], it[2]) }
    fun readVector3dOrDefault(key: String, default: Vector3d = Vector3d(0.0, 0.0, 0.0)): Vector3d = ubo[key]?.asFloat64Array()?.let { Vector3d(it[0], it[1], it[2]) } ?: default

    /** BYTE ARRAY CONVERSIONS **/

    fun toByteArray(): ByteArray {
        val out = ByteArrayOutputStream()
        val writer = UBWriter(out)
        writer.write(ubo)
        writer.close()
        out.use { o ->
            return o.toByteArray()
        }
    }

    fun fromByteArray(input: ByteArrayInputStream): UBJ {
        val reader = UBReader(input)

        val value = reader.read()
        val obj = value.asObject()
        reader.close()

        return UBJ(obj)
    }

    override fun equals(other: Any?): Boolean = other is UBJ && ubo == other.ubo
    override fun hashCode(): Int = ubo.hashCode()
}